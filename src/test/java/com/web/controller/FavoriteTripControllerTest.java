package com.web.controller;

import com.web.model.Trip;
import com.web.service.FavoriteTripService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FavoriteTripController.class)
public class FavoriteTripControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FavoriteTripService favoriteTripService;

    private Trip trip;

    @BeforeEach
    public void setUp() {
        trip = new Trip();
        trip.setId(1L);
    }

    @Test
    public void testGetFavouriteTrips() throws Exception {
        when(favoriteTripService.getFavouriteTrips()).thenReturn(Collections.singletonList(trip));

        mockMvc.perform(MockMvcRequestBuilders.get("/favouriteTrips/get")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'id':1}]"));
    }

    @Test
    public void testSaveToFavourite() throws Exception {
        when(favoriteTripService.saveToFavourite(anyLong())).thenReturn(ResponseEntity.ok("Trip saved to favourites"));

        mockMvc.perform(MockMvcRequestBuilders.put("/favouriteTrips/save")
                        .param("tripId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Trip saved to favourites"));
    }

    @Test
    public void testDeleteFromFavourite() throws Exception {
        when(favoriteTripService.deleteFromFavourite(anyLong())).thenReturn(ResponseEntity.ok("Trip removed from favourites"));

        mockMvc.perform(MockMvcRequestBuilders.delete("/favouriteTrips/delete")
                        .param("tripId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Trip removed from favourites"));
    }

    @Test
    public void testDragAndDrop() throws Exception {
        when(favoriteTripService.dragAndDrop(anyLong(), anyLong())).thenReturn(ResponseEntity.ok("Trip moved"));

        mockMvc.perform(MockMvcRequestBuilders.put("/favouriteTrips/dragAndDrop")
                        .param("tripId", "1")
                        .param("newPosition", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Trip moved"));
    }
}
