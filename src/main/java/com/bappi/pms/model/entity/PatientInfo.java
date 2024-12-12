package com.bappi.pms.model.entity;

import com.bappi.pms.config.PatientInfoDBConstant;
import com.bappi.pms.model.enums.PatientType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

import static com.bappi.pms.config.PatientInfoDBConstant.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = PatientInfoDBConstant.PATIENT_INFO_TABLE)
public class PatientInfo implements Serializable {

    @Id
    @Column(name = ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = CODE,unique=true)
    private String code;

    @Column(name = PatientInfoDBConstant.FULL_NAME)
    private String fullName;

    @Column(name = PatientInfoDBConstant.AGE)
    private String age;

    @Column(name = PatientInfoDBConstant.GENDER)
    private String gender;

    @Column(name = PatientInfoDBConstant.CONTACT_NO)
    private String contactNo;

    @Column(name = PatientInfoDBConstant.ADDRESS)
    private String address;

    @Column(name = PatientInfoDBConstant.PATIENT_TYPE)
    @Enumerated(EnumType.STRING)
    private PatientType patientType;

    @Column(name = CREATE_DATE)
    private Timestamp createDate = new Timestamp(System.currentTimeMillis());

    @Column(name = CREATED_BY)
    private Long createBy;

    @Column(name = UPDATE_DATE)
    private Timestamp updateDate = new Timestamp(System.currentTimeMillis());

    @Column(name = UPDATED_BY)
    private Long updateBy;

}
