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
    @GetMapping("getData")
    public Map<String, Object> getDashboard(
            @RequestParam String userRole,
            @RequestParam(required = false) Long userId)
    {
        System.out.println(userRole+" "+userId);
        return dashboardService.getDashboard(userRole, userId);
    }
}