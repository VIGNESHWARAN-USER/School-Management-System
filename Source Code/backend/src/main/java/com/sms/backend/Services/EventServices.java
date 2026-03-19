package com.sms.backend.Services;

import com.sms.backend.Entities.Event;
import com.sms.backend.Entities.Registration;
import com.sms.backend.Repositories.EventRepository;
import com.sms.backend.Repositories.RegistrationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServices {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    RegistrationRepository registrationRepository;

    //  Add Event
    public String addEvent(Event event) {

        event.setCurrentParticipants(0); // initially zero
        eventRepository.save(event);

        return "Event Added Successfully";
    }

    //  Update Event
    public String updateEvent(Long eventId, Event updatedEvent) {

        Event event = eventRepository.findById(eventId).orElse(null);

        if (event == null) {
            return "Event Not Found";
        }

        event.setEventName(updatedEvent.getEventName());
        event.setEventDescription(updatedEvent.getEventDescription());
        event.setEventDate(updatedEvent.getEventDate());
        event.setEventTime(updatedEvent.getEventTime());
        event.setEventLocation(updatedEvent.getEventLocation());
        event.setMaxParticipants(updatedEvent.getMaxParticipants());

        eventRepository.save(event);

        return "Event Updated Successfully";
    }

    //  Delete Event
    public String deleteEvent(Long eventId) {

        if (!eventRepository.existsById(eventId)) {
            return "Event Not Found";
        }

        eventRepository.deleteById(eventId);
        return "Event Deleted Successfully";
    }

    //  Participate in Event (IMPORTANT LOGIC)
    public String participateEvent(Registration registration) {

        Event event = eventRepository.findById(registration.getEventId()).orElse(null);

        if (event == null) {
            return "Event Not Found";
        }

        //  LIMIT CHECK
        if (event.getCurrentParticipants() >= event.getMaxParticipants()) {
            return "Event Full - Cannot Register";
        }

        // Increase count
        event.setCurrentParticipants(event.getCurrentParticipants() + 1);

        eventRepository.save(event);
        registrationRepository.save(registration);

        return "Participation Successful";
    }

    //  Fetch All Events
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    //  Fetch Participation (specific user)
    public List<Registration> getParticipation(Long participantId) {
        return registrationRepository.findByParticipantId(participantId);
    }
}