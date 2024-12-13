package com.bappi.pms.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PatientInfoRequestDto {

    private String fullName;

    private String code;

    private Float age;

    private String gender;

    private String contactNo;

    private String address;

    private String patientType;

}
