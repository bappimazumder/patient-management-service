package com.bappi.pms.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PatientInfoResponseDto {

    private String fullName;

    private String code;

    private Float age;

    private String gender;

    private String contactNo;

    private String address;

    private String patientType;

    private Boolean activeStatus;

    private Timestamp createDate;

}
