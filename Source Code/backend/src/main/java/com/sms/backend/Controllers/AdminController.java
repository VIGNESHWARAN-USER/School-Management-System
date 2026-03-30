package com.sms.backend.Controllers;

import com.sms.backend.DTO.CreateParentDTO;
import com.sms.backend.DTO.StudentDTO;
import com.sms.backend.DTO.TeacherDTO;
import com.sms.backend.Entities.Administrator;
import com.sms.backend.Entities.ClassRoom;
import com.sms.backend.Services.AdminServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AdminController {

    @Autowired
    AdminServices adminService;



    @PostMapping("/addStudent")
    public String addStudent(@RequestBody StudentDTO student)
    {
        return adminService.addStudent(student);
    }


    // Add Teacher
    @PostMapping("/addTeacher")
    public String addTeacher(@RequestBody TeacherDTO teacher)
    {
        return adminService.addTeacher(teacher);
    }

    // Add Parent
    @PostMapping("/addParent")
    public String addParent(@RequestBody CreateParentDTO parent)
    {
        return adminService.addParent(parent);
    }

    @PostMapping("/addAdmin")
    public String addParent(@RequestBody Administrator admin)
    {
        return adminService.addAdmin(admin);
    }

    @DeleteMapping("/deleteStudent/{id}")
    public String deleteStudent(@PathVariable Long id)
    {
        return adminService.deleteStudent(id);
    }


    // Add Teacher
    @DeleteMapping("/deleteTeacher/{id}")
    public String deleteTeacher(@PathVariable Long id)
    {
        return adminService.deleteTeacher(id);
    }

    // Add Parent
    @DeleteMapping("/deleteParent/{id}")
    public String deleteParent(@PathVariable Long id)
    {
        return adminService.deleteParent(id);
    }

    @GetMapping("/fetchAllStudents")
    public ResponseEntity<?> fetchAllStudents()
    {
        return adminService.fetchAllStudents();
    }

    @GetMapping("/fetchAllStudents/{classId}")
    public ResponseEntity<?> fetchAllStudents(@PathVariable String classId)
    {
        return adminService.fetchAllStudentsByClassId(classId);
    }

    @GetMapping("/fetchAllParents")
    public ResponseEntity<?> fetchAllParents()
    {
        return adminService.fetchAllParents();
    }

    @GetMapping("/fetchAllTeachers")
    public ResponseEntity<?> fetchAllTeachers()
    {
        return adminService.fetchAllTeachers();
    }

//    @GetMapping("/classrooms/add")
//    public ResponseEntity<?> addClassRoom(@RequestBody ClassRoom classRoom)
//    {
//        return adminService.addClassRoom(classRoom);
//    }


}