package com.sms.backend.Services;

import com.sms.backend.DTO.*;
import com.sms.backend.Entities.Administrator;
import com.sms.backend.Entities.Student;
import com.sms.backend.Entities.Teacher;
import com.sms.backend.Entities.Parent;
import com.sms.backend.Repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private ClassRoomRepository classRoomRepository;


    public String addStudent(StudentDTO studentdto)
    {
        Student student = new Student();

        student.setAge(studentdto.getAge());
        student.setAddress(studentdto.getAddress());
        student.setEmail(studentdto.getEmail());
        student.setClassId(studentdto.getClassId());
        student.setName(studentdto.getName());

        student.setPassword(passwordEncoder.encode("1234"));
        studentRepository.save(student);
        return "Student Added Successfully";
    }

    public String addTeacher(TeacherDTO teacherdto)
    {
        Teacher teacher = new Teacher();

        teacher.setName(teacherdto.getName());
        teacher.setEmail(teacherdto.getEmail());
        teacher.setSubject(subjectRepository.findBySubjectCode(teacherdto.getSubject()));
        teacher.setClassRoom(classRoomRepository.findById(Long.valueOf(teacherdto.getClassId())).orElse(null));
        teacher.setPhoneNumber(teacherdto.getPhoneNumber());

        teacher.setPassword(passwordEncoder.encode("1234"));
        teacherRepository.save(teacher);
        return "Teacher Added Successfully";
    }

    public String addParent(CreateParentDTO parentdto) {

        Parent parent = new Parent();

        parent.setAddress(parentdto.getAddress());
        parent.setName(parentdto.getName());
        parent.setAge(parentdto.getAge());
        parent.setMobileNumber(parentdto.getMobileNumber());
        parent.setEmail(parentdto.getEmail());
        parent.setPassword(passwordEncoder.encode("1234"));


        parent = parentRepository.save(parent);

        List<Student> studentList = new ArrayList<>();

        for (Long id : parentdto.getStudentIds()) {

            Student student = studentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            student.setParent(parent);

            studentList.add(student);
        }


        studentRepository.saveAll(studentList);
        parent.setStudentList(studentList);

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

        try {
            List<Student> students = studentRepository.findAll();

            List<StudentDTO> studentList = students.stream()
                    .map(student -> {

                        StudentDTO dto = new StudentDTO();

                        dto.setId(student.getId());
                        dto.setName(student.getName());
                        dto.setAge(student.getAge());
                        dto.setEmail(student.getEmail());
                        dto.setClassId(student.getClassId());
                        dto.setAddress(student.getAddress());

                        // 🔹 Parent mapping
                        if (student.getParent() != null) {
                            ParentDTO parentDTO = new ParentDTO();
                            parentDTO.setId(student.getParent().getId());
                            parentDTO.setName(student.getParent().getName());
                            parentDTO.setMobileNumber(student.getParent().getMobileNumber());

                            dto.setParentDTO(parentDTO);
                        }

                        // 🔹 Attendance mapping
                        if (student.getAttendenceList() != null) {
                            List<AttendanceDTO> attendanceList =
                                    student.getAttendenceList().stream()
                                            .map(att -> {
                                                AttendanceDTO a = new AttendanceDTO();
                                                a.setDate(att.getDate());
                                                a.setStatus(att.getStatus());
                                                a.setRemarks(att.getRemarks());
                                                return a;
                                            })
                                            .toList();

                            dto.setAttendanceDTOList(attendanceList);
                        }

                        return dto;

                    }).toList();

            return ResponseEntity.ok(studentList);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error fetching students");
        }
    }

    public ResponseEntity<?> fetchAllParents() {
        try
        {
            List<Parent> parents = parentRepository.findAll();

            List<ParentDTO> parentList = parents.stream().map(parent->{
                ParentDTO parentDTO = new ParentDTO();

                parentDTO.setName(parent.getName());
                parentDTO.setMobileNumber(parent.getMobileNumber());
                parentDTO.setAge(parent.getAge());
                parentDTO.setId(parent.getId());
                parentDTO.setEmail(parent.getEmail());
                parentDTO.setAddress(parent.getAddress());

               if(parent.getStudentList() != null)
               {
                   List<Student> students = parent.getStudentList();

                   List<StudentDTO> studentDTOList = students.stream().map(student->{
                       StudentDTO dto = new StudentDTO();
                       dto.setId(student.getId());
                       dto.setName(student.getName());
                       dto.setAge(student.getAge());
                       dto.setEmail(student.getEmail());
                       dto.setClassId(student.getClassId());
                       dto.setAddress(student.getAddress());

                       if (student.getAttendenceList() != null) {
                           List<AttendanceDTO> attendanceList =
                                   student.getAttendenceList().stream()
                                           .map(att -> {
                                               AttendanceDTO a = new AttendanceDTO();
                                               a.setDate(att.getDate());
                                               a.setStatus(att.getStatus());
                                               a.setRemarks(att.getRemarks());
                                               return a;
                                           })
                                           .toList();

                           dto.setAttendanceDTOList(attendanceList);
                       }

                       return dto;
                   }).toList();


               }
                return parentDTO;
            }).toList();

            return ResponseEntity.status(200).body(parentList);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ResponseEntity<?> fetchAllTeachers() {
        try {
            List<Teacher> teachers = teacherRepository.findAll();

            List<TeacherDTO> teacherDTOList = teachers.stream()
                    .map(teacher -> {

                        TeacherDTO dto = new TeacherDTO();

                        dto.setId(teacher.getId());
                        dto.setName(teacher.getName());
                        dto.setEmail(teacher.getEmail());
                        dto.setClassId(String.valueOf(teacher.getClassRoom().getClassId()));
                        dto.setPhoneNumber(teacher.getPhoneNumber());
                        dto.setSubject(teacher.getSubject().getSubjectName());


                        if (teacher.getAttendenceList() != null) {
                            List<AttendanceDTO> attendanceList =
                                    teacher.getAttendenceList().stream()
                                            .map(att -> {
                                                AttendanceDTO a = new AttendanceDTO();
                                                a.setDate(att.getDate());
                                                a.setStatus(att.getStatus());
                                                a.setRemarks(att.getRemarks());
                                                return a;
                                            })
                                            .toList();

                            dto.setAttendanceList(attendanceList);
                        }

                        return dto;

                    }).toList();

            return ResponseEntity.ok(teacherDTOList);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error fetching students");
        }
    }

    public ResponseEntity<?> fetchAllStudentsByClassId(String classId) {
        try {
            List<Student> students = studentRepository.findAllByClassId(classId);

            List<StudentDTO> studentList = students.stream()
                    .map(student -> {

                        StudentDTO dto = new StudentDTO();

                        dto.setId(student.getId());
                        dto.setName(student.getName());
                        dto.setAge(student.getAge());
                        dto.setEmail(student.getEmail());
                        dto.setClassId(student.getClassId());
                        dto.setAddress(student.getAddress());

                        // 🔹 Parent mapping
                        if (student.getParent() != null) {
                            ParentDTO parentDTO = new ParentDTO();
                            parentDTO.setId(student.getParent().getId());
                            parentDTO.setName(student.getParent().getName());
                            parentDTO.setMobileNumber(student.getParent().getMobileNumber());

                            dto.setParentDTO(parentDTO);
                        }

                        // 🔹 Attendance mapping
                        if (student.getAttendenceList() != null) {
                            List<AttendanceDTO> attendanceList =
                                    student.getAttendenceList().stream()
                                            .map(att -> {
                                                AttendanceDTO a = new AttendanceDTO();
                                                a.setDate(att.getDate());
                                                a.setStatus(att.getStatus());
                                                a.setRemarks(att.getRemarks());
                                                return a;
                                            })
                                            .toList();

                            dto.setAttendanceDTOList(attendanceList);
                        }

                        return dto;

                    }).toList();

            return ResponseEntity.ok(studentList);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error fetching students");
        }
    }
}