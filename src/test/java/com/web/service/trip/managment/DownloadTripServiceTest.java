package com.web.service.trip.managment;

import com.web.export.TripExcelExporterFastExcel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DownloadTripServiceTest {

    @Mock
    private TripExcelExporterFastExcel tripExcelExporterFastExcel;

    @Mock
    private OutputStream outputStream;

    private DownloadTripService downloadTripService;

    @BeforeEach
    void setUp() {
        downloadTripService = new DownloadTripService(tripExcelExporterFastExcel);
    }

    @Test
    void execute_ShouldReturnStreamingResponseBody() {
        StreamingResponseBody result = downloadTripService.execute();
        assertNotNull(result, "StreamingResponseBody should not be null");
    }

    @Test
    void execute_ShouldCallExporterAndWriteToStream() throws IOException {
        // Act
        StreamingResponseBody responseBody = downloadTripService.execute();

        // Assert & Act
        assertDoesNotThrow(() -> responseBody.writeTo(outputStream));

        // Verify
        verify(tripExcelExporterFastExcel, times(1))
                .tripsToExcelStream(outputStream);
    }

    @Test
    void execute_WhenIOExceptionOccurs_ShouldThrowIllegalStateException() throws IOException {
        // Arrange
        doThrow(new IOException("Test exception"))
                .when(tripExcelExporterFastExcel)
                .tripsToExcelStream(any(OutputStream.class));

        StreamingResponseBody responseBody = downloadTripService.execute();

        // Act & Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> responseBody.writeTo(outputStream)
        );

        assertTrue(exception.getMessage().contains("Error exporting data to Excel"));
        assertTrue(exception.getCause() instanceof IOException);
    }
}