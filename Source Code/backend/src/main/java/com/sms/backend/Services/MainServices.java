package com.sms.backend.Services;

import com.sms.backend.Entities.Administrator;
import com.sms.backend.Entities.Parent;
import com.sms.backend.Entities.Student;
import com.sms.backend.Entities.Teacher;
import com.sms.backend.Repositories.AdministratorRepository;
import com.sms.backend.Repositories.ParentRepository;
import com.sms.backend.Repositories.StudentRepository;
import com.sms.backend.Repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainServices {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    private ParentRepository parentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private AdministratorRepository administratorRepository;

    public String login(String email, String password, String role) {
    try{
        if (role.equals("Student")) {
            Student student = studentRepository.findByEmail(email);
            if(student.getPassword().equals(password)){
                return "Login Successful";
            }
              else{
                  return "Invalid Password";
            }

        }
        else if (role.equals("Parent")) {
            Parent parent = parentRepository.findByEmail(email);
            if(parent.getPassword().equals(password)){
                return "Login Successful";
            }
            else{
                return "Invalid Password";
            }


        }
        else if (role.equals("Teacher")) {
            Teacher teacher = teacherRepository.findByEmail(email);
            if(teacher.getPassword().equals(password)){
                return "Login Successful";
            }
            else{
                return "Invalid Password";
            }

        }
        else if (role.equals("Admin")) {
            Administrator admin = administratorRepository.findByEmail(email);
            if(admin.getPassword().equals(password)){
                return "Login Successful";
            }
            else{
                return "Invalid Password";
            }

        }
        else {
            return "emailId not found";
        }
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    }
}
