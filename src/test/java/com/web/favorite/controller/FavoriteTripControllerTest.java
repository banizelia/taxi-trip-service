package com.web.favorite.controller;

import com.web.favorite.service.*;
import com.web.trip.model.TripDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteTripControllerTest {

    @Mock
    private SaveFavoriteTripService saveFavoriteTripService;

    @Mock
    private DeleteFavoriteTripService deleteFavoriteTripService;

    @Mock
    private DragAndDropFavoriteTripService dragAndDropFavoriteTripService;

    @Mock
    private GetFavoriteTripService getFavoriteTripService;

    @Mock
    private PagedResourcesAssembler<TripDto> pagedResourcesAssembler;

    @InjectMocks
    private FavoriteTripController controller;

    private TripDto tripDto;
    private Page<TripDto> tripPage;
    private PagedModel<EntityModel<TripDto>> pagedModel;

    @BeforeEach
    void setUp() {
        tripDto = new TripDto();
        tripDto.setId(1L);
        tripPage = new PageImpl<>(List.of(tripDto));

        @SuppressWarnings("unchecked")
        PagedModel<EntityModel<TripDto>> mockedPagedModel = mock(PagedModel.class);
        pagedModel = mockedPagedModel;
    }

    @Nested
    class GetFavouriteTripsTests {

        @Test
        void getFavouriteTrips_WithDefaultParams_ShouldReturnPagedResponse() {
            // Arrange
            when(getFavoriteTripService.execute(anyInt(), anyInt(), anyString(), anyString()))
                    .thenReturn(tripPage);
            when(pagedResourcesAssembler.toModel(any(Page.class))).thenReturn(pagedModel);

            // Act
            ResponseEntity<PagedModel<EntityModel<TripDto>>> response =
                    controller.getFavouriteTrips(0, 20, "position", "asc");

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            verify(getFavoriteTripService).execute(0, 20, "position", "asc");
            verify(pagedResourcesAssembler).toModel(tripPage);
        }

        @Test
        void getFavouriteTrips_WithCustomParams_ShouldUseProvidedValues() {
            // Arrange
            when(getFavoriteTripService.execute(anyInt(), anyInt(), anyString(), anyString()))
                    .thenReturn(tripPage);
            when(pagedResourcesAssembler.toModel(any(Page.class))).thenReturn(pagedModel);

            // Act
            ResponseEntity<PagedModel<EntityModel<TripDto>>> response =
                    controller.getFavouriteTrips(1, 50, "id", "desc");

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(getFavoriteTripService).execute(1, 50, "id", "desc");
        }

        @Test
        void getFavouriteTrips_WithEmptyResult_ShouldReturnEmptyPage() {
            // Arrange
            Page<TripDto> emptyPage = new PageImpl<>(List.of());
            when(getFavoriteTripService.execute(anyInt(), anyInt(), anyString(), anyString()))
                    .thenReturn(emptyPage);
            when(pagedResourcesAssembler.toModel(any(Page.class))).thenReturn(pagedModel);

            // Act
            ResponseEntity<PagedModel<EntityModel<TripDto>>> response =
                    controller.getFavouriteTrips(0, 20, "position", "asc");

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(pagedResourcesAssembler).toModel(emptyPage);
        }
    }

    @Nested
    class SaveToFavouriteTests {

        @Test
        void saveToFavourite_WithValidId_ShouldReturnCreated() {
            // Arrange
            Long tripId = 1L;
            doNothing().when(saveFavoriteTripService).execute(tripId);

            // Act
            ResponseEntity<String> response = controller.saveToFavourite(tripId);

            // Assert
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals("Trip added to favorites", response.getBody());
            verify(saveFavoriteTripService).execute(tripId);
        }
    }

    @Nested
    class DeleteFromFavouriteTests {

        @Test
        void deleteFromFavourite_WithValidId_ShouldReturnOk() {
            // Arrange
            Long tripId = 1L;
            doNothing().when(deleteFavoriteTripService).execute(tripId);

            // Act
            ResponseEntity<String> response = controller.deleteFromFavourite(tripId);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Trip deleted successfully", response.getBody());
            verify(deleteFavoriteTripService).execute(tripId);
        }
    }

    @Nested
    class DragAndDropTests {

        @Test
        void dragAndDrop_WithValidParams_ShouldReturnOk() {
            // Arrange
            Long tripId = 1L;
            Long newPosition = 2L;
            doNothing().when(dragAndDropFavoriteTripService).execute(tripId, newPosition);

            // Act
            ResponseEntity<String> response = controller.dragAndDrop(tripId, newPosition);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Position updated successfully", response.getBody());
            verify(dragAndDropFavoriteTripService).execute(tripId, newPosition);
        }
    }
}