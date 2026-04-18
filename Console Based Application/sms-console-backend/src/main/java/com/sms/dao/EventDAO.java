package com.sms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.sms.entities.Event;
import com.sms.util.DatabaseConfig;

public class EventDAO {

    // Convert database row into Event object
    private Event mapEvent(ResultSet rs) throws Exception {
        return new Event(
            rs.getLong("id"),
            rs.getString("event_name"),
            rs.getString("event_description"),
            rs.getString("event_date"),
            rs.getString("event_time"),
            rs.getString("event_location"),
            rs.getString("organizer"),
            rs.getInt("max_participants"),
            rs.getInt("current_participants"),
            rs.getString("event_status")
        );
    }

    // Add new event to database
    public boolean addEvent(Event event) {
        try {
            Connection con = DatabaseConfig.getConnection(); // get connection

            String query = "INSERT INTO events (event_name, event_description, event_date, event_time, event_location, organizer, max_participants, current_participants, event_status) VALUES (?, ?, ?, ?, ?, ?, ?, 0, ?)";
            PreparedStatement ps = con.prepareStatement(query);

            // set values
            ps.setString(1, event.getEventName());
            ps.setString(2, event.getEventDescription());
            ps.setString(3, event.getEventDate());
            ps.setString(4, event.getEventTime());
            ps.setString(5, event.getEventLocation());
            ps.setString(6, event.getOrganizer());
            ps.setInt(7, event.getMaxParticipants());
            ps.setString(8, event.getEventStatus());

            return ps.executeUpdate() > 0; // insert result

        } catch (Exception e) {
            System.out.println("Database Error: " + e.getMessage());
            return false;
        }
    }

    // Update event details
    public boolean updateEvent(Event event) {
        try {
            Connection con = DatabaseConfig.getConnection();

            String query = "UPDATE events SET event_name=?, event_description=?, event_date=?, event_time=?, event_location=?, organizer=?, max_participants=?, event_status=? WHERE id=?";
            PreparedStatement ps = con.prepareStatement(query);

            // update values
            ps.setString(1, event.getEventName());
            ps.setString(2, event.getEventDescription());
            ps.setString(3, event.getEventDate());
            ps.setString(4, event.getEventTime());
            ps.setString(5, event.getEventLocation());
            ps.setString(6, event.getOrganizer());
            ps.setInt(7, event.getMaxParticipants());
            ps.setString(8, event.getEventStatus());
            ps.setLong(9, event.getId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Database Error: " + e.getMessage());
            return false;
        }
    }

    // Delete event by id
    public boolean deleteEvent(long id) {
        try {
            Connection con = DatabaseConfig.getConnection();

            // delete registrations first
            PreparedStatement ps1 = con.prepareStatement("DELETE FROM registrations WHERE event_id = ?");
            ps1.setLong(1, id);
            ps1.executeUpdate();

            // delete event
            PreparedStatement ps = con.prepareStatement("DELETE FROM events WHERE id = ?");
            ps.setLong(1, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Database Error: " + e.getMessage());
            return false;
        }
    }

    // Get all events
    public List<Event> getAllEvents() {
        List<Event> list = new ArrayList<>(); // store events

        try {
            Connection con = DatabaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM events ORDER BY id");
            ResultSet rs = ps.executeQuery();

            // loop and add events
            while (rs.next()) list.add(mapEvent(rs));

        } catch (Exception e) {
            System.out.println("Database Error: " + e.getMessage());
        }

        return list;
    }

    // Get event by id
    public Event getEventById(long id) {
        try {
            Connection con = DatabaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM events WHERE id = ?");
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) return mapEvent(rs); // found event

        } catch (Exception e) {
            System.out.println("Database Error: " + e.getMessage());
        }

        return null;
    }

    // Check if student already registered
    public boolean isRegistered(long eventId, long studentId) {
        try {
            Connection con = DatabaseConfig.getConnection();

            PreparedStatement ps = con.prepareStatement("SELECT 1 FROM registrations WHERE event_id = ? AND student_id = ?");
            ps.setLong(1, eventId);
            ps.setLong(2, studentId);

            return ps.executeQuery().next();

        } catch (Exception e) {
            return false;
        }
    }

    // Register student for event
    public boolean registerStudent(long eventId, long studentId) {
        try {
            Connection con = DatabaseConfig.getConnection();

            // check event capacity
            PreparedStatement psCheck = con.prepareStatement("SELECT max_participants, current_participants FROM events WHERE id = ?");
            psCheck.setLong(1, eventId);
            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                int max = rs.getInt("max_participants");
                int cur = rs.getInt("current_participants");

                if (cur >= max) {
                    System.out.println("Event is full!");
                    return false;
                }
            }

            // insert registration
            PreparedStatement ps = con.prepareStatement("INSERT INTO registrations (event_id, student_id, registration_date) VALUES (?, ?, ?)");
            ps.setLong(1, eventId);
            ps.setLong(2, studentId);
            ps.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));
            ps.executeUpdate();

            // increase participant count
            PreparedStatement psUpd = con.prepareStatement("UPDATE events SET current_participants = current_participants + 1 WHERE id = ?");
            psUpd.setLong(1, eventId);
            psUpd.executeUpdate();

            return true;

        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            System.out.println("Student not found");
            return false;

        } catch (Exception e) {
            System.out.println("Database Error: " + e.getMessage());
            return false;
        }
    }

    // Get all participant ids
    public List<Long> getParticipants(long eventId) {
        List<Long> ids = new ArrayList<>();

        try {
            Connection con = DatabaseConfig.getConnection();

            PreparedStatement ps = con.prepareStatement("SELECT student_id FROM registrations WHERE event_id = ?");
            ps.setLong(1, eventId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) ids.add(rs.getLong("student_id"));

        } catch (Exception e) {
            System.out.println("Database Error: " + e.getMessage());
        }

        return ids;
    }

    // Get events registered by a student
    public List<Event> getEventsByStudent(long studentId) {
        List<Event> list = new ArrayList<>();

        try {
            Connection con = DatabaseConfig.getConnection();

            String query = "SELECT e.* FROM events e JOIN registrations r ON e.id = r.event_id WHERE r.student_id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setLong(1, studentId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) list.add(mapEvent(rs));

        } catch (Exception e) {
            System.out.println("Database Error: " + e.getMessage());
        }

        return list;
    }
}