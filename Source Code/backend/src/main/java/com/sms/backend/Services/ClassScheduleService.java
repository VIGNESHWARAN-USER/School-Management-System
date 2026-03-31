package com.sms.backend.Services;

import com.sms.backend.DTO.ClassRoomDTO;
import com.sms.backend.DTO.ScheduleDTO;
import com.sms.backend.DTO.SubjectDTO;
import com.sms.backend.DTO.TeacherDTO;
import com.sms.backend.Entities.*;
import com.sms.backend.Repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClassScheduleService {

    @Autowired
    ClassRoomRepository classRoomRepository;
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;


    public ResponseEntity<?> addClassRoom(ClassRoomDTO dto)
    {
        ClassRoom classRoom = new ClassRoom();

        classRoom.setClassName(dto.getClassName());
        classRoom.setSection(dto.getSection());
        classRoom.setAcademicYear(dto.getAcademicYear());
        classRoom.setCapacity(dto.getCapacity());

        classRoomRepository.save(classRoom);
        return ResponseEntity.status(200).body(classRoom);
    }


    public String addSubject(Subject subject)
    {
        System.out.println("In services: "+subject.getSubjectName());
        subjectRepository.save(subject);
        return "Subject Added Successfully";
    }

    public String addSchedule(ScheduleDTO dto)
    {
        Schedule schedule = new Schedule();

        schedule.setStartTime(dto.getStartTime());
        schedule.setEndTime(dto.getEndTime());
        schedule.setDayOfWeek(dto.getDayOfWeek());
        schedule.setTeacher(teacherRepository.findById(dto.getTeacherId()).orElse(null));
        schedule.setClassRoom(classRoomRepository.findById(dto.getClassId()).orElse(null));
        schedule.setSubject(subjectRepository.findBySubjectId(dto.getSubjectId()));

        scheduleRepository.save(schedule);

        Teacher teacher = teacherRepository.findById(dto.getTeacherId()).orElse(null);
        if(teacher != null)
        {
            teacher.getSchedules().add(schedule);
            teacherRepository.save(teacher);
        }

        ClassRoom classRoom = classRoomRepository.findById(dto.getClassId()).orElse(null);
        if(classRoom != null)
        {
            classRoom.getSchedules().add(schedule);
            classRoomRepository.save(classRoom);
        }

        return "Schedule Added Successfully";
    }

    public ResponseEntity<?> getAllClassRooms() {
        List<ClassRoom> classRoomList = classRoomRepository.findAll();

        List<ClassRoomDTO> classRoomDTOS = classRoomList.stream().map(classRoom -> {

            ClassRoomDTO dto = new ClassRoomDTO();

            dto.setClassId(classRoom.getClassId());
            dto.setClassName(classRoom.getClassName());
            dto.setCapacity(classRoom.getCapacity());
            dto.setSection(classRoom.getSection());
            dto.setAcademicYear(classRoom.getAcademicYear());

            return dto;
        }).toList();

        return ResponseEntity.status(200).body(classRoomDTOS);
    }

    public ResponseEntity<?> getAllSubjects() {
        List<Subject> subjects = subjectRepository.findAll();
        List<SubjectDTO> subjectDTOS = subjects.stream().map(subject -> {
            SubjectDTO subjectDTO = new SubjectDTO();

            subjectDTO.setSubjectCode(subject.getSubjectCode());
            subjectDTO.setSubjectName(subject.getSubjectName());
            subjectDTO.setSubjectId(subject.getSubjectId());
            return subjectDTO;
        }).toList();
        return ResponseEntity.status(200).body(subjectDTOS);
    }

    public ResponseEntity<?> getAllTeachers() {
        List<Teacher> teachers = teacherRepository.findAll();

        List<TeacherDTO> teacherDTOList = teachers.stream().map(teacher -> {
            TeacherDTO teacherDTO = new TeacherDTO();
            teacherDTO.setName(teacher.getName());
            teacherDTO.setId(teacher.getId());
            teacherDTO.setEmail(teacher.getEmail());

            return teacherDTO;
        }).toList();
        return ResponseEntity.status(200).body(teacherDTOList);
    }

    public ResponseEntity<?> fetchClassSchedule(Long classId) {
        List<Schedule> schedules = scheduleRepository.findAllByClassRoom(classRoomRepository.findById(classId).orElse(null));
        List<ScheduleDTO> scheduleDTOS = schedules.stream().map(schedule -> {
            ScheduleDTO scheduleDTO = new ScheduleDTO();

            scheduleDTO.setClassId(schedule.getClassRoom().getClassId());
            scheduleDTO.setSubjectId(schedule.getSubject().getSubjectId());
            scheduleDTO.setStartTime(schedule.getStartTime());
            scheduleDTO.setEndTime(schedule.getEndTime());
            scheduleDTO.setDayOfWeek(schedule.getDayOfWeek());
            scheduleDTO.setTeacherId(schedule.getTeacher().getId());

            return scheduleDTO;
        }).toList();
        return ResponseEntity.status(200).body(scheduleDTOS);
    }

    public ResponseEntity<?> fetchTeacherSchedule(Long userId) {
        Teacher teacher = teacherRepository.findById(userId).orElse(null);
        if (teacher != null) {
            List<Schedule> schedules = teacher.getSchedules();
            return ResponseEntity.status(200).body(schedules);
        }
        else {
            return ResponseEntity.status(404).body("Teacher id not found");
        }
    }

    public ResponseEntity<?> fetchStudentSchedule(Long userId) {
        Student student = studentRepository.findById(userId).orElse(null);
        if (student != null) {
            ClassRoom classRoom = classRoomRepository.findById(Long.valueOf(student.getClassRoom().getClassId())).orElse(null);
            assert classRoom != null;
            List<Schedule> schedules = classRoom.getSchedules();
            if(schedules != null)
                return ResponseEntity.status(200).body(schedules);
            else
                return ResponseEntity.status(404).body("Schedules not found");
        }
        else {
            return ResponseEntity.status(404).body("Teacher id not found");
        }
    }
}