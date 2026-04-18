package com.sms.service;

// Author : Vigneshwaran M 
// This class handles classroom and subject related operations

import java.util.List;

import com.sms.dao.ResourceDAO;
import com.sms.entities.ClassRoom;
import com.sms.entities.Subject;

public class ResourceService {

    // Creating DAO object 
    private final ResourceDAO resourceDAO = new ResourceDAO();

    // Add a new classroom
    public String addClassRoom(ClassRoom classRoom) {
        boolean success = resourceDAO.addClassRoom(classRoom);
        return success ? "ClassRoom added successfully!" : "Failed to add ClassRoom.";
    }

    // Delete classroom by id
    public String deleteClassRoom(long id) {
        boolean success = resourceDAO.deleteClassRoom(id);
        return success ? "ClassRoom deleted successfully!" : "Failed to delete ClassRoom or not found.";
    }

    // Get all classrooms
    public List<ClassRoom> getAllClassRooms() {
        return resourceDAO.getAllClassRooms(); // Collections used
    }

    // Add a new subject
    public String addSubject(Subject subject) {

        // Validation to check if classroom exists before adding subject
        if (!resourceDAO.isClassRoomExists(subject.getClassId())) {
            return "Cannot add Subject: Parent ClassRoom (ID: " + subject.getClassId() + ") does not exist.";
        }

        boolean success = resourceDAO.addSubject(subject);
        return success ? "Subject added successfully!" : "Failed to add Subject.";
    }

    // Delete subject by id
    public String deleteSubject(long subjectId) {
        boolean success = resourceDAO.deleteSubject(subjectId);
        return success ? "Subject deleted successfully!" : "Failed to delete Subject or not found.";
    }

    // Get all subjects
    public List<Subject> getAllSubjects() {
        return resourceDAO.getAllSubjects(); // Collections used
    }
}