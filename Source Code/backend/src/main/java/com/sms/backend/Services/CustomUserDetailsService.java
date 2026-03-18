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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private AdministratorRepository adminRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private TeacherRepository teacherRepo;

    @Autowired
    private ParentRepository parentRepo;

    private UserDetails buildUser(String email, String password, String role) {

        return org.springframework.security.core.userdetails.User
                .withUsername(email)
                .password(password)
                .roles(role)
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {



        // 1. Check Admin
        Optional<Administrator> admin = Optional.ofNullable(adminRepo.findByEmail(email));
        if (admin.isPresent()) {
            return buildUser(admin.get().getEmail(), admin.get().getPassword(), "Admin");
        }

        // 2. Check Teacher
        Optional<Teacher> teacher = Optional.ofNullable(teacherRepo.findByEmail(email));
        if (teacher.isPresent()) {
            return buildUser(teacher.get().getEmail(), teacher.get().getPassword(), "Teacher");
        }

        // 3. Check Student
        Optional<Student> student = Optional.ofNullable(studentRepo.findByEmail(email));
        if (student.isPresent()) {
            return buildUser(student.get().getEmail(), student.get().getPassword(), "Student");
        }

        // 4. Check Parent
        Optional<Parent> parent = Optional.ofNullable(parentRepo.findByEmail(email));
        if (parent.isPresent()) {
            return buildUser(parent.get().getEmail(), parent.get().getPassword(), "Parent");
        }

        throw new UsernameNotFoundException("User not found");
    }
}