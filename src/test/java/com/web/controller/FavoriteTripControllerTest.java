//package com.web.controller;
//
//import com.web.model.Trip;
//import com.web.service.FavoriteTripService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//class FavoriteTripControllerTest {
//
//    @Mock
//    private FavoriteTripService favoriteTripService;
//
//    @InjectMocks
//    private FavoriteTripController favoriteTripController;
//
//    private MockMvc mockMvc;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(favoriteTripController).build();
//    }
//
//    @Test
//    void getFavouriteTrips_ReturnsListOfTrips() throws Exception {
//        List<Trip> trips = List.of(new Trip(), new Trip());
//        when(favoriteTripService.getFavouriteTrips()).thenReturn(ResponseEntity.ok(trips));
//
//        mockMvc.perform(get("/api/v1/favorite-trips"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(trips.size()));
//    }
//
//    @Test
//    void saveToFavourite_Success() throws Exception {
//        when(favoriteTripService.saveToFavourite(anyLong())).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body("Trip added to favorites"));
//
//        mockMvc.perform(put("/api/v1/favorite-trips/")
//                        .param("tripId", "1"))
//                .andExpect(status().isCreated())
//                .andExpect(content().string("Trip added to favorites"));
//    }
//
//    @Test
//    void saveToFavourite_TripAlreadyExists_ReturnsBadRequest() throws Exception {
//        when(favoriteTripService.saveToFavourite(anyLong())).thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Trip already in the table"));
//
//        mockMvc.perform(put("/api/v1/favorite-trips/")
//                        .param("tripId", "1"))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Trip already in the table"));
//    }
//
//    @Test
//    void deleteFromFavourite_Success() throws Exception {
//        when(favoriteTripService.deleteFromFavourite(anyLong())).thenReturn(ResponseEntity.ok("Trip deleted successfully"));
//
//        mockMvc.perform(delete("/api/v1/favorite-trips")
//                        .param("tripId", "1"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Trip deleted successfully"));
//    }
//
//    @Test
//    void deleteFromFavourite_TripNotFound_ReturnsNotFound() throws Exception {
//        when(favoriteTripService.deleteFromFavourite(anyLong())).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trip not found"));
//
//        mockMvc.perform(delete("/api/v1/favorite-trips")
//                        .param("tripId", "1"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("Trip not found"));
//    }
//
//    @Test
//    void dragAndDrop_Success() throws Exception {
//        when(favoriteTripService.dragAndDrop(anyLong(), anyLong())).thenReturn(ResponseEntity.ok("Position updated successfully"));
//
//        mockMvc.perform(put("/api/v1/favorite-trips/drag-and-drop")
//                        .param("tripId", "1")
//                        .param("newPosition", "2"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Position updated successfully"));
//    }
//
//    @Test
//    void dragAndDrop_TripNotFound_ReturnsNotFound() throws Exception {
//        when(favoriteTripService.dragAndDrop(anyLong(), anyLong())).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trip not found"));
//
//        mockMvc.perform(put("/api/v1/favorite-trips/drag-and-drop")
//                        .param("tripId", "1")
//                        .param("newPosition", "2"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("Trip not found"));
//    }
//}
