package com.sms.backend.Controllers;

import com.sms.backend.DTO.EventDTO;
import com.sms.backend.DTO.RegistrationDTO;
import com.sms.backend.Services.EventServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    EventServices eventServices;

    //  Add Event
    @PostMapping("/add")
    public ResponseEntity<?> addEvent(@RequestBody EventDTO event) {
        return eventServices.addEvent(event);
    }

    //  Update Event
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @RequestBody EventDTO event) {
        return eventServices.updateEvent(id, event);
    }

    //  Delete Event
    @DeleteMapping("/delete/{eventId}")
    public String deleteEvent(@PathVariable Long eventId) {
        return eventServices.deleteEvent(eventId);
    }

    //  Participate
    @PostMapping("/participate")
    public ResponseEntity<?> participate(@RequestBody RegistrationDTO registration) {
        return eventServices.participateEvent(registration);
    }

    // Get All Events
    @GetMapping("/all")
    public ResponseEntity<?> getAllEvents() {
        return eventServices.getAllEvents();
    }
}