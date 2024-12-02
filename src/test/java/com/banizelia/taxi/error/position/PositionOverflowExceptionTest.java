package com.banizelia.taxi.error.position;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PositionOverflowExceptionTest {

    @Test
    void testPositionOverflowExceptionMessage() {
        long maxPosition = 922337203685477580L;
        double thresholdPercent = 95.0;

        PositionOverflowException exception = new PositionOverflowException(maxPosition, thresholdPercent);

        String expectedMessage = String.format(
                "Unable to calculate next position even after sparsification. Current max position: %d exceeds %f%% of Long.MAX_VALUE",
                maxPosition,
                thresholdPercent
        );
        assertEquals(expectedMessage, exception.getMessage());
    }
}
