package com.sms.backend.Services;

import com.sms.backend.DTO.EventDTO;
import com.sms.backend.DTO.RegistrationDTO;
import com.sms.backend.Entities.Event;
import com.sms.backend.Entities.Registration;
import com.sms.backend.Entities.Student;
import com.sms.backend.Repositories.EventRepository;
import com.sms.backend.Repositories.RegistrationRepository;

import com.sms.backend.Repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class EventServices {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    RegistrationRepository registrationRepository;
    @Autowired
    private StudentRepository studentRepository;

    //  Add Event
    public ResponseEntity<?> addEvent(EventDTO eventdto) {
        try
        {
            Event event = new Event();

            event.setEventStatus(eventdto.getEventStatus());
            event.setEventName(eventdto.getEventName());
            event.setEventDate(eventdto.getEventDate());
            event.setEventLocation(eventdto.getEventLocation());
            event.setEventDescription(eventdto.getEventDescription());
            event.setOrganizer(eventdto.getOrganizer());
            event.setMaxParticipants(eventdto.getMaxParticipants());
            event.setEventTime(eventdto.getEventTime());

            event.setCurrentParticipants(0);
            eventRepository.save(event);

            return ResponseEntity.ok().body("Event Added Successfully");
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    //  Update Event
    public ResponseEntity<?> updateEvent(Long eventId, EventDTO updatedEvent) {

        Event event = eventRepository.findById(eventId).orElse(null);

        if (event == null) {
            return ResponseEntity.notFound().build();
        }

        event.setEventName(updatedEvent.getEventName());
        event.setEventDescription(updatedEvent.getEventDescription());
        event.setEventDate(updatedEvent.getEventDate());
        event.setEventTime(updatedEvent.getEventTime());
        event.setEventLocation(updatedEvent.getEventLocation());
        event.setMaxParticipants(updatedEvent.getMaxParticipants());

        eventRepository.save(event);

        return ResponseEntity.ok().body("Event Updated Successfully");
    }

    //  Delete Event
    public String deleteEvent(Long eventId) {

        if (!eventRepository.existsById(eventId)) {
            return "Event Not Found";
        }

        eventRepository.deleteById(eventId);
        return "Event Deleted Successfully";
    }


    public ResponseEntity<?> participateEvent(RegistrationDTO dto) {
        try{
            Event event = eventRepository.findById(dto.getEventId()).orElse(null);

            Registration registration = new Registration();

            registration.setRegistrationDate(String.valueOf(LocalDate.now()));
            registration.setEvent(eventRepository.findById(dto.getEventId()).orElse(null));
            registration.setStudent(studentRepository.findById(dto.getStudentId()).orElse(null));

            if (event == null) {
                return ResponseEntity.notFound().build();
            }

            //  LIMIT CHECK
            if (event.getCurrentParticipants() >= event.getMaxParticipants()) {
                return ResponseEntity.status(403).body("Event Full - Cannot Register");
            }

            // Increase count
            event.setCurrentParticipants(event.getCurrentParticipants() + 1);

            eventRepository.save(event);
            registrationRepository.save(registration);

            return ResponseEntity.ok().body("Participation Successfully added");
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    //  Fetch All Events
    public ResponseEntity<?> getAllEvents() {
        List<Event> events = eventRepository.findAll();

        List<EventDTO> eventDTOS = events.stream().map(event -> {
            EventDTO eventDTO = new EventDTO();

            eventDTO.setEventStatus(event.getEventStatus());
            eventDTO.setEventName(event.getEventName());
            eventDTO.setEventDate(event.getEventDate());
            eventDTO.setEventLocation(event.getEventLocation());
            eventDTO.setEventDescription(event.getEventDescription());
            eventDTO.setOrganizer(event.getOrganizer());
            eventDTO.setMaxParticipants(event.getMaxParticipants());
            eventDTO.setEventTime(event.getEventTime());
            eventDTO.setId(event.getId());

            if(event.getRegistrationList() != null) {
                List<RegistrationDTO> registrationDTOS = event.getRegistrationList().stream().map(registration -> {
                    RegistrationDTO registrationDTO = new RegistrationDTO();

                    Student student = registration.getStudent();

                    registrationDTO.setAge(student.getAge());
                    registrationDTO.setStudentId(student.getId());
                    registrationDTO.setName(student.getName());
                    registrationDTO.setClassId(student.getClassId());
                    registrationDTO.setEmail(student.getEmail());
                    registrationDTO.setId(registration.getId());
                    registrationDTO.setEventId(registration.getEvent().getId());
                    return registrationDTO;
                }).toList();
                eventDTO.setRegistrationDTOS(registrationDTOS);
            }
            return eventDTO;
        }).toList();

        return ResponseEntity.ok().body(eventDTOS);
    }


}