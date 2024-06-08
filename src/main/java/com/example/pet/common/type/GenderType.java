package com.example.pet.common.type;

import com.example.pet.common.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

import static com.example.pet.common.exception.ErrorCode.WRONG_GENDER_TYPE;

@Getter
@AllArgsConstructor
public enum GenderType {
    FEMALE("FEMALE"),
    MALE("MALE");

    private final String value;

    @JsonCreator
    public static GenderType fromGenderType(String value) {
        return Arrays.stream(values())
                .filter(type -> type.getValue().equals(value))
                .findAny()
                .orElseThrow(() -> new BusinessException(WRONG_GENDER_TYPE));
    }
}
