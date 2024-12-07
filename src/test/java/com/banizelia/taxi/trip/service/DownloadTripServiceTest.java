package com.banizelia.taxi.trip.service;

import com.banizelia.taxi.error.export.ExportException;
import com.banizelia.taxi.util.export.excel.TripExcelExporter;
import com.banizelia.taxi.trip.model.TripFilterParams;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import java.io.IOException;
import java.io.OutputStream;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DownloadTripServiceTest {

    @Mock
    private TripExcelExporter tripExcelExporter;

    @Mock
    private OutputStream outputStream;

    @InjectMocks
    private DownloadTripService downloadTripService;

    private TripFilterParams sampleFilterParams;

    @Test
    void execute_SuccessfulExport() throws IOException {
        doNothing().when(tripExcelExporter).exportTrips(any(OutputStream.class), eq(sampleFilterParams));
        StreamingResponseBody result = downloadTripService.execute(sampleFilterParams);
        assertNotNull(result);
        result.writeTo(outputStream);
        verify(tripExcelExporter, times(1)).exportTrips(outputStream, sampleFilterParams);
        verifyNoMoreInteractions(tripExcelExporter);
    }

    @Test
    void execute_ThrowsExportException_WhenIOExceptionOccurs() throws IOException {
        doThrow(new IOException("Test IO Exception"))
                .when(tripExcelExporter)
                .exportTrips(any(OutputStream.class), eq(sampleFilterParams));
        StreamingResponseBody result = downloadTripService.execute(sampleFilterParams);
        assertNotNull(result, "StreamingResponseBody не должен быть null");
        ExportException exception = assertThrows(
                ExportException.class,
                () -> result.writeTo(outputStream));

        assertEquals("Error exporting data to Excel", exception.getMessage());
        assertInstanceOf(IOException.class, exception.getCause());
        assertEquals("Test IO Exception", exception.getCause().getMessage());

        verify(tripExcelExporter, times(1)).exportTrips(outputStream, sampleFilterParams);
        verifyNoMoreInteractions(tripExcelExporter);
    }

    @Test
    void execute_EnsuresOutputStreamIsClosed() throws IOException {
        doNothing().when(tripExcelExporter).exportTrips(any(OutputStream.class), eq(sampleFilterParams));
        StreamingResponseBody result = downloadTripService.execute(sampleFilterParams);
        result.writeTo(outputStream);
        verify(outputStream, times(1)).close();
        verify(tripExcelExporter, times(1)).exportTrips(outputStream, sampleFilterParams);
        verifyNoMoreInteractions(tripExcelExporter);
    }

    @Test
    void execute_EnsuresOutputStreamIsClosedEvenOnError() throws IOException {
        doThrow(new IOException("Test IO Exception"))
                .when(tripExcelExporter)
                .exportTrips(any(OutputStream.class), eq(sampleFilterParams));
        StreamingResponseBody result = downloadTripService.execute(sampleFilterParams);
        assertThrows(ExportException.class, () -> result.writeTo(outputStream));
        verify(outputStream, times(1)).close();
        verify(tripExcelExporter, times(1)).exportTrips(outputStream, sampleFilterParams);
        verifyNoMoreInteractions(tripExcelExporter);
    }

    @Test
    void execute_ReturnsUniqueStreamingResponseBody() {
        StreamingResponseBody result1 = downloadTripService.execute(sampleFilterParams);
        StreamingResponseBody result2 = downloadTripService.execute(sampleFilterParams);

        assertNotNull(result1);
        assertNotNull(result2);
        assertNotSame(result1, result2);
    }
}
