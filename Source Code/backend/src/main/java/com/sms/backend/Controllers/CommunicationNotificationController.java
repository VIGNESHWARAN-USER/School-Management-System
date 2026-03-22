package com.sms.backend.Controllers;

import com.sms.backend.Entities.Notification;
import com.sms.backend.Services.CommunicationNotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
public class CommunicationNotificationController {

    @Autowired
    CommunicationNotificationService service;


    // 🔥 SEND NOTIFICATION
    @PostMapping("/send")
    public String sendNotification(
            @RequestParam String type,
            @RequestParam String title,
            @RequestParam String message,
            @RequestParam String senderEmail,
            @RequestParam(required = false) Long targetId)
    {
        return service.sendNotification(type, title, message, senderEmail, targetId);
    }


    // 🔥 GET NOTIFICATIONS (EMAIL BASED)
    @GetMapping("/{email}")
    public List<Notification> getNotifications(@PathVariable String email)
    {
        return service.getNotifications(email);
    }
}