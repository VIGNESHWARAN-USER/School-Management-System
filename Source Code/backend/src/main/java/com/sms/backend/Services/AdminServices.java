package com.sms.backend.Services;

import com.sms.backend.Entities.Administrator;
import com.sms.backend.Entities.Student;
import com.sms.backend.Entities.Teacher;
import com.sms.backend.Entities.Parent;
import com.sms.backend.Repositories.AdministratorRepository;
import com.sms.backend.Repositories.StudentRepository;
import com.sms.backend.Repositories.TeacherRepository;
import com.sms.backend.Repositories.ParentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServices {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    ParentRepository parentRepository;
    @Autowired
    private AdministratorRepository administratorRepository;


    // Add Student
    public String addStudent(Student student)
    {
        studentRepository.save(student);
        return "Student Added Successfully";
    }


    // Add Teacher
    public String addTeacher(Teacher teacher)
    {
        teacherRepository.save(teacher);
        return "Teacher Added Successfully";
    }


    // Add Parent
    public String addParent(Parent parent)
    {
        parentRepository.save(parent);
        return "Parent Added Successfully";
    }

    public String addAdmin(Administrator admin) {
        administratorRepository.save(admin);
        return "Admin addedd successfully";
    }
}