package com.bappi.pms.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;


public enum Gender {
    MALE("Male"),
    FEMALE("Female"),
    OTHERS("Others")
    ;

    @Getter
    private final String message;

    Gender(String message) {
        this.message = message;
    }

    private static final Map<String, Gender> genderMap = new HashMap<>();

    static {
        for (Gender gender : Gender.values()) {
            if(!genderMap.containsKey(gender.getMessage().toLowerCase())) {
                genderMap.put(gender.getMessage().toLowerCase(), gender);
            }
        }
    }

    public static boolean isValidGender(String value) {
        try {
            Gender status = Gender.valueOf(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
