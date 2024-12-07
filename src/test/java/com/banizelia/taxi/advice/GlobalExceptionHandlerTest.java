package com.banizelia.taxi.advice;

import com.banizelia.taxi.error.export.ExportException;
import com.banizelia.taxi.error.filter.FilterValidationException;
import com.banizelia.taxi.error.filter.InvalidSortDirectionException;
import com.banizelia.taxi.error.filter.InvalidSortFieldException;
import com.banizelia.taxi.error.filter.InvalidWindSpeedRangeException;
import com.banizelia.taxi.error.initialization.ApplicationInitializationException;
import com.banizelia.taxi.error.initialization.InvalidTimeZoneException;
import com.banizelia.taxi.error.position.PositionException;
import com.banizelia.taxi.error.position.PositionOverflowException;
import com.banizelia.taxi.error.trip.FavoriteTripModificationException;
import com.banizelia.taxi.error.trip.TripAlreadyInFavoritesException;
import com.banizelia.taxi.error.trip.TripNotFoundException;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = GlobalExceptionHandlerTest.TestController.class)
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @RestController
    @RequestMapping("/test")
    public static class TestController {

        @GetMapping("/optimistic-lock")
        public void throwOptimisticLockException() {
            throw new OptimisticLockException("Optimistic lock failed");
        }

        @GetMapping("/export")
        public void throwExportException() {
            throw new ExportException("Export failed due to IO error", new IOException("Disk not found"));
        }

        @GetMapping("/filter-validation")
        public void throwFilterValidationException() {
            throw new FilterValidationException("Invalid filter parameters");
        }

        @GetMapping("/invalid-sort-direction")
        public void throwInvalidSortDirectionException() {
            throw new InvalidSortDirectionException("DOWNWARD");
        }

        @GetMapping("/invalid-sort-field")
        public void throwInvalidSortFieldException() {
            throw new InvalidSortFieldException("invalidField");
        }

        @GetMapping("/invalid-wind-speed-range")
        public void throwInvalidWindSpeedRangeException() {
            throw new InvalidWindSpeedRangeException(10.0, 5.0);
        }

        @GetMapping("/application-initialization")
        public void throwApplicationInitializationException() {
            throw new ApplicationInitializationException("Initialization failed", new RuntimeException("DB not reachable"));
        }

        @GetMapping("/invalid-timezone")
        public void throwInvalidTimeZoneException() {
            throw new InvalidTimeZoneException("Invalid/Timezone");
        }

        @GetMapping("/position")
        public void throwPositionException() {
            throw new PositionException("Position calculation error");
        }

        @GetMapping("/position-overflow")
        public void throwPositionOverflowException() {
            throw new PositionOverflowException(999999999L, 0.85);
        }

        @GetMapping("/favorite-trip-modification")
        public void throwFavoriteTripModificationException() {
            throw new FavoriteTripModificationException("Modification failed", new RuntimeException("Concurrent update"));
        }

        @GetMapping("/trip-already-in-favorites")
        public void throwTripAlreadyInFavoritesException() {
            throw new TripAlreadyInFavoritesException(123L);
        }

        @GetMapping("/trip-not-found")
        public void throwTripNotFoundException() {
            throw new TripNotFoundException(456L);
        }

        @GetMapping("/general-exception")
        public void throwGeneralException() {
            throw new RuntimeException("General exception");
        }
    }

    @Nested
    @DisplayName("GlobalExceptionHandler Tests")
    class ExceptionHandlerTests {

        @Test
        @DisplayName("Handle OptimisticLockException")
        void handleOptimisticLockException() throws Exception {
            mockMvc.perform(get("/test/optimistic-lock"))
                    .andExpect(status().isConflict())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(409)))
                    .andExpect(jsonPath("$.error", is("Concurrent Modification Error")))
                    .andExpect(jsonPath("$.message", is("Optimistic lock failed")));
        }

        @Test
        @DisplayName("Handle ExportException")
        void handleExportException() throws Exception {
            mockMvc.perform(get("/test/export"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(500)))
                    .andExpect(jsonPath("$.error", is("Export Operation Failed")))
                    .andExpect(jsonPath("$.message", is("Export failed due to IO error")));
        }

        @Test
        @DisplayName("Handle FilterValidationException")
        void handleFilterValidationException() throws Exception {
            mockMvc.perform(get("/test/filter-validation"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.error", is("Filter Validation Error")))
                    .andExpect(jsonPath("$.message", is("Invalid filter parameters")));
        }

        @Test
        @DisplayName("Handle InvalidSortDirectionException")
        void handleInvalidSortDirectionException() throws Exception {
            mockMvc.perform(get("/test/invalid-sort-direction"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.error", is("Sort Error")))
                    .andExpect(jsonPath("$.message", is("Invalid direction: DOWNWARD")));
        }

        @Test
        @DisplayName("Handle InvalidSortFieldException")
        void handleInvalidSortFieldException() throws Exception {
            mockMvc.perform(get("/test/invalid-sort-field"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.error", is("Sort Error")))
                    .andExpect(jsonPath("$.message", is("Invalid sort field: invalidField")));
        }

        @Test
        @DisplayName("Handle InvalidWindSpeedRangeException")
        void handleInvalidWindSpeedRangeException() throws Exception {
            mockMvc.perform(get("/test/invalid-wind-speed-range"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.error", is("Invalid Wind Speed Range")));
        }

        @Test
        @DisplayName("Handle ApplicationInitializationException")
        void handleApplicationInitializationException() throws Exception {
            mockMvc.perform(get("/test/application-initialization"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(500)))
                    .andExpect(jsonPath("$.error", is("Application Initialization Error")))
                    .andExpect(jsonPath("$.message", is("Initialization failed")));
        }

        @Test
        @DisplayName("Handle InvalidTimeZoneException")
        void handleInvalidTimeZoneException() throws Exception {
            mockMvc.perform(get("/test/invalid-timezone"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.error", is("Invalid Timezone Error")))
                    .andExpect(jsonPath("$.message", is("Invalid timezone specified: Invalid/Timezone")));
        }

        @Test
        @DisplayName("Handle PositionException")
        void handlePositionException() throws Exception {
            mockMvc.perform(get("/test/position"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(500)))
                    .andExpect(jsonPath("$.error", is("Position Processing Error")))
                    .andExpect(jsonPath("$.message", is("Position calculation error")));
        }

        @Test
        @DisplayName("Handle PositionOverflowException")
        void handlePositionOverflowException() throws Exception {
            mockMvc.perform(get("/test/position-overflow"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(500)))
                    .andExpect(jsonPath("$.error", is("Position Overflow Error")));
        }

        @Test
        @DisplayName("Handle FavoriteTripModificationException")
        void handleFavoriteTripModificationException() throws Exception {
            mockMvc.perform(get("/test/favorite-trip-modification"))
                    .andExpect(status().isConflict())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(409)))
                    .andExpect(jsonPath("$.error", is("Concurrent Modification Error")))
                    .andExpect(jsonPath("$.message", is("Modification failed")));
        }

        @Test
        @DisplayName("Handle TripAlreadyInFavoritesException")
        void handleTripAlreadyInFavoritesException() throws Exception {
            mockMvc.perform(get("/test/trip-already-in-favorites"))
                    .andExpect(status().isConflict())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(409)))
                    .andExpect(jsonPath("$.error", is("Trip Already In Favorites")))
                    .andExpect(jsonPath("$.message", is("Trip with ID 123 is already in favorites")));
        }

        @Test
        @DisplayName("Handle TripNotFoundException")
        void handleTripNotFoundException() throws Exception {
            mockMvc.perform(get("/test/trip-not-found"))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(404)))
                    .andExpect(jsonPath("$.error", is("Trip Not Found")))
                    .andExpect(jsonPath("$.message", is("Such trip doesn't exist: 456")));
        }

        @Test
        @DisplayName("Handle General Exception")
        void handleGeneralException() throws Exception {
            mockMvc.perform(get("/test/general-exception"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(500)))
                    .andExpect(jsonPath("$.error", is("Internal Server Error")))
                    .andExpect(jsonPath("$.message", is("An unexpected error occurred.")));
        }
    }
}
