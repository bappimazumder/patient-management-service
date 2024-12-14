package com.bappi.pms.service;

import com.bappi.pms.model.dto.PatientInfoRequestDto;
import com.bappi.pms.model.dto.PatientInfoResponseDto;
import com.bappi.pms.model.entity.PatientInfo;
import com.bappi.pms.model.enums.Gender;
import com.bappi.pms.model.enums.PatientType;
import com.bappi.pms.repository.PatientInfoRepository;
import com.bappi.pms.service.impl.PatientInfoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class PatientInfoServiceImplTest {

    @Mock
    private PatientInfoRepository repository;   

    @InjectMocks
    private PatientInfoServiceImpl patientInfoService;


    @Test
    public void testSavePatientInfo() {

        PatientInfoRequestDto requestDto = new PatientInfoRequestDto();
        requestDto.setFullName("Rahman");
        requestDto.setAge(30.0F);
        requestDto.setGender("MALE");
        requestDto.setPatientType("EMERGENCY");
        requestDto.setContactNo("1234567890");
        requestDto.setAddress("Mohakhali, Dhaka");

        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setId(1L);
        patientInfo.setFullName("Rahman");
        patientInfo.setAge(30.0F);
        patientInfo.setGender(Gender.MALE);
        patientInfo.setPatientType(PatientType.EMERGENCY);
        patientInfo.setContactNo("1234567890");
        patientInfo.setAddress("Mohakhali");

        PatientInfoResponseDto responseDto = new PatientInfoResponseDto();
        responseDto.setFullName("Rahman");
        responseDto.setAge(30.0F);
        responseDto.setCode("PATIENT_CODE_PREFIX123456");

        when(repository.save(any(PatientInfo.class))).thenReturn(patientInfo);

        PatientInfoResponseDto result = patientInfoService.save(requestDto);

        assertNotNull(result);
        assertEquals("Rahman", result.getFullName());
        assertEquals(30, result.getAge());

        verify(repository, times(1)).save(any(PatientInfo.class));
    }

    @Test
    public void testSavePatientInfo_EmptyPatientInfo() {
        PatientInfoRequestDto requestDto = new PatientInfoRequestDto();

        PatientInfo patientInfo = new PatientInfo();

        when(repository.save(any(PatientInfo.class))).thenReturn(patientInfo);

        PatientInfoResponseDto result = patientInfoService.save(requestDto);

        assertNotNull(result);

        verify(repository, times(1)).save(any(PatientInfo.class));

    }

    @Test
    public void testUpdatePatientInfo_Success() {

        PatientInfoRequestDto requestDto = new PatientInfoRequestDto();
        requestDto.setCode("PATIENT_CODE_123");
        requestDto.setFullName("Karim Updated");
        requestDto.setAge(31.0F);
        requestDto.setGender("MALE");
        requestDto.setPatientType("EMERGENCY");
        requestDto.setContactNo("9876543210");
        requestDto.setAddress("456 Updated Mirpur");

        PatientInfo existingPatient = new PatientInfo();
        existingPatient.setCode("PATIENT_CODE_123");
        existingPatient.setFullName("Karim");
        existingPatient.setAge(30.0F);
        existingPatient.setGender(Gender.MALE);
        existingPatient.setPatientType(PatientType.EMERGENCY);
        existingPatient.setContactNo("1234567890");
        existingPatient.setAddress("123 Main Mirpur");

        PatientInfoResponseDto responseDto = new PatientInfoResponseDto();
        responseDto.setFullName("Karim Updated");
        responseDto.setAge(31.0F);
        responseDto.setCode("PATIENT_CODE_123");

        when(repository.findByCode("PATIENT_CODE_123")).thenReturn(existingPatient);
        when(repository.save(existingPatient)).thenReturn(existingPatient);

        PatientInfoResponseDto result = patientInfoService.update(requestDto);

        assertNotNull(result);
        assertEquals("Karim Updated", result.getFullName());
        assertEquals(31.0F, result.getAge(), 0.0);
        assertEquals("PATIENT_CODE_123", result.getCode());

        verify(repository, times(1)).findByCode("PATIENT_CODE_123");
        verify(repository, times(2)).save(existingPatient);

    }

    @Test
    public void testUpdatePatientInfo_PatientNotFound() {
        PatientInfoRequestDto requestDto = new PatientInfoRequestDto();
        requestDto.setCode("PATIENT_CODE_999");

        when(repository.findByCode("PATIENT_CODE_999")).thenReturn(null);


        PatientInfoResponseDto result = patientInfoService.update(requestDto);

        assertNull(result);

        verify(repository, times(1)).findByCode("PATIENT_CODE_999");
        verify(repository, times(0)).save(any(PatientInfo.class));
    }

    @Test
    public void testGetPatientInfo_Success() {
        String patientCode = "PATIENT_CODE_123";

        PatientInfo existingPatient = new PatientInfo();
        existingPatient.setCode(patientCode);
        existingPatient.setFullName("Salman");
        existingPatient.setAge(30.0F);
        existingPatient.setGender(Gender.MALE);
        existingPatient.setPatientType(PatientType.EMERGENCY);
        existingPatient.setContactNo("1234567890");
        existingPatient.setAddress("123 Main Gulshan");

        PatientInfoResponseDto responseDto = new PatientInfoResponseDto();
        responseDto.setCode(patientCode);
        responseDto.setFullName("Salman");
        responseDto.setAge(30.0F);

        when(repository.findByCode(patientCode)).thenReturn(existingPatient);

        PatientInfoResponseDto result = patientInfoService.get(patientCode);

        assertNotNull(result);
        assertEquals(patientCode, result.getCode());
        assertEquals("Salman", result.getFullName());
        assertEquals(30.0F, result.getAge(), 0.0);

        verify(repository, times(1)).findByCode(patientCode);

    }

    @Test
    public void testGetPatientInfo_PatientNotFound() {
        String patientCode = "INVALID_CODE";

        when(repository.findByCode(patientCode)).thenReturn(null);

        PatientInfoResponseDto result = patientInfoService.get(patientCode);

        assertNotNull(result);
        assertNull(result.getCode());
        assertNull(result.getFullName());

        verify(repository, times(1)).findByCode(patientCode);

    }

    @Test
    public void testGetAll_ValidRequest() {
        Integer page = 1;
        Integer size = 5;
        Boolean activeStatus = true;

        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setCode("PATIENT_CODE_123");
        patientInfo.setFullName("Akash");
        patientInfo.setAge(30.0F);
        patientInfo.setGender(Gender.MALE);
        patientInfo.setPatientType(PatientType.EMERGENCY);
        patientInfo.setContactNo("1234567890");
        patientInfo.setAddress("123 Baridhara");
        patientInfo.setActiveStatus(true);

        List<PatientInfo> patientInfoList = Collections.singletonList(patientInfo);
        Page<PatientInfo> patientInfoPage = new PageImpl<>(patientInfoList);

        PatientInfoResponseDto responseDto = new PatientInfoResponseDto();
        responseDto.setCode("PATIENT_CODE_123");
        responseDto.setFullName("Akash");
        responseDto.setAge(30.0F);

        when(repository.findAll(any(Pageable.class))).thenReturn(patientInfoPage);

        Page<PatientInfoResponseDto> result = patientInfoService.getAll(page, size, activeStatus);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals("PATIENT_CODE_123", result.getContent().get(0).getCode());

        verify(repository, times(1)).findAll(any(Pageable.class));

    }

    @Test
    public void testGetAll_EmptyPage() {
        Integer page = 1;
        Integer size = 5;
        Boolean activeStatus = true;

        List<PatientInfo> emptyList = Collections.emptyList();
        Page<PatientInfo> emptyPage = new PageImpl<>(emptyList);

        when(repository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        Page<PatientInfoResponseDto> result = patientInfoService.getAll(page, size, activeStatus);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());

        verify(repository, times(1)).findAll(any(Pageable.class));
    }

}


