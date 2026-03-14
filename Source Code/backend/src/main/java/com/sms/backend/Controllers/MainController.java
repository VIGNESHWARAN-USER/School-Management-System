package com.sms.backend.Controllers;

import com.sms.backend.Services.MainServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api")

public class MainController {
    @Autowired
    MainServices mainServices;
    @RequestMapping("/login")
    public String login(@RequestBody String email,@RequestBody String password,@RequestBody String role){
           return mainServices.login(email,password,role);
    }
}
