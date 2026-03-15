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
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sound.midi.SysexMessage;
import java.util.List;

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
    @Autowired
    private PasswordEncoder passwordEncoder;


    public String addStudent(Student student)
    {
        student.setPassword(passwordEncoder.encode("1234"));
        studentRepository.save(student);
        return "Student Added Successfully";
    }

    public String addTeacher(Teacher teacher)
    {
        teacher.setPassword(passwordEncoder.encode("1234"));
        teacherRepository.save(teacher);
        return "Teacher Added Successfully";
    }

    public String addParent(Parent parent)
    {
        parent.setPassword(passwordEncoder.encode("1234"));
        parentRepository.save(parent);
        Parent temp = parentRepository.findByEmail(parent.getEmail());
        for(Long id: temp.getStudentIds())
        {
            studentRepository.findById(id).ifPresent(student -> {student.setParentId(temp.getId()); studentRepository.save(student);});
        }
        return "Parent Added Successfully";
    }

    public String deleteStudent(Long id)
    {
        try
        {
            studentRepository.deleteById(id);
            return "Student Deleted Successfully";
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public String deleteTeacher(Long id)
    {
        try
        {
            teacherRepository.deleteById(id);
            return "Teacher Deleted Successfully";
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public String deleteParent(Long id)
    {
        try
        {
            parentRepository.deleteById(id);
            return "Parent Deleted Successfully";
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public String addAdmin(Administrator admin) {
        admin.setPassword(passwordEncoder.encode("1234"));
        administratorRepository.save(admin);
        return "Admin addedd successfully";
    }

    public ResponseEntity<?> fetchAllStudents() {
        try
        {
            List<Student> studentList = studentRepository.findAll();
            return ResponseEntity.status(200).body(studentList);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ResponseEntity<?> fetchAllParents() {
        try
        {
            List<Parent> parentList = parentRepository.findAll();
            return ResponseEntity.status(200).body(parentList);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ResponseEntity<?> fetchAllTeachers() {
        try
        {
            List<Teacher> teacherList = teacherRepository.findAll();
            return ResponseEntity.status(200).body(teacherList);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ResponseEntity<?> fetchAllStudentsByClassId(String classId) {
        try
        {
            List<Student> studentList = studentRepository.findAllByClassId(classId);
            return ResponseEntity.status(200).body(studentList);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }
}