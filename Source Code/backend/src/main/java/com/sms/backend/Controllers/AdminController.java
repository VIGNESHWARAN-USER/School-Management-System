package com.sms.backend.Controllers;

import com.sms.backend.Entities.Student;
import com.sms.backend.Entities.Teacher;
import com.sms.backend.Entities.Parent;
import com.sms.backend.Services.AdminServices;

import com.sms.backend.Services.AdminServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminServices adminService;


    // Add Student
    @PostMapping("/addStudent")
    public String addStudent(@RequestBody Student student)
    {
        return adminService.addStudent(student);
    }


    // Add Teacher
    @PostMapping("/addTeacher")
    public String addTeacher(@RequestBody Teacher teacher)
    {
        return adminService.addTeacher(teacher);
    }


    // Add Parent
    @PostMapping("/addParent")
    public String addParent(@RequestBody Parent parent)
    {
        return adminService.addParent(parent);
    }

}