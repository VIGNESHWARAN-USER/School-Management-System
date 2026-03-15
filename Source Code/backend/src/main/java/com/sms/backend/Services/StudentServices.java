package com.sms.backend.Services;

import com.sms.backend.Entities.Student;
import com.sms.backend.Repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServices {

    @Autowired
    StudentRepository studentRepositories;

    public void addStudent(Student student)
    {

        studentRepositories.save(student);
    }
}
