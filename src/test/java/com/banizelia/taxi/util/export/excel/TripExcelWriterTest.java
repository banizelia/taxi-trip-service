package com.banizelia.taxi.util.export.excel;

import org.dhatim.fastexcel.Worksheet;
import org.dhatim.fastexcel.Workbook;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TripExcelWriterTest {
    @Test
    void testWriteHeaders() {
        Workbook wb = Mockito.mock(Workbook.class);
        Worksheet ws = Mockito.mock(Worksheet.class);
        Mockito.when(wb.newWorksheet("test")).thenReturn(ws);

        TripExcelWriter writer = new TripExcelWriter();
        Worksheet sheet = wb.newWorksheet("test");

        writer.writeHeaders(sheet);
        Mockito.verify(ws, Mockito.times(20)).value(Mockito.eq(0), Mockito.anyInt(), Mockito.anyString());
    }
}