package com.sms.backend.Services;

import com.sms.backend.DTO.*;
import com.sms.backend.Entities.Administrator;
import com.sms.backend.Entities.Parent;
import com.sms.backend.Entities.Student;
import com.sms.backend.Entities.Teacher;
import com.sms.backend.Repositories.AdministratorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.sms.backend.Repositories.ParentRepository;
import com.sms.backend.Repositories.StudentRepository;
import com.sms.backend.Repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;

    public ResponseEntity<?> login(String email, String password, String role) {
    try{
        switch (role) {
            case "Student" -> {
                Student student = studentRepository.findByEmail(email);
                if (passwordEncoder.matches(password, student.getPassword())) {
                    return ResponseEntity.status(200).body(student);
                } else {
                    return ResponseEntity.status(201).body("Invalid Password");
                }
            }
            case "Parent" -> {
                Parent parent = parentRepository.findByEmail(email);
                if (passwordEncoder.matches(password, parent.getPassword())) {
                    return ResponseEntity.status(200).body(parent);
                } else {
                    return ResponseEntity.status(201).body("Invalid Password");
                }
            }
            case "Teacher" -> {
                Teacher teacher = teacherRepository.findByEmail(email);
                if (passwordEncoder.matches(password, teacher.getPassword())){
                    return ResponseEntity.status(200).body(teacher);
                } else {
                    return ResponseEntity.status(201).body("Invalid Password");
                }
            }
            case "Admin" -> {
                System.out.println(email+" "+password+" "+role);
                Administrator admin = administratorRepository.findByEmail(email);
                if (passwordEncoder.matches(password, admin.getPassword())) {
                    return ResponseEntity.status(200).body(admin);
                } else {
                    return ResponseEntity.status(201).body("Invalid Password");
                }
            }
            default -> {
                return ResponseEntity.notFound().build();
            }
        }
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    }

    public ResponseEntity<?> forgotPassword(String email, String role) {
        try{

            if(role.equals("Student"))
            {
                Student user = studentRepository.findByEmail(email);
                if(user  == null) return ResponseEntity.notFound().build();
            }
            else if(role.equals("Teacher"))
            {
                Teacher user = teacherRepository.findByEmail(email);
                if(user  == null) return ResponseEntity.notFound().build();
            }
            else if(role.equals("Parent"))
            {
                Parent user = parentRepository.findByEmail(email);
                if(user  == null) return ResponseEntity.notFound().build();
            }
            else
            {
                Administrator user = administratorRepository.findByEmail(email);
                if(user  == null) return ResponseEntity.notFound().build();
            }
            int otp = (int) (Math.random() * 10000);

            String htmlContent = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "  <meta charset=\"UTF-8\">\n" +
                    "  <style>\n" +
                    "    .container {\n" +
                    "      font-family: Arial, sans-serif;\n" +
                    "      padding: 20px;\n" +
                    "      border: 1px solid #e0e0e0;\n" +
                    "      border-radius: 10px;\n" +
                    "      max-width: 500px;\n" +
                    "      margin: auto;\n" +
                    "      background-color: #f9f9f9;\n" +
                    "    }\n" +
                    "\n" +
                    "    .header {\n" +
                    "      background-color: #007bff;\n" +
                    "      color: white;\n" +
                    "      padding: 15px;\n" +
                    "      text-align: center;\n" +
                    "      border-radius: 10px 10px 0 0;\n" +
                    "      font-size: 20px;\n" +
                    "    }\n" +
                    "\n" +
                    "    .body {\n" +
                    "      padding: 20px;\n" +
                    "      color: #333;\n" +
                    "    }\n" +
                    "\n" +
                    "    .otp-box {\n" +
                    "      background-color: #e6f0ff;\n" +
                    "      color: #007bff;\n" +
                    "      font-weight: bold;\n" +
                    "      font-size: 24px;\n" +
                    "      text-align: center;\n" +
                    "      padding: 15px;\n" +
                    "      border-radius: 5px;\n" +
                    "      margin: 20px 0;\n" +
                    "      letter-spacing: 3px;\n" +
                    "    }\n" +
                    "\n" +
                    "    .footer {\n" +
                    "      font-size: 12px;\n" +
                    "      color: #777;\n" +
                    "      text-align: center;\n" +
                    "      margin-top: 30px;\n" +
                    "    }\n" +
                    "  </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "  <div class=\"container\">\n" +
                    "    <div class=\"header\">\n" +
                    "      Your OTP Code\n" +
                    "    </div>\n" +
                    "    <div class=\"body\">\n" +
                    "      <p>Dear User,</p>\n" +
                    "      <p>Use the following OTP to complete your action. This OTP is valid for the next 5 minutes.</p>\n" +
                    "\n" +
                    "      <div class=\"otp-box\">\n" +
                    "        otp_value\n" +
                    "      </div>\n" +
                    "\n" +
                    "      <p>If you did not request this, please ignore this email.</p>\n" +
                    "      <p>Thank you,<br><strong>SMS Team</strong></p>\n" +
                    "    </div>\n" +
                    "    <div class=\"footer\">\n" +
                    "      Please do not share this code with anyone for security reasons.\n" +
                    "    </div>\n" +
                    "  </div>\n" +
                    "</body>\n" +
                    "</html>\n";
            htmlContent = htmlContent.replace("otp_value", Integer.toString(otp));
            emailService.sendHtmlEmail(email, "Password Recovery", htmlContent);

            return ResponseEntity.ok().body(otp);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

    public ResponseEntity<?> resetPassword(String email, String password, String role) {
        try{
            if(role.equals("Student"))
            {
                Student user = studentRepository.findByEmail(email);
                System.out.println(password);
                if(user  == null) return ResponseEntity.notFound().build();
                else
                {
                    user.setPassword(passwordEncoder.encode(password));
                    studentRepository.save(user);
                    return ResponseEntity.status(201).body("Password has been changed successfully");
                }
            }
            else if(role.equals("Teacher"))
            {
                Teacher user = teacherRepository.findByEmail(email);
                System.out.println(password);
                if(user  == null) return ResponseEntity.notFound().build();
                else
                {
                    user.setPassword(passwordEncoder.encode(password));
                    teacherRepository.save(user);
                    return ResponseEntity.status(201).body("Password has been changed successfully");
                }
            }
            else if(role.equals("Parent"))
            {
                Parent user = parentRepository.findByEmail(email);
                System.out.println(password);
                if(user  == null) return ResponseEntity.notFound().build();
                else
                {
                    user.setPassword(passwordEncoder.encode(password));
                    parentRepository.save(user);
                    return ResponseEntity.status(201).body("Password has been changed successfully");
                }
            }
            else {
                Administrator user = administratorRepository.findByEmail(email);
                System.out.println(password);
                if(user  == null) return ResponseEntity.notFound().build();
                else
                {
                    user.setPassword(passwordEncoder.encode(password));
                    administratorRepository.save(user);
                    return ResponseEntity.status(201).body("Password has been changed successfully");
                }
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

    public ResponseEntity<?> getDetails(String email, String role, String studentId) {
        try{
            switch (role) {
                case "Admin" -> {
                    Administrator admin = administratorRepository.findByEmail(email);
                    if(admin == null) return ResponseEntity.status(404).body("User Not Found");
                    return ResponseEntity.status(200).body(admin);
                }
                case "Parent" -> {
                    Parent parent = parentRepository.findByEmail(email);
                    if(parent == null) return ResponseEntity.status(404).body("User Not Found");
                    for(Student student: parent.getStudentList())
                    {
                        if(student.getId() == Integer.parseInt(studentId))
                        {
                            ParentDTO parentDTO = new ParentDTO();
                            parentDTO.setAddress(parent.getAddress());
                            parentDTO.setId(parent.getId());
                            parentDTO.setName(parent.getName());
                            parentDTO.setEmail(parent.getEmail());
                            parentDTO.setMobileNumber(parent.getMobileNumber());
                            parentDTO.setAge(parent.getAge());
                            return ResponseEntity.status(200).body(parentDTO);
                        }
                    }
                    return ResponseEntity.status(404).body("Student ID Not Found");
                }
                case "Teacher" -> {
                    Teacher teacher = teacherRepository.findByEmail(email);
                    if(teacher == null) return ResponseEntity.status(404).body("User Not Found");
                    TeacherDTO teacherDTO = new TeacherDTO();
                    teacherDTO.setName(teacher.getName());
                    teacherDTO.setPhoneNumber(teacher.getPhoneNumber());
                    teacherDTO.setClassId(String.valueOf(teacher.getClassRoom().getClassId()));
                    teacherDTO.setId(teacher.getId());
                    List<AttendanceDTO> teacherAttendanceDTOList = teacher.getAttendenceList().stream().map(teacherAttendance -> {
                        AttendanceDTO dto = new AttendanceDTO();

                        dto.setId(teacherAttendance.getId());
                        dto.setStatus(teacherAttendance.getStatus());
                        dto.setRemarks(teacherAttendance.getRemarks());
                        dto.setDate(teacherAttendance.getDate());
                        dto.setClassId(teacherAttendance.getClassId());

                        return dto;
                    }).toList();
                    teacherDTO.setAttendanceList(teacherAttendanceDTOList);
                    return ResponseEntity.status(200).body(teacherDTO);
                }
                case "Student" -> {
                    Student student = studentRepository.findByEmail(email);
                    if(student == null) return ResponseEntity.status(404).body("User Not Found");
                    StudentDTO studentDTO = new StudentDTO();
                    studentDTO.setName(student.getName());
                    studentDTO.setEmail(student.getEmail());
                    studentDTO.setId(student.getId());
                    studentDTO.setClassId(String.valueOf(student.getClassRoom().getClassId()));
                    studentDTO.setAddress(student.getAddress());
                    List<AttendanceDTO> studentAttendanceDTOList = student.getAttendenceList().stream().map(teacherAttendance -> {
                        AttendanceDTO dto = new AttendanceDTO();

                        dto.setId(teacherAttendance.getId());
                        dto.setStatus(teacherAttendance.getStatus());
                        dto.setRemarks(teacherAttendance.getRemarks());
                        dto.setDate(teacherAttendance.getDate());
                        dto.setClassId(teacherAttendance.getClassId());

                        return dto;
                    }).toList();
                    studentDTO.setAttendanceDTOList(studentAttendanceDTOList);
                    return ResponseEntity.status(200).body(studentDTO);
                }
                default -> {
                    return ResponseEntity.notFound().build();
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
