package com.sms.backend.Controllers;

import com.sms.backend.Entities.Student;
import com.sms.backend.Services.StudentServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MainController {


    @Autowired
    StudentServices studentServices;

    @RequestMapping("test")
    public String checkAPI()
    {
        return "Its Working...";
    }



    @RequestMapping("addStudent")
    public String addStudent(@RequestBody Student student)
    {
        System.out.println(student.getAge());
        studentServices.addStudent(student);

        return "Student added sucessfully..";
    }
}
