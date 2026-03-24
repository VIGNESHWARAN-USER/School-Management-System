package com.sms.backend.Services;

import com.sms.backend.Entities.Notification;
import com.sms.backend.Repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CommunicationNotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    ParentRepository parentRepository;

    @Autowired
    private JavaMailSender mailSender;


    // 🔥 MAIN METHOD (EMAIL BASED)
    public String sendNotification(String type, String title, String message,
                                   String senderEmail, Long targetId)
    {
        switch (type.toLowerCase())
        {
            case "resource":
                sendToStudents(title, message, senderEmail);
                break;

            case "exam":
                sendToStudentsParentsTeachers(title, message, senderEmail);
                break;

            case "event":
                sendToAll(title, message, senderEmail);
                break;

            case "result":
                sendToStudentsParentsTeachers(title, message, senderEmail);
                break;

            case "assignment":
                sendToTeachers(title, message, senderEmail);
                break;

            case "schedule":
                sendToStudentsAndTeachers(title, message, senderEmail);
                break;

            case "attendance":
                sendAbsentToParent(title, message, senderEmail, targetId);
                break;

            case "fee_payment":
                sendToAdmin(title, message, senderEmail);
                break;

            case "fee":
                sendToParents(title, message, senderEmail);
                break;

            case "announcement":
                sendToAll(title, message, senderEmail);
                break;

            default:
                return "Invalid Type";
        }

        return "Notification Sent Successfully";
    }


    // ================= METHODS =================

    private void sendToStudents(String title, String message, String senderEmail)
    {
        studentRepository.findAll().forEach(s ->
                saveNotification(title, message, senderEmail, s.getEmail()));
    }

    private void sendToTeachers(String title, String message, String senderEmail)
    {
        teacherRepository.findAll().forEach(t ->
                saveNotification(title, message, senderEmail, t.getEmail()));
    }

    private void sendToParents(String title, String message, String senderEmail)
    {
        parentRepository.findAll().forEach(p ->
                saveNotification(title, message, senderEmail, p.getEmail()));
    }

    private void sendToStudentsAndTeachers(String title, String message, String senderEmail)
    {
        sendToStudents(title, message, senderEmail);
        sendToTeachers(title, message, senderEmail);
    }

    private void sendToStudentsParentsTeachers(String title, String message, String senderEmail)
    {
        sendToStudents(title, message, senderEmail);
        sendToTeachers(title, message, senderEmail);
        sendToParents(title, message, senderEmail);
    }

    private void sendToAll(String title, String message, String senderEmail)
    {
        sendToStudentsParentsTeachers(title, message, senderEmail);
    }

    // 🔥 Attendance → Parent
    private void sendAbsentToParent(String title, String message,
                                    String senderEmail, Long studentId)
    {
        String parentEmail = studentRepository.findById(studentId)
                .orElseThrow()
                .getParentEmail(); // ensure field exists

        saveNotification(title, message, senderEmail, parentEmail);
    }

    // 🔥 Fee → Admin
    private void sendToAdmin(String title, String message, String senderEmail)
    {
        String adminEmail = "admin@gmail.com";
        saveNotification(title, message, senderEmail, adminEmail);
    }

    // 🔥 SAVE + EMAIL
    private void saveNotification(String title, String message,
                                  String senderEmail, String receiverEmail)
    {
        Notification n = new Notification();

        n.setTitle(title);
        n.setMessage(message);
        n.setDate(LocalDate.now().toString());
        n.setSenderEmail(senderEmail);
        n.setReceiverEmail(receiverEmail);
        n.setStatus("SENT");

        notificationRepository.save(n);

        sendEmail(receiverEmail, title, message);
    }

    // 🔥 EMAIL METHOD
    private void sendEmail(String email, String subject, String body)
    {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setSubject(subject);
        mail.setText(body);

        mailSender.send(mail);
    }

    // 🔹 FETCH
    public List<Notification> getNotifications(String email)
    {
        return notificationRepository.findByReceiverEmail(email);
    }
}