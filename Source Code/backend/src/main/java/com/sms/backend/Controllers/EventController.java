package com.sms.backend.Controllers;

import com.sms.backend.Entities.Event;
import com.sms.backend.Entities.Registration;
import com.sms.backend.Services.EventServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    EventServices eventServices;

    //  Add Event
    @PostMapping("/add")
    public String addEvent(@RequestBody Event event) {
        return eventServices.addEvent(event);
    }

    //  Update Event
    @PutMapping("/update")
    public String updateEvent(@RequestParam Long eventId,
                              @RequestBody Event event) {
        return eventServices.updateEvent(eventId, event);
    }

    //  Delete Event
    @DeleteMapping("/delete")
    public String deleteEvent(@RequestParam Long eventId) {
        return eventServices.deleteEvent(eventId);
    }

    //  Participate
    @PostMapping("/participate")
    public String participate(@RequestBody Registration registration) {
        return eventServices.participateEvent(registration);
    }

    // Get All Events
    @GetMapping("/all")
    public List<Event> getAllEvents() {
        return eventServices.getAllEvents();
    }

    // Get Participation (Student/Teacher specific)
    @GetMapping("/participation")
    public List<Registration> getParticipation(@RequestParam Long participantId) {
        return eventServices.getParticipation(participantId);
    }
}