package com.web.common.export;

import org.apache.commons.lang3.time.StopWatch;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExportContextTest {

    private ExportContext context;
    private Workbook workbook;
    private ByteArrayOutputStream outputStream;
    private Worksheet worksheet;
    private StopWatch stopWatch;

    @BeforeEach
    void setUp() {
        workbook = new Workbook(outputStream = new ByteArrayOutputStream(), "TestWorkbook", "1.0");
        worksheet = workbook.newWorksheet("Sheet1");
        stopWatch = StopWatch.createStarted();
        context = new ExportContext(workbook, outputStream, worksheet, 10, 1, 0, System.currentTimeMillis(), stopWatch);
    }

    @Test
    void testInitialValues() {
        assertEquals(workbook, context.getWorkbook());
        assertEquals(outputStream, context.getOutputStream());
        assertEquals(worksheet, context.getCurrentSheet());
        assertEquals(10, context.getTotalPages());
        assertEquals(1, context.getCurrentRow());
        assertEquals(0, context.getBatchCounter());
        assertTrue(context.getLastSplitTime() > 0);
        assertEquals(stopWatch, context.getWatch());
    }

    @Test
    void testSetters() {
        context.setTotalPages(20);
        context.setCurrentRow(5);
        context.setBatchCounter(3);

        assertEquals(20, context.getTotalPages());
        assertEquals(5, context.getCurrentRow());
        assertEquals(3, context.getBatchCounter());
    }

    @Test
    void testStopWatchOperations() {
        StopWatch newWatch = StopWatch.createStarted();
        context.setWatch(newWatch);
        assertEquals(newWatch, context.getWatch());
    }
}
