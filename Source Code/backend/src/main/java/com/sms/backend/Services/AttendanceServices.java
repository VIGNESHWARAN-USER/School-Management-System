package com.sms.backend.Services;

import java.time.LocalDate;
import java.util.List;

import com.sms.backend.DTO.AttendanceDTO;
import com.sms.backend.DTO.StudentAttendanceDTO;
import com.sms.backend.DTO.TeacherAttendanceDTO;
import com.sms.backend.Entities.*;
import com.sms.backend.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class AttendanceServices {

    @Autowired
    private StudentAttendanceRepository studentAttendanceRepository;
    @Autowired
    private TeacherAttendanceRepository teacherAttendanceRepository;
    @Autowired
    private ParentRepository parentRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private EmailService emailService;

    String htmlContent = """
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f6f8;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 600px;
            margin: 20px auto;
            background: #ffffff;
            border-radius: 8px;
            padding: 20px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.1);
        }
        .header {
            background-color: #d32f2f;
            color: white;
            padding: 12px;
            text-align: center;
            font-size: 18px;
            border-radius: 6px 6px 0 0;
        }
        .content {
            margin: 20px 0;
            color: #333;
            line-height: 1.6;
        }
        .details {
            background: #f9fafb;
            padding: 15px;
            border-radius: 6px;
            margin-top: 10px;
        }
        .footer {
            font-size: 12px;
            color: #777;
            text-align: center;
            margin-top: 20px;
        }
        .highlight {
            color: #d32f2f;
            font-weight: bold;
        }
    </style>
</head>

<body>
    <div class="container">

        <div class="header">
            Attendance Alert
        </div>

        <div class="content">
            Dear %s,<br><br>

            This is to inform you that your child 
            <span class="highlight">%s</span> 
            was marked <span class="highlight">%s</span> today.
        </div>

        <div class="details">
            <strong>Student Name:</strong> %s <br>
            <strong>Class:</strong> %s <br>
            <strong>Date:</strong> %s <br>
            <strong>Status:</strong> Absent <br>
            <strong>Remarks:</strong> %s <br>
        </div>

        <div class="content">
            Kindly ensure your child attends school regularly.  
            If this is due to any valid reason, please inform the school.
        </div>

        <div class="footer">
            This is an automated message from School Management System.<br>
            Please do not reply to this email.
        </div>

    </div>
</body>
</html>
""";

    String htmlContentForTeacher = """
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f6f8;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 600px;
            margin: 20px auto;
            background: #ffffff;
            border-radius: 8px;
            padding: 20px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.1);
        }
        .header {
            background-color: #d32f2f;
            color: white;
            padding: 12px;
            text-align: center;
            font-size: 18px;
            border-radius: 6px 6px 0 0;
        }
        .content {
            margin: 20px 0;
            color: #333;
            line-height: 1.6;
        }
        .details {
            background: #f9fafb;
            padding: 15px;
            border-radius: 6px;
            margin-top: 10px;
        }
        .footer {
            font-size: 12px;
            color: #777;
            text-align: center;
            margin-top: 20px;
        }
        .highlight {
            color: #d32f2f;
            font-weight: bold;
        }
    </style>
</head>

<body>
    <div class="container">

        <div class="header">
            Attendance Alert
        </div>

        <div class="content">
            Dear %s,<br><br>

            This is to inform you that your attendance
            was marked <span class="highlight">%s</span> today.
        </div>

        <div class="details">
            <strong>Name:</strong> %s <br>
            <strong>Class:</strong> %s <br>
            <strong>Date:</strong> %s <br>
            <strong>Status:</strong> Absent <br>
            <strong>Remarks:</strong> %s <br>
        </div>

        <div class="footer">
            This is an automated message from School Management System.<br>
            Please do not reply to this email.
        </div>

    </div>
</body>
</html>
""";

    @Autowired
    private TeacherRepository teacherRepository;


    public ResponseEntity<?> saveStudentAttendance(List<AttendanceDTO> attendanceList) {
        try {
            for (AttendanceDTO i : attendanceList) {

                StudentAttendance existing =
                        studentAttendanceRepository.findByDateAndStudent(i.getDate(), studentRepository.findById(i.getId()).orElse(null));

                if (existing != null) {
                    existing.setStatus(i.getStatus());
                    existing.setRemarks(i.getRemarks());
                    System.out.println(i.getClassId());
                    existing.setClassId(i.getClassId());

                    studentAttendanceRepository.save(existing);

                    Student student = studentRepository.findById(i.getId()).orElse(null);
                    if(student != null)
                    {
                        int index = student.getAttendenceList().indexOf(existing);

                    }

                } else {

                    StudentAttendance studentAttendance = new StudentAttendance();

                    studentAttendance.setStudent(studentRepository.findById(i.getId()).orElse(null));
                    studentAttendance.setRemarks(i.getRemarks());
                    studentAttendance.setStatus(i.getStatus());
                    studentAttendance.setDate(i.getDate());
                    studentAttendance.setClassId(i.getClassId());
                    studentAttendanceRepository.save(studentAttendance);

                    Student student = studentRepository.findById(i.getId()).orElse(null);
                    assert student != null;
                    student.getAttendenceList().add(studentAttendance);
                    studentAttendanceRepository.save(studentAttendance);
                }

                if ("Absent".equalsIgnoreCase(i.getStatus()) || "Late".equalsIgnoreCase(i.getStatus())) {

                    Student student = studentRepository.findById(i.getId()).orElse(null);
                    if (student == null) continue;

                    Parent parent = student.getParent();
                    if (parent == null) continue;

                    String formattedHtml = String.format(
                            htmlContent,
                            parent.getName(),
                            student.getName(),
                            i.getStatus(),
                            student.getName(),
                            student.getClassRoom().getClassId(),
                            i.getDate(),
                            i.getRemarks()
                    );

                    emailService.sendHtmlEmail(parent.getEmail(), "Attendance Alert", formattedHtml);
                }
            }

            return ResponseEntity.ok(studentAttendanceRepository.findAll());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }


    public ResponseEntity<?> saveTeacherAttendance(List<AttendanceDTO> attendanceList) {
        try {
            for (AttendanceDTO i : attendanceList) {
                TeacherAttendance existing =
                        teacherAttendanceRepository.findByDateAndTeacher(i.getDate(), teacherRepository.findById(i.getId()).orElse(null));

                if (existing != null) {
                    existing.setStatus(i.getStatus());
                    existing.setRemarks(i.getRemarks());
                    existing.setClassId(i.getClassId());
                    teacherAttendanceRepository.save(existing);
                } else {
                    TeacherAttendance teacherAttendance = new TeacherAttendance();
                    teacherAttendance.setTeacher(teacherRepository.findById(i.getId()).orElse(null));
                    teacherAttendance.setRemarks(i.getRemarks());
                    teacherAttendance.setStatus(i.getStatus());
                    teacherAttendance.setDate(i.getDate());
                    teacherAttendance.setClassId(i.getClassId());

                   teacherAttendanceRepository.save(teacherAttendance);

                    Teacher teacher = teacherRepository.findById(i.getId()).orElse(null);
                    assert teacher != null;
                    teacher.getAttendenceList().add(teacherAttendance);
                    teacherAttendanceRepository.save(teacherAttendance);

                }

                if ("Absent".equalsIgnoreCase(i.getStatus()) || "Late".equalsIgnoreCase(i.getStatus())) {

                    Teacher teacher = teacherRepository.findById(i.getId()).orElse(null);
                    if (teacher == null) continue;



                    String formattedHtml = String.format(
                            htmlContentForTeacher,
                            teacher.getName(),
                            i.getStatus(),
                            teacher.getName(),
                            teacher.getClassRoom().getClassId(),
                            i.getDate(),
                            i.getRemarks()
                    );

                    emailService.sendHtmlEmail(teacher.getEmail(), "Attendance Alert", formattedHtml);
                }
            }

            return ResponseEntity.ok(studentAttendanceRepository.findAll());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }

    public ResponseEntity<?> getAllStudentAttendance(int classId, LocalDate date){
        try
        {
            List<StudentAttendance> studentAttendanceList = studentAttendanceRepository.findAllByClassIdAndDate(classId, date);

            List<AttendanceDTO> attendanceDTOS = studentAttendanceList.stream().map(studentAttendance ->
            {
                AttendanceDTO attendanceDTO = new AttendanceDTO();

                attendanceDTO.setId(studentAttendance.getId());
                attendanceDTO.setStatus(studentAttendance.getStatus());
                attendanceDTO.setRemarks(studentAttendance.getRemarks());
                attendanceDTO.setDate(studentAttendance.getDate());
                attendanceDTO.setName(studentAttendance.getStudent().getName());
                attendanceDTO.setClassId(studentAttendance.getClassId());

                return attendanceDTO;
            }).toList();

            return ResponseEntity.status(200).body(attendanceDTOS);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ResponseEntity<?> getAllTeacherAttendance(LocalDate date) {
        try
        {
            List<TeacherAttendance> teacherAttendanceList = teacherAttendanceRepository.findAllByDate(date);

            List<AttendanceDTO> attendanceDTOS = teacherAttendanceList.stream().map(studentAttendance ->
            {
                AttendanceDTO attendanceDTO = new AttendanceDTO();

                attendanceDTO.setId(studentAttendance.getId());
                attendanceDTO.setStatus(studentAttendance.getStatus());
                attendanceDTO.setRemarks(studentAttendance.getRemarks());
                attendanceDTO.setDate(studentAttendance.getDate());
                attendanceDTO.setName(studentAttendance.getTeacher().getName());
                attendanceDTO.setClassId(studentAttendance.getClassId());

                return attendanceDTO;
            }).toList();

            return ResponseEntity.status(200).body(attendanceDTOS);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ResponseEntity<?> getIndividualAttendance(String role, Long id) {
        if ("Teacher".equalsIgnoreCase(role)) {
            return teacherRepository.findById(id).map(teacher -> {
                List<TeacherAttendanceDTO> dtos = teacher.getAttendenceList().stream().map(attendance -> {
                    TeacherAttendanceDTO dto = new TeacherAttendanceDTO();
                    dto.setId(attendance.getId());

                    // SAFE PARSING: Handle potential nulls for Class ID
                    if (attendance.getClassId() != null) {
                        try {
                            // Only parse if it's a String. If it's already a Long, just set it.
                            dto.setClassId(Long.valueOf(attendance.getClassId().toString()));
                        } catch (NumberFormatException e) {
                            dto.setClassId(null);
                        }
                    }

                    dto.setRemarks(attendance.getRemarks());
                    dto.setStatus(attendance.getStatus());
                    dto.setDate(attendance.getDate());
                    return dto;
                }).toList();
                return ResponseEntity.ok(dtos);
            }).orElse(ResponseEntity.notFound().build());
        }

        else if ("Parent".equalsIgnoreCase(role) || "Student".equalsIgnoreCase(role)) {
            return studentRepository.findById(id).map(student -> {
                List<StudentAttendanceDTO> dtos = student.getAttendenceList().stream().map(attendance -> {
                    StudentAttendanceDTO dto = new StudentAttendanceDTO();

                    // SAFE PARSING: Handle potential nulls
                    if (attendance.getClassId() != null) {
                        try {
                            dto.setClassId(String.valueOf(Long.valueOf(attendance.getClassId().toString())));
                        } catch (NumberFormatException e) {
                            dto.setClassId(null);
                        }
                    }

                    dto.setRemarks(attendance.getRemarks());
                    dto.setStatus(attendance.getStatus());
                    dto.setDate(attendance.getDate());
                    return dto;
                }).toList();
                return ResponseEntity.ok(dtos);
            }).orElse(ResponseEntity.notFound().build());
        }

        return ResponseEntity.badRequest().body("Invalid Role");
    }
}