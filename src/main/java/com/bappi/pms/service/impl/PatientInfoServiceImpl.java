package com.bappi.pms.service.impl;

import com.bappi.pms.model.dto.PatientInfoRequestDto;
import com.bappi.pms.model.dto.PatientInfoResponseDto;
import com.bappi.pms.model.entity.PatientInfo;
import com.bappi.pms.model.enums.Gender;
import com.bappi.pms.model.enums.PatientType;
import com.bappi.pms.repository.PatientInfoRepository;
import com.bappi.pms.service.PatientInfoService;
import com.bappi.pms.utils.mapper.PatientInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

import static com.bappi.pms.config.Constant.PATIENT_CODE_PREFIX;

@Slf4j
@Service
public class PatientInfoServiceImpl implements PatientInfoService {


    private final PatientInfoRepository repository;
    private final PatientInfoMapper objectMapper;

    @Autowired
    public PatientInfoServiceImpl(PatientInfoRepository repository) {
        this.repository = repository;
        this.objectMapper = Mappers.getMapper(PatientInfoMapper.class);
    }

    @Override
    public PatientInfoResponseDto save(PatientInfoRequestDto requestDto) {
        // Convert requestDto to PatientInfo
        PatientInfo patientInfo = objectMapper.mapDtoToObj(requestDto);
        log.debug("Mapped requestDto to patientInfo: {}", patientInfo); // Check mapping

        int uniqueId = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        String patientCode = PATIENT_CODE_PREFIX + uniqueId;
        patientInfo.setCode(patientCode);
        patientInfo.setActiveStatus(true);
        patientInfo.setCreateDate(new Timestamp(System.currentTimeMillis()));
        patientInfo.setCreateBy(1L);

        // Save PatientInfo and map back to responseDto
        PatientInfo savedPatientInfo = repository.save(patientInfo);
        log.debug("Saved patientInfo: {}", savedPatientInfo);

        PatientInfoResponseDto responseDto = objectMapper.mapObjToDto(savedPatientInfo);
        log.debug("Mapped saved patientInfo to responseDto: {}", responseDto);

        return responseDto;
    }

    @Override
    public PatientInfoResponseDto update(PatientInfoRequestDto requestDto) {
        PatientInfoResponseDto responseDto = null;
        String code = requestDto.getCode();
        log.debug("Update Patient Info details by code {} " , code);

        PatientInfo existingPatient = repository.findByCode(code);
        if(existingPatient == null) {
            log.error("No Patient Information Found");
            return responseDto;
        }
        existingPatient.setFullName(requestDto.getFullName());
        existingPatient.setAge(requestDto.getAge());
        existingPatient.setGender(Gender.valueOf(requestDto.getGender()));
        existingPatient.setPatientType(PatientType.valueOf(requestDto.getPatientType()));
        existingPatient.setContactNo(requestDto.getContactNo());
        existingPatient.setAddress(requestDto.getAddress());

        existingPatient.setUpdateBy(1L);
        existingPatient.setUpdateDate(new Timestamp(System.currentTimeMillis()));

        PatientInfo savedObj = repository.save(existingPatient);

        return  objectMapper.mapObjToDto(repository.save(savedObj));

    }

    @Override
    public PatientInfoResponseDto get(String code) {
        log.debug("Get Patient Info details by code {} " , code);
        PatientInfoResponseDto responseDto = new PatientInfoResponseDto();

        PatientInfo patientInfo = repository.findByCode(code);

        if(patientInfo == null){
            log.error("Patient code is invalid");
            return responseDto;
        }
        return objectMapper.mapObjToDto(patientInfo);
    }

    @Override
    public PatientInfo getByCode(String code) {
        return repository.findByCode(code);
    }

    @Override
    public Page<PatientInfoResponseDto> getAll(Integer page, Integer size, Boolean activeStatus) {
        log.info("=> get patient list request: page: {}, size: {},activeStatus: {}", page, size,activeStatus);

        page = (page == null || page < 1) ? 0 : page - 1;
        size = (size == null || size < 1) ? 10 : size;

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "code"));

        Page<PatientInfo> patientInfoPages = repository.findAll(pageable);

        return objectMapper.map(patientInfoPages);
    }

}
