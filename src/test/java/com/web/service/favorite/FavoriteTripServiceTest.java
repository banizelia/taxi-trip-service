package com.web.service.favorite;

import com.web.model.dto.TripDto;
import com.web.service.favorite.managment.DeleteFavoriteTripService;
import com.web.service.favorite.managment.DragAndDropFavoriteTripService;
import com.web.service.favorite.managment.GetFavoriteTripService;
import com.web.service.favorite.managment.SaveFavoriteTripService;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteTripServiceTest {

    @Mock
    private GetFavoriteTripService getFavoriteTripService;

    @Mock
    private SaveFavoriteTripService saveFavoriteTripService;

    @Mock
    private DeleteFavoriteTripService deleteFavoriteTripService;

    @Mock
    private DragAndDropFavoriteTripService dragAndDropFavoriteTripService;

    private FavoriteTripService favoriteTripService;

    @BeforeEach
    void setUp() {
        favoriteTripService = new FavoriteTripService(
                getFavoriteTripService,
                saveFavoriteTripService,
                deleteFavoriteTripService,
                dragAndDropFavoriteTripService
        );
    }

    @Test
    void getFavouriteTrips_ShouldDelegateToGetFavoriteTripService() {
        // Arrange
        List<TripDto> expectedTrips = Arrays.asList(
                new TripDto(),
                new TripDto()
        );
        when(getFavoriteTripService.execute()).thenReturn(expectedTrips);

        // Act
        List<TripDto> result = favoriteTripService.getFavouriteTrips();

        // Assert
        assertSame(expectedTrips, result);
        verify(getFavoriteTripService).execute();
        verifyNoMoreInteractions(getFavoriteTripService);
        verifyNoInteractions(saveFavoriteTripService, deleteFavoriteTripService, dragAndDropFavoriteTripService);
    }

    @Test
    void saveToFavourite_ShouldDelegateToSaveFavoriteTripService() {
        // Arrange
        Long tripId = 1L;

        // Act
        favoriteTripService.saveToFavourite(tripId);

        // Assert
        verify(saveFavoriteTripService).execute(tripId);
        verifyNoMoreInteractions(saveFavoriteTripService);
        verifyNoInteractions(getFavoriteTripService, deleteFavoriteTripService, dragAndDropFavoriteTripService);
    }

    @Test
    void saveToFavourite_WhenServiceThrowsBadRequestException_ShouldPropagateException(){
        // Arrange
        Long tripId = 1L;
        IllegalArgumentException expectedException = new IllegalArgumentException("Test exception");
        doThrow(expectedException).when(saveFavoriteTripService).execute(tripId);

        // Act & Assert
        IllegalArgumentException thrownException = assertThrows(
                IllegalArgumentException.class,
                () -> favoriteTripService.saveToFavourite(tripId)
        );

        assertSame(expectedException, thrownException);
        verify(saveFavoriteTripService).execute(tripId);
    }

    @Test
    void deleteFromFavourite_ShouldDelegateToDeleteFavoriteTripService() {
        // Arrange
        Long tripId = 1L;

        // Act
        favoriteTripService.deleteFromFavourite(tripId);

        // Assert
        verify(deleteFavoriteTripService).execute(tripId);
        verifyNoMoreInteractions(deleteFavoriteTripService);
        verifyNoInteractions(getFavoriteTripService, saveFavoriteTripService, dragAndDropFavoriteTripService);
    }

    @Test
    void dragAndDrop_ShouldDelegateToDragAndDropFavoriteTripService() {
        // Arrange
        Long tripId = 1L;
        Long newPosition = 2L;

        // Act
        favoriteTripService.dragAndDrop(tripId, newPosition);

        // Assert
        verify(dragAndDropFavoriteTripService).execute(tripId, newPosition);
        verifyNoMoreInteractions(dragAndDropFavoriteTripService);
        verifyNoInteractions(getFavoriteTripService, saveFavoriteTripService, deleteFavoriteTripService);
    }

    @Test
    void dragAndDrop_WhenServiceThrowsBadRequestException_ShouldPropagateException() {
        // Arrange
        Long tripId = 1L;
        Long newPosition = 2L;
        IllegalArgumentException expectedException = new IllegalArgumentException("Test exception");
        doThrow(expectedException).when(dragAndDropFavoriteTripService).execute(tripId, newPosition);

        // Act & Assert
        IllegalArgumentException thrownException = assertThrows(
                IllegalArgumentException.class,
                () -> favoriteTripService.dragAndDrop(tripId, newPosition)
        );

        assertSame(expectedException, thrownException);
        verify(dragAndDropFavoriteTripService).execute(tripId, newPosition);
    }
}