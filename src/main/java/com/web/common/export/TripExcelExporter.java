package com.web.common.export;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.web.common.exception.export.ExportException;
import com.web.trip.mapper.TripMapper;
import com.web.trip.model.Trip;
import com.web.trip.model.TripDto;
import com.web.trip.model.TripFilterParams;
import com.web.trip.repository.TripsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TripExcelExporter {
    private final TripsRepository tripsRepository;
    private final ExcelExporterConf conf;

    private final List<FieldAndFunctionExtractor> fieldAndFunctionExtractors = List.of(
            new FieldAndFunctionExtractor("ID", TripDto::getId),
            new FieldAndFunctionExtractor("Vendor ID", TripDto::getVendorId),
            new FieldAndFunctionExtractor("Pickup Datetime", TripDto::getPickupDatetime),
            new FieldAndFunctionExtractor("Dropoff Datetime", TripDto::getDropoffDatetime),
            new FieldAndFunctionExtractor("Passenger Count", TripDto::getPassengerCount),
            new FieldAndFunctionExtractor("Trip Distance", TripDto::getTripDistance),
            new FieldAndFunctionExtractor("Rate Code ID", TripDto::getRateCodeId),
            new FieldAndFunctionExtractor("Store and Forward Flag", TripDto::getStoreAndFwdFlag),
            new FieldAndFunctionExtractor("Pickup Location ID", TripDto::getPickupLocationId),
            new FieldAndFunctionExtractor("Dropoff Location ID", TripDto::getDropoffLocationId),
            new FieldAndFunctionExtractor("Payment Type", TripDto::getPaymentType),
            new FieldAndFunctionExtractor("Fare Amount", TripDto::getFareAmount),
            new FieldAndFunctionExtractor("Extra", TripDto::getExtra),
            new FieldAndFunctionExtractor("MTA Tax", TripDto::getMtaTax),
            new FieldAndFunctionExtractor("Tip Amount", TripDto::getTipAmount),
            new FieldAndFunctionExtractor("Tolls Amount", TripDto::getTollsAmount),
            new FieldAndFunctionExtractor("Improvement Surcharge", TripDto::getImprovementSurcharge),
            new FieldAndFunctionExtractor("Total Amount", TripDto::getTotalAmount),
            new FieldAndFunctionExtractor("Congestion Surcharge", TripDto::getCongestionSurcharge),
            new FieldAndFunctionExtractor("Airport Fee", TripDto::getAirportFee)
    );

    public void tripsToExcelStream(OutputStream outputStream, TripFilterParams filterParams) throws IOException {
        ExportContext context = initializeExport(outputStream);

        try {
            processTrips(context, filterParams);
        } catch (IOException e) {
            log.error("Ошибка во время экспорта в xlsx: {}", e.getMessage());
            throw new ExportException("Ошибка во время экспорта в xlsx", e);
        } finally {
            finalizeExport(context);
        }
    }

    private ExportContext initializeExport(OutputStream outputStream) {
        StopWatch watch = new StopWatch();
        watch.start();

        Workbook workbook = new Workbook(outputStream, "Trips Export", "1.0");
        Worksheet currentSheet = createNewSheet(workbook, 1);

        return new ExportContext(
                workbook,
                outputStream,
                currentSheet,
                1,
                1,
                1,
                watch.getTime(),
                watch
        );
    }

    private void processTrips(ExportContext context, TripFilterParams params) throws IOException {
        Pageable pageable = params.getPageable();
        Page<Trip> page = tripsRepository.filter(
                params.startDateTime(),
                params.endDateTime(),
                params.minWindSpeed(),
                params.maxWindSpeed(),
                pageable
        );

        while (page.hasContent()) {
            List<TripDto> tripDtos = page.map(TripMapper.INSTANCE::tripToTripDto).getContent();

            for (TripDto tripDto : tripDtos) {
                if (context.getCurrentRow() > conf.getMaxRowsPerSheet()) {
                    context.getCurrentSheet().finish();
                    context.getOutputStream().flush();

                    context.setTotalPages(context.getTotalPages() + 1);
                    Worksheet newSheet = createNewSheet(context.getWorkbook(), context.getTotalPages());
                    context.setCurrentSheet(newSheet);
                    context.setCurrentRow(1);
                    log.info("Создан новый лист: {}", conf.getSheetPrefix() + context.getTotalPages());
                }

                writeRow(context.getCurrentSheet(), context.getCurrentRow(), tripDto);
                context.setCurrentRow(context.getCurrentRow() + 1);

                if (context.getCurrentRow() % conf.getBatchSize() == 0) {
                    Runtime rt = Runtime.getRuntime();
                    long usedMB = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
                    long currentTime = context.getWatch().getTime();
                    log.info("Партия #{} завершена за {} секунд, использование памяти: {} MB",
                            context.getBatchCounter(),
                            (currentTime - context.getLastSplitTime()) / 1000.0,
                            usedMB);
                    context.setBatchCounter(context.getBatchCounter() + 1);
                    context.setLastSplitTime(currentTime);
                }
            }

            if (page.hasNext()) {
                pageable = page.nextPageable();
                page = tripsRepository.filter(
                        params.startDateTime(),
                        params.endDateTime(),
                        params.minWindSpeed(),
                        params.maxWindSpeed(),
                        pageable
                );
            } else {
                break;
            }
        }
    }

    private void finalizeExport(ExportContext context) {
        context.getWatch().stop();
        log.info("Total export time: {} seconds", context.getWatch().getTime(TimeUnit.SECONDS));

        try {
            if (context.getWorkbook() != null) {
                context.getWorkbook().finish();
            }
            context.getOutputStream().flush();
        } catch (IOException e) {
            log.error("Ошибка при завершении экспорта: {}", e.getMessage());
            throw new ExportException("Ошибка во время завершения экспорта в xlsx", e);
        }
    }

    private Worksheet createNewSheet(Workbook workbook, int sheetNumber) {
        Worksheet sheet = workbook.newWorksheet(conf.getSheetPrefix() + sheetNumber);
        writeHeaders(sheet);
        return sheet;
    }

    private void writeHeaders(Worksheet sheet) {
        for (int i = 0; i < fieldAndFunctionExtractors.size(); i++) {
            sheet.value(0, i, fieldAndFunctionExtractors.get(i).name());
        }
    }

    private void writeRow(Worksheet sheet, int rowIdx, TripDto trip) {
        for (int i = 0; i < fieldAndFunctionExtractors.size(); i++) {
            Object value = fieldAndFunctionExtractors.get(i).extractor().apply(trip);
            if (value != null) {
                switch (value) {
                    case LocalDateTime localDateTime -> sheet.value(rowIdx, i, localDateTime.toString());

                    case Number number -> sheet.value(rowIdx, i, number.doubleValue());

                    default -> sheet.value(rowIdx, i, value.toString());
                }
            }
        }
    }
}