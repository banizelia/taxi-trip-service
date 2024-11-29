package com.web.common.export;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.apache.commons.lang3.time.StopWatch;
import java.io.OutputStream;

@Getter
@Setter
@AllArgsConstructor
public class ExportContext {
    private Workbook workbook;
    private OutputStream outputStream;
    private Worksheet currentSheet;
    private int totalPages;
    private int currentRow;
    private int batchCounter;
    private long lastSplitTime;
    private StopWatch watch;
}