package com.sms.backend.DTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateParentDTO {

    private String name;
    private int age;
    private String mobileNumber;
    private String email;
    private String address;

    private List<Long> studentIds;
}