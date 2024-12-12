package com.bappi.pms.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;


public enum PatientType {
    EMERGENCY("Emergency"),
    NON_EMERGENCY("Non-Emergency");

    @Getter
    private final String message;

    PatientType(String message) {
        this.message = message;
    }

    private static final Map<String, PatientType> patientTypeMap = new HashMap<>();

    static {
        for (PatientType patientType : PatientType.values()) {
            if(!patientTypeMap.containsKey(patientType.getMessage().toLowerCase())) {
                patientTypeMap.put(patientType.getMessage().toLowerCase(), patientType);
            }
        }
    }

    public static boolean isValidPatientType(String value) {
        try {
            PatientType status = PatientType.valueOf(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
