package com.sms.backend.Services;

import com.sms.backend.Entities.Student;
import com.sms.backend.Repositories.StudentRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServices {

    @Autowired
    StudentRepositories studentRepositories;

    public void addStudent(Student student)
    {

        studentRepositories.save(student);
    }
}
