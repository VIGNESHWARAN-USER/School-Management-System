package com.sms.controller;
 // Author : Shobana V
/*
 *  This Controller is to handle all the event functionalities
 *  It consists of adding, updating, deleting and viewing events and participants
 */
import java.util.List;
import java.util.Scanner;

import com.sms.dao.EventDAO;
import com.sms.entities.Event;
import com.sms.entities.User;
import com.sms.util.AppScanner;
import com.sms.util.InputReader;

public class EventController {

	// Getting scanner object
    private final InputReader sc = InputReader.get();
    
    // Creating DAO object
    private final EventDAO eventDAO = new EventDAO();

    // Display all events
    public void showAllEvents() {
    	
    	// Collection used
        List<Event> events = eventDAO.getAllEvents();
        if (events.isEmpty()) {
            System.out.println("No events found.");
            return;
        }
        System.out.println("\n--- Events ---");
        
        // Formatting output in table format
        System.out.printf("%-5s | %-25s | %-12s | %-8s | %-20s | %-15s | %-5s/%-5s | %-10s%n",
            "ID", "Name", "Date", "Time", "Location", "Organizer", "Enr", "Max", "Status");
        System.out.println("-".repeat(115));
        for (Event e : events) {
            System.out.printf("%-5d | %-25s | %-12s | %-8s | %-20s | %-15s | %-5d/%-5d | %-10s%n",
                e.getId(), e.getEventName(), e.getEventDate(), e.getEventTime(),
                e.getEventLocation(), e.getOrganizer(),
                e.getCurrentParticipants(), e.getMaxParticipants(), e.getEventStatus());
        }
    }

    // Display Participants
    public void showParticipants() {
        showAllEvents();
        System.out.print("Enter Event ID to see participants: ");
        
        // Exception handling
        try {
        	
        	// Conversion method
            long eventId = Long.parseLong(sc.readLine().trim());
            System.out.println(eventId);
            Event event = eventDAO.getEventById(eventId);
            if (event == null) {
                System.out.println("Event not found.");
                return;
            }
            
            // Collections used
            List<Long> participants = eventDAO.getParticipants(eventId);
            System.out.println("\n--- Participants for: " + event.getEventName() + " ---");
            if (participants.isEmpty()) {
                System.out.println("No participants yet.");
            } else {
            	
                // Enrich with names via MembersService for display
                com.sms.service.MembersService membersService = new com.sms.service.MembersService();
                List<User> allMembers = membersService.getAllMembers(); // Collections used
                System.out.printf("%-8s | %-20s | %-25s%n", "Stud ID", "Name", "Email"); // Formatting output in table format
                System.out.println("-".repeat(60));
                for (Long pid : participants) {
                    User u = allMembers.stream().filter(m -> m.getId() == pid).findFirst().orElse(null);
                    if (u != null) {
                        System.out.printf("%-8d | %-20s | %-25s%n", u.getId(), u.getName(), u.getEmail());
                    } else {
                        System.out.printf("%-8d | %-20s%n", pid, "(unknown)");
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    // Add a new event
    public void addEvent() {
        System.out.println("\n--- Add New Event ---");
        try {
            System.out.print("Event Name: ");
            String name = sc.readLine().trim();
            System.out.println(name);
            System.out.print("Description: ");
            String desc = sc.readLine().trim();
            System.out.println(desc);
            System.out.print("Date (YYYY-MM-DD): ");
            String date = sc.readLine().trim();
            System.out.println(date);
            System.out.print("Time (HH:MM): ");
            String time = sc.readLine().trim();
            System.out.println(time);

            System.out.print("Location: ");
            String loc = sc.readLine().trim();
            System.out.println(loc);
            System.out.print("Organizer: ");
            String org = sc.readLine().trim();
            System.out.println(org);
            System.out.print("Max Participants: ");
            int max = Integer.parseInt(sc.readLine().trim());
            System.out.println(max);
            System.out.print("Status (UPCOMING/ONGOING/COMPLETED): ");
            String status = sc.readLine().trim().toUpperCase();
            System.out.println(status);
            Event event = new Event(0L, name, desc, date, time, loc, org, max, 0, status);
            if (eventDAO.addEvent(event)) {
                System.out.println("Event added successfully!");
            } else {
                System.out.println("Failed to add event.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input!");
        }
    }

    // Edit a event
    public void editEvent() {
        showAllEvents();
        System.out.print("Enter Event ID to edit: ");
        try {
        	// Conversion method
            long id = Long.parseLong(sc.readLine().trim());
            System.out.println(id);
            Event event = eventDAO.getEventById(id);
            if (event == null) {
                System.out.println("Event not found.");
                return;
            }
            System.out.println("Press Enter to keep existing value.");

            // String handling
            System.out.print("Event Name [" + event.getEventName() + "]: ");
            String v = sc.readLine();
            System.out.println(v);
            if (!v.trim().isEmpty()) event.setEventName(v.trim());
            System.out.print("Description [" + event.getEventDescription() + "]: ");
            v = sc.readLine(); 
            System.out.println(v);
            if (!v.trim().isEmpty()) event.setEventDescription(v.trim());

            System.out.print("Date [" + event.getEventDate() + "]: ");
            v = sc.readLine(); 
            System.out.println(v);
            if (!v.trim().isEmpty()) event.setEventDate(v.trim());

            System.out.print("Time [" + event.getEventTime() + "]: ");
            v = sc.readLine();
            System.out.println(v);
            if (!v.trim().isEmpty()) event.setEventTime(v.trim());

            System.out.print("Location [" + event.getEventLocation() + "]: ");
            v = sc.readLine(); 
            System.out.println(v);
            if (!v.trim().isEmpty()) event.setEventLocation(v.trim());

            System.out.print("Organizer [" + event.getOrganizer() + "]: ");
            v = sc.readLine();
            System.out.println(v);
            if (!v.trim().isEmpty()) event.setOrganizer(v.trim());

            System.out.print("Max Participants [" + event.getMaxParticipants() + "]: ");
            v = sc.readLine(); 
            System.out.println(v);
            if (!v.trim().isEmpty()) event.setMaxParticipants(Integer.parseInt(v.trim()));

            System.out.print("Status [" + event.getEventStatus() + "]: ");
            v = sc.readLine(); 
            System.out.println(v);
            if (!v.trim().isEmpty()) event.setEventStatus(v.trim().toUpperCase());

            System.out.println(eventDAO.updateEvent(event) ? "Event updated!" : "Update failed.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    // Delete a event
    public void deleteEvent() {
        showAllEvents();
        System.out.print("Enter Event ID to delete: ");
        try {
            long id = Long.parseLong(sc.readLine().trim());
            System.out.println(id);
            System.out.println(eventDAO.deleteEvent(id) ? "Event deleted!" : "Deletion failed.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    // Menu for Student Event
    public void studentEventsMenu(long studentId) {
        while (true) {
            System.out.println("\n  --- Events ---");
            System.out.println("  1. Browse Events");
            System.out.println("  2. Register for an Event");
            System.out.println("  3. My Registrations");
            System.out.println("  4. Back");
            System.out.print("  Enter choice: ");
            int c;
            
            // Exception handling
            try { 
            	
            	// Conversion method
            	c = Integer.parseInt(sc.readLine().trim()); 
            	System.out.println(c);
            	}
            catch (Exception e) { System.out.println("Invalid."); continue; }

            if (c == 1) {
                showAllEvents();
            } else if (c == 2) {
                showAllEvents();
                System.out.print("Enter Event ID to register: ");
                try {
                	
                	// Conversion method
                    long eventId = Long.parseLong(sc.readLine().trim());
                    System.out.println(eventId);
                    if (eventDAO.isRegistered(eventId, studentId)) {
                        System.out.println("You are already registered for this event.");
                    } else {
                        System.out.println(eventDAO.registerStudent(eventId, studentId)
                            ? "Successfully registered!" : "Registration failed.");
                    }
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid ID.");
                }
            } else if (c == 3) {
            	
            	// Collection used
                List<Event> myEvents = eventDAO.getEventsByStudent(studentId);
                if (myEvents.isEmpty()) {
                    System.out.println("You have not registered for any events.");
                } else {
                    System.out.println("\n--- Your Registered Events ---");
                    
                    // Formatting output in table format
                    System.out.printf("%-5s | %-25s | %-12s | %-20s | %-10s%n", "ID", "Name", "Date", "Location", "Status");
                    System.out.println("-".repeat(80));
                    for (Event e : myEvents) {
                        System.out.printf("%-5d | %-25s | %-12s | %-20s | %-10s%n",
                            e.getId(), e.getEventName(), e.getEventDate(), e.getEventLocation(), e.getEventStatus());
                    }
                }
            } else if (c == 4) {
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    // Teacher, Admin and Parent can view the events
    public void viewerEventsMenu(String role, Long parentChildId) {
        while (true) {
            System.out.println("\n  --- Events ---");
            System.out.println("  1. View All Events");
            System.out.println("  2. View Participants of an Event");
            if ("PARENT".equalsIgnoreCase(role) && parentChildId != null) {
                System.out.println("  3. View Events My Child Participated In");
            }
            System.out.println("  0. Back");
            System.out.print("  Enter choice: ");
            int c;
            
            // Exception handling
            try { c = Integer.parseInt(sc.readLine().trim());
            System.out.println(c);
            }
            catch (Exception e) { System.out.println("Invalid."); continue; }

            if (c == 1) {
                showAllEvents();
            } else if (c == 2) {
                showParticipants();
            } else if (c == 3 && "PARENT".equalsIgnoreCase(role) && parentChildId != null) { // String handling
            	
            	// Collection used
                List<Event> events = eventDAO.getEventsByStudent(parentChildId);
                System.out.println("\n--- Events Your Child (ID: " + parentChildId + ") Participated In ---");
                if (events.isEmpty()) {
                    System.out.println("No events found.");
                } else {
                	
                	// Formatting output in table format
                    System.out.printf("%-5s | %-25s | %-12s | %-20s | %-10s%n", "ID", "Name", "Date", "Location", "Status");
                    System.out.println("-".repeat(80));
                    for (Event e : events) {
                        System.out.printf("%-5d | %-25s | %-12s | %-20s | %-10s%n",
                            e.getId(), e.getEventName(), e.getEventDate(), e.getEventLocation(), e.getEventStatus()); // Formatting output in table format
                    }
                }
            } else if (c == 0) {
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }
}
