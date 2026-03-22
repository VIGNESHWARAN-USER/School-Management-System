package com.sms.backend.Controllers;

import com.sms.backend.Services.DashboardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    DashboardService dashboardService;


    // ONE API FOR ALL ROLES
    @GetMapping
    public Map<String, Object> getDashboard(
            @RequestParam String role,
            @RequestParam(required = false) Long userId)
    {
        return dashboardService.getDashboard(role, userId);
    }
}