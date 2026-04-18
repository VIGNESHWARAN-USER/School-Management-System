package com.sms.backend.DTO;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDTO {

    private Long id;
    private Long eventId;
    private Long studentId;
    private String name;
    private String classId;
    private String age;
    private String email;
    
}