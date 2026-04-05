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
import java.util.List;

@Service
public class EventServices {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EmailService emailService;

    //  1. ADD EVENT + EMAIL
    public ResponseEntity<?> addEvent(EventDTO eventdto) {
        try {
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

            //  EMAIL → Student + Parent + Teacher
            List<Student> students = studentRepository.findAll();

            for (Student student : students) {

                String subject = "New Event Scheduled";
                String body = "<h3>New Event</h3>" +
                        "<p>Event: " + event.getEventName() + "</p>" +
                        "<p>Date: " + event.getEventDate() + "</p>" +
                        "<p>Location: " + event.getEventLocation() + "</p>";

                try {
                    emailService.sendHtmlEmail(student.getEmail(), subject, body);
                    emailService.sendHtmlEmail(student.getParentEmail(), subject, body);
                    emailService.sendHtmlEmail("teacher@gmail.com", subject, body);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return ResponseEntity.ok("Event Added & Notifications Sent");

        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    // 2. UPDATE EVENT
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

        return ResponseEntity.ok("Event Updated Successfully");
    }

    //  3. DELETE EVENT
    public String deleteEvent(Long eventId) {

        if (!eventRepository.existsById(eventId)) {
            return "Event Not Found";
        }

        eventRepository.deleteById(eventId);
        return "Event Deleted Successfully";
    }

    //  4. PARTICIPATE EVENT + EMAIL
    public ResponseEntity<?> participateEvent(RegistrationDTO dto) {
        try {

            Event event = eventRepository.findById(dto.getEventId()).orElse(null);

            if (event == null) {
                return ResponseEntity.notFound().build();
            }

            // LIMIT CHECK
            if (event.getCurrentParticipants() >= event.getMaxParticipants()) {
                return ResponseEntity.status(403).body("Event Full - Cannot Register");
            }

            // DUPLICATE CHECK (NULL SAFE)
            if (event.getRegistrationList() != null) {
                for (Registration reg : event.getRegistrationList()) {
                    if (reg.getStudent().getId().equals(dto.getStudentId())) {
                        return ResponseEntity.status(403).body("Participation already added");
                    }
                }
            }

            Student student = studentRepository.findById(dto.getStudentId()).orElse(null);

            if (student == null) {
                return ResponseEntity.status(404).body("Student Not Found");
            }

            Registration registration = new Registration();
            registration.setRegistrationDate(String.valueOf(LocalDate.now()));
            registration.setEvent(event);
            registration.setStudent(student);

            // Increase count
            event.setCurrentParticipants(event.getCurrentParticipants() + 1);

            eventRepository.save(event);
            registrationRepository.save(registration);

            //  EMAIL → Student + Parent + Admin
            String subject = "Event Registration Successful";

            String body = "<h3>Registered Successfully</h3>" +
                    "<p>Event: " + event.getEventName() + "</p>" +
                    "<p>Student: " + student.getName() + "</p>";

            try {
                // Student
                emailService.sendHtmlEmail(student.getEmail(), subject, body);

                // Parent
                emailService.sendHtmlEmail(student.getParentEmail(), subject, body);

                // Admin (with count)
                String adminBody = body +
                        "<p>Total Participants: " + event.getCurrentParticipants() + "</p>";

                emailService.sendHtmlEmail("admin@gmail.com", subject, adminBody);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return ResponseEntity.ok("Participation Successfully added & Notifications Sent");

        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    //  5. GET ALL EVENTS
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

            if (event.getRegistrationList() != null) {
                List<RegistrationDTO> registrationDTOS = event.getRegistrationList().stream().map(registration -> {
                    RegistrationDTO registrationDTO = new RegistrationDTO();

                    Student student = registration.getStudent();

                    registrationDTO.setAge(String.valueOf(student.getAge()));
                    registrationDTO.setStudentId(student.getId());
                    registrationDTO.setName(student.getName());
                    registrationDTO.setClassId(student.getClassRoom().getClassName());
                    registrationDTO.setEmail(student.getEmail());
                    registrationDTO.setId(registration.getId());
                    registrationDTO.setEventId(registration.getEvent().getId());

                    return registrationDTO;
                }).toList();

                eventDTO.setRegistrationDTOS(registrationDTOS);
            }

            return eventDTO;
        }).toList();

        return ResponseEntity.ok(eventDTOS);
    }
}