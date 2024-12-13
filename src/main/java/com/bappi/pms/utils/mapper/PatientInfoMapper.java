package com.bappi.pms.utils.mapper;

import com.bappi.pms.model.dto.PatientInfoRequestDto;
import com.bappi.pms.model.dto.PatientInfoResponseDto;
import com.bappi.pms.model.entity.PatientInfo;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;


@Mapper
public interface PatientInfoMapper {

    PatientInfo map(PatientInfoRequestDto dto);

    PatientInfoResponseDto map(PatientInfo obj);

    default Page<PatientInfoResponseDto> map(Page<PatientInfo> page) {
        List<PatientInfoResponseDto> responseDtos = page.getContent().stream()
                .map(this::map)
                .toList();

        return new PageImpl<>(responseDtos, page.getPageable(), page.getTotalElements());
    }

}
