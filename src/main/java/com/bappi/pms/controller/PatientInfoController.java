package com.bappi.pms.controller;

import com.bappi.pms.model.dto.PatientInfoRequestDto;
import com.bappi.pms.model.dto.PatientInfoResponseDto;
import com.bappi.pms.model.dto.RequestValidatorResponseDto;
import com.bappi.pms.model.entity.PatientInfo;
import com.bappi.pms.model.enums.Gender;
import com.bappi.pms.model.enums.PatientType;
import com.bappi.pms.service.PatientInfoService;
import com.bappi.pms.utils.ApiResponse;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.bappi.pms.config.ApiPath.*;
import static com.bappi.pms.config.Constant.*;

@Slf4j
@RestController
@RequestMapping(API_PATIENT_INFO)
public class PatientInfoController {

    private final PatientInfoService service;

    @Autowired
    public PatientInfoController(PatientInfoService service) {
        this.service = service;
    }

    @PostMapping(value = API_CREATE_PATIENT)
    public ResponseEntity<ApiResponse<PatientInfoResponseDto>> create(@RequestBody PatientInfoRequestDto requestDto) {
        // Log incoming request details
        log.info("Creating patient info with request: {}", requestDto);

        // Validate the request
        RequestValidatorResponseDto requestValidator = requestValidatorForCreate(requestDto);
        if (requestValidator.getStatus().equals(FAILED_STATUS)) {
            return buildErrorResponse(requestValidator.getMessage(), HttpStatus.BAD_REQUEST);
        }

        // Perform the save operation
        PatientInfoResponseDto responseDto = service.save(requestDto);
        return buildSuccessResponse(CREATED_SUCCESSFULLY_MESSAGE, responseDto);
    }



    @PutMapping(value = API_UPDATE_PATIENT)
    public ResponseEntity<ApiResponse<PatientInfoResponseDto>> update(@RequestBody PatientInfoRequestDto requestDto) {
        // Log incoming request details
        log.info("Updating patient info with request: {}", requestDto);

        // Validate the request
        RequestValidatorResponseDto requestValidator = requestValidatorForUpdate(requestDto);
        if (requestValidator.getStatus().equals(FAILED_STATUS)) {
            return buildErrorResponse(requestValidator.getMessage(), HttpStatus.BAD_REQUEST);
        }

        // Perform the update and return success
        PatientInfoResponseDto responseDto = service.update(requestDto);
        return buildSuccessResponse(UPDATED_SUCCESSFULLY_MESSAGE, responseDto);
    }

    @GetMapping(value = API_GET_PATIENT)
    public ResponseEntity<ApiResponse<PatientInfoResponseDto>> getDetails(@RequestParam(value = "code") String code) {
        // Validate the input code
        if (code == null || code.trim().isEmpty()) {
            log.error("Get patient details failed, code is null or empty");
            return buildErrorResponse(INVALID_CODE_MESSAGE, HttpStatus.BAD_REQUEST);
        }

        // Get patient details
        PatientInfoResponseDto responseDto = service.get(code);
        log.info("Patient details to return: " + responseDto);

        // Check if responseDto is null or has an invalid code
        if (responseDto == null || responseDto.getCode() == null) {
            return buildErrorResponse(GET_FAILED_MESSAGE, HttpStatus.NOT_FOUND);
        }

        // Return success response
        return buildSuccessResponse(GET_SUCCESSFULLY_MESSAGE, responseDto);
    }

    @GetMapping(value = API_GET_ALL_PATIENT)
    public ResponseEntity<ApiResponse<List<PatientInfoResponseDto>>> getAllPatientList(
            @Nullable @RequestParam(value = "page") Integer pageNo,
            @Nullable @RequestParam(value = "size") Integer size,
            @Nullable @RequestParam(value = "activeStatus") Boolean activeStatus
    ){

        log.info("Get All Patient list with " + "pageNo: " + pageNo + ", size: " + size + ", status: " + activeStatus);
        Page<PatientInfoResponseDto> patientPage =  service.getAll(pageNo, size, activeStatus);

        ApiResponse<List<PatientInfoResponseDto>> response = new ApiResponse<>(
                SUCCESS_STATUS,
                GET_SUCCESSFULLY_MESSAGE,
                patientPage.getContent(),
                Map.of(
                        "currentPage", patientPage.getNumber(),
                        "totalPages", patientPage.getTotalPages(),
                        "totalItems", patientPage.getTotalElements()
                )
        );

        return ResponseEntity.ok(response);
    }



    private ResponseEntity<ApiResponse<PatientInfoResponseDto>> buildSuccessResponse(String message, PatientInfoResponseDto data) {
        ApiResponse<PatientInfoResponseDto> response = new ApiResponse<>(SUCCESS_STATUS, message, data, null);
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<ApiResponse<PatientInfoResponseDto>> buildErrorResponse(String message, HttpStatus status) {
        ApiResponse<PatientInfoResponseDto> response = new ApiResponse<>(FAILED_STATUS, message, null, null);
        return ResponseEntity.status(status).body(response);
    }


    private RequestValidatorResponseDto requestValidatorForCreate(PatientInfoRequestDto requestDto) {
        // Validate age
        if (isInvalidAge(requestDto.getAge())) {
            return createErrorResponse(INVALID_AGE_MESSAGE);
        }

        // Validate patient type
        if (isInvalidPatientType(requestDto.getPatientType())) {
            return createErrorResponse(INVALID_PATIENT_TYPE_MESSAGE);
        }

        // Validate gender
        if (isInvalidGender(requestDto.getGender())) {
            return createErrorResponse(INVALID_GENDER_MESSAGE);
        }

        // If all validations passed
        return new RequestValidatorResponseDto(SUCCESS_STATUS, "");
    }

    private RequestValidatorResponseDto requestValidatorForUpdate(PatientInfoRequestDto requestDto) {
        // Validate Code
        if (isInvalidCode(requestDto.getCode())) {
            return createErrorResponse(INVALID_CODE_MESSAGE);
        }

        PatientInfo patientInfo = service.getByCode(requestDto.getCode());
        if (patientInfo == null) {
            return createErrorResponse(INVALID_CODE_NOT_FOUND_MESSAGE);
        }

        // Validate age
        if (isInvalidAge(requestDto.getAge())) {
            return createErrorResponse(INVALID_AGE_MESSAGE);
        }

        // Validate patient type
        if (isInvalidPatientType(requestDto.getPatientType())) {
            return createErrorResponse(INVALID_PATIENT_TYPE_MESSAGE);
        }

        // Validate gender
        if (isInvalidGender(requestDto.getGender())) {
            return createErrorResponse(INVALID_GENDER_MESSAGE);
        }

        // If all validations passed
        return new RequestValidatorResponseDto(SUCCESS_STATUS, "");
    }

    private boolean isInvalidCode(String code) {
        return code == null || code.isEmpty();
    }

    private boolean isInvalidAge(Float age) {
        return age == null || age.isNaN() || age < 0.1;
    }

    private boolean isInvalidPatientType(String patientType) {
        return patientType == null || !PatientType.isValidPatientType(patientType);
    }

    private boolean isInvalidGender(String gender) {
        return gender == null || !Gender.isValidGender(gender);
    }

    private RequestValidatorResponseDto createErrorResponse(String errorMessage) {
        log.error(errorMessage);
        return new RequestValidatorResponseDto(FAILED_STATUS, errorMessage);
    }

}
