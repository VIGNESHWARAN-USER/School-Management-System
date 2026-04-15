package com.sms.service;

import java.util.List;

import com.sms.dao.ResourceDAO;
import com.sms.entities.ClassRoom;
import com.sms.entities.Subject;

public class ResourceService {

    private final ResourceDAO resourceDAO = new ResourceDAO();

    public String addClassRoom(ClassRoom classRoom) {
        boolean success = resourceDAO.addClassRoom(classRoom);
        return success ? "ClassRoom added successfully!" : "Failed to add ClassRoom.";
    }

    public String deleteClassRoom(long id) {
        boolean success = resourceDAO.deleteClassRoom(id);
        return success ? "ClassRoom deleted successfully!" : "Failed to delete ClassRoom or not found.";
    }

    public List<ClassRoom> getAllClassRooms() {
        return resourceDAO.getAllClassRooms();
    }

    public String addSubject(Subject subject) {
        if (!resourceDAO.isClassRoomExists(subject.getClassId())) {
            return "Cannot add Subject: Parent ClassRoom (ID: " + subject.getClassId() + ") does not exist.";
        }

        boolean success = resourceDAO.addSubject(subject);
        return success ? "Subject added successfully!" : "Failed to add Subject.";
    }

    public String deleteSubject(long subjectId) {
        boolean success = resourceDAO.deleteSubject(subjectId);
        return success ? "Subject deleted successfully!" : "Failed to delete Subject or not found.";
    }

    public List<Subject> getAllSubjects() {
        return resourceDAO.getAllSubjects();
    }
}
