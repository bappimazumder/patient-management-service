package com.bappi.pms.service;

import com.bappi.pms.model.dto.PatientInfoRequestDto;
import com.bappi.pms.model.dto.PatientInfoResponseDto;
import com.bappi.pms.model.entity.PatientInfo;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PatientInfoService {

    PatientInfoResponseDto save(PatientInfoRequestDto requestDto);

    PatientInfoResponseDto update(PatientInfoRequestDto requestDto);

    PatientInfoResponseDto get(String code);

    PatientInfo getByCode(String code);

    Page<PatientInfoResponseDto> getAll(Integer page, Integer size, Boolean activeStatus);
}
