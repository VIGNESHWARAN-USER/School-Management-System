package com.sms.backend.Controllers;

import com.sms.backend.Services.MainServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")

public class MainController {

    @Autowired
    MainServices mainServices;

    @RequestMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> data){
            String email = data.get("email");
            String password = data.get("password");
            String role = data.get("role");
           return mainServices.login(email,password,role);
    }

    @RequestMapping("getDetails")
    public ResponseEntity<?> getDetails(@RequestBody Map<String, String> data)
    {
        String email = data.get("email");
        String role = data.get("role");
        String studentId = data.get("studentId");
        return mainServices.getDetails(email, role, studentId);
    }

    @RequestMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> data)
    {
        String email = data.get("email");
        String role = data.get("role");
        return mainServices.forgotPassword(email, role);
    }

    @RequestMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> data)
    {
        String email = data.get("email");
        String password = data.get("password");
        String role = data.get("role");
        return mainServices.resetPassword(email, password, role);
    }
}
