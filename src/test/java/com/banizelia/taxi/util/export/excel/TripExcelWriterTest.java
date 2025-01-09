package com.banizelia.taxi.util.export.excel;

import com.banizelia.taxi.trip.export.excel.TripExcelWriter;
import org.dhatim.fastexcel.Worksheet;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TripExcelWriterTest {
    @Test
    void testWriteHeaders() {
        TripExcelWriter writer = new TripExcelWriter();
        Worksheet sheet = Mockito.mock(Worksheet.class);
        writer.writeHeaders(sheet);
        Mockito.verify(sheet, Mockito.atLeastOnce()).value(Mockito.eq(0), Mockito.anyInt(), Mockito.anyString());
    }
}
