package com.web.controller;

import com.web.model.Trip;
import com.web.service.TripService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.ByteArrayInputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TripController.class)
public class TripControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TripService tripService;

    private Trip trip;

    @BeforeEach
    public void setUp() {
        trip = new Trip();
        trip.setId(1L);
    }

    @Test
    public void testFilter() throws Exception {
        when(tripService.filter(any(Timestamp.class), any(Timestamp.class), anyDouble(), anyDouble(), anyString(), anyString(), anyInt()))
                .thenReturn(Collections.singletonList(trip));

        mockMvc.perform(MockMvcRequestBuilders.get("/trips/filter")
                        .param("startDateTime", "2016-01-01 00:00:00")
                        .param("endDateTime", "2016-01-31 23:59:59")
                        .param("minWindSpeed", "0")
                        .param("maxWindSpeed", "9999")
                        .param("direction", "asc")
                        .param("sortBy", "id")
                        .param("page", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'id':1}]"));
    }

    @Test
    public void testDownload() throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Mock Excel Content".getBytes());
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);

        when(tripService.download()).thenReturn(inputStream);

        mockMvc.perform(MockMvcRequestBuilders.get("/trips/download")
                        .accept(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=trips.xlsx"))
                .andExpect(content().contentType("application/vnd.ms-excel"));
    }
}
