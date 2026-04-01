package com.sms.backend.Services;


import com.sms.backend.DTO.ExamScheduleDTO;
import com.sms.backend.DTO.GradeDTO;
import com.sms.backend.Entities.ClassRoom;
import com.sms.backend.Entities.ExamSchedule;
import com.sms.backend.Entities.Grade;
import com.sms.backend.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ExamGradeService {

    @Autowired private ExamRepository examRepo;
    @Autowired private GradeRepository gradeRepo;
    @Autowired
    private ClassRoomRepository classRoomRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private StudentRepository studentRepository;

    // US1: Admin schedules Exam
    public ExamSchedule scheduleExam(ExamScheduleDTO dto) {

        ExamSchedule exam = new ExamSchedule();

        exam.setClassRoom(classRoomRepository.findById(Long.valueOf(dto.getClassName())).orElse(null));
        exam.setExamDate(dto.getExamDate());
        exam.setEndTime(dto.getEndTime());
        exam.setSubject(subjectRepository.findById(Long.valueOf(dto.getSubject())).orElse(null));
        exam.setRoomNumber(dto.getRoomNumber());
        exam.setStartTime(dto.getStartTime());

        ClassRoom classRoom = exam.getClassRoom();

        classRoom.getExamSchedules().add(exam);
        classRoomRepository.save(classRoom);


        if (exam.getSubject() == null || exam.getExamDate() == null) {
            throw new RuntimeException("Subject and Date are required fields!");
        }

        return examRepo.save(exam);
    }

    // US2: Teacher enters grades
    public Grade saveGrade(GradeDTO dto) {

        if (dto.getMarksObtained() == null) {
            throw new RuntimeException("Marks are required!");
        }


        double percentage = (dto.getMarksObtained() / dto.getTotalMarks()) * 100;
        if (percentage >= 90) dto.setLetterGrade("A+");
        else if (percentage >= 80) dto.setLetterGrade("A");
        else if (percentage >= 70) dto.setLetterGrade("B");
        else dto.setLetterGrade("C");

        Grade grade = new Grade();
        grade.setLetterGrade(dto.getLetterGrade());
        grade.setExam(examRepo.findById(dto.getExamId()).orElse(null));
        grade.setSubject(grade.getExam().getSubject());
        grade.setRemarks(dto.getRemarks());
        grade.setMarksObtained(dto.getMarksObtained());
        grade.setStudent(studentRepository.findById(dto.getStudentId()).orElse(null));
        grade.setTotalMarks(dto.getTotalMarks());

        return gradeRepo.save(grade);
    }

    // US3: View Results
    public List<Grade> getStudentResults(String studentId) {
        return gradeRepo.findByStudentId(studentId);
    }


    public List<ExamScheduleDTO> getAllExams() {
        List<ExamSchedule> examSchedules = examRepo.findAll();
        List<ExamScheduleDTO> examScheduleDTOS = examSchedules.stream().map(examSchedule -> {
            ExamScheduleDTO dto = new ExamScheduleDTO();

            dto.setSubject(examSchedule.getSubject().getSubjectName());
            dto.setExamDate(examSchedule.getExamDate());
            dto.setId(examSchedule.getId());
            dto.setRoomNumber(examSchedule.getRoomNumber());
            dto.setClassName(examSchedule.getClassRoom().getClassName());
            dto.setStartTime(examSchedule.getStartTime());
            dto.setEndTime(examSchedule.getEndTime());

            return dto;
        }).toList();

        return examScheduleDTOS;
    }

}