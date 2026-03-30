package com.sms.backend.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "subjectId")
    private Subject subject;

    private String email;

    @ManyToOne
    @JoinColumn(name = "classId")
    private ClassRoom classRoom;

    private String password;
    private String phoneNumber;

    @OneToMany(mappedBy = "teacher")
    private List<TeacherAttendance> attendenceList;

    @OneToMany(mappedBy = "teacher")
    private List<Schedule> schedules;

}