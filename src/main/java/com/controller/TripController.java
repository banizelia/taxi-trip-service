package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.model.Trip;
import com.service.TripService;

@RestController
public class TripController {
    private final TripService tripService;

    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

//    @RequestMapping(method = RequestMethod.GET, value = "/test")
//    @ResponseBody
//    public ResponseEntity<String> test() {
//        return new ResponseEntity<String>("test", HttpStatus.OK);
//    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}