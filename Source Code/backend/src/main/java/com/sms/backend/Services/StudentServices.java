package com.sms.backend.Services;

import com.sms.backend.Entities.Student;
import com.sms.backend.Repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
public class StudentServices {

    @Autowired
    StudentRepository studentRepositories;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public void addStudent(Student student)
    {
        student.setPassword(passwordEncoder.encode("1234"));
        studentRepositories.save(student);
    }
}
