package com.web.trip.service;

import com.web.common.exception.export.ExportException;
import com.web.common.export.TripExcelExporter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
    private TripExcelExporter tripExcelExporter;

    @Mock
    private OutputStream outputStream;

    @InjectMocks
    private DownloadTripService downloadTripService;

    @BeforeEach
    void setUp() {
        // No additional setup needed as we use @InjectMocks
    }

    @Test
    void execute_SuccessfulExport() throws IOException {
        // Arrange
        doNothing().when(tripExcelExporter).tripsToExcelStream(any(OutputStream.class));

        // Act
        StreamingResponseBody result = downloadTripService.execute();

        // Assert
        assertNotNull(result);

        // Verify the streaming works
        result.writeTo(outputStream);

        verify(tripExcelExporter, times(1)).tripsToExcelStream(outputStream);
        verifyNoMoreInteractions(tripExcelExporter);
    }

    @Test
    void execute_ThrowsExportException_WhenIOExceptionOccurs() throws IOException {
        // Arrange
        doThrow(new IOException("Test IO Exception"))
                .when(tripExcelExporter)
                .tripsToExcelStream(any(OutputStream.class));

        // Act
        StreamingResponseBody result = downloadTripService.execute();

        // Assert
        assertNotNull(result);

        // Verify that ExportException is thrown when writing to stream
        ExportException exception = assertThrows(
                ExportException.class,
                () -> result.writeTo(outputStream)
        );

        assertEquals("Error exporting data to Excel", exception.getMessage());
        assertTrue(exception.getCause() instanceof IOException);
        assertEquals("Test IO Exception", exception.getCause().getMessage());

        verify(tripExcelExporter, times(1)).tripsToExcelStream(outputStream);
        verifyNoMoreInteractions(tripExcelExporter);
    }

    @Test
    void execute_EnsuresOutputStreamIsClosed() throws IOException {
        // Arrange
        doNothing().when(tripExcelExporter).tripsToExcelStream(any(OutputStream.class));

        // Act
        StreamingResponseBody result = downloadTripService.execute();
        result.writeTo(outputStream);

        // Assert
        verify(outputStream, times(1)).close();
    }

    @Test
    void execute_EnsuresOutputStreamIsClosedEvenOnError() throws IOException {
        // Arrange
        doThrow(new IOException("Test IO Exception"))
                .when(tripExcelExporter)
                .tripsToExcelStream(any(OutputStream.class));

        // Act
        StreamingResponseBody result = downloadTripService.execute();

        // Assert
        assertThrows(ExportException.class, () -> result.writeTo(outputStream));
        verify(outputStream, times(1)).close();
    }

    @Test
    void execute_ReturnsUniqueStreamingResponseBody() {
        // Act
        StreamingResponseBody result1 = downloadTripService.execute();
        StreamingResponseBody result2 = downloadTripService.execute();

        // Assert
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotSame(result1, result2, "Each call should return a new StreamingResponseBody instance");
    }
}