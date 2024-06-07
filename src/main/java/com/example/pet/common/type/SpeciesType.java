package com.example.pet.common.type;

import com.example.pet.common.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

import static com.example.pet.common.exception.ErrorCode.WRONG_SPECIES_TYPE;

@Getter
@AllArgsConstructor
public enum SpeciesType {
    DOG("DOG"),
    CAT("CAT");

    private final String value;

    @JsonCreator
    public static SpeciesType fromSpeciesType(String value) {
        return Arrays.stream(values())
                .filter(type -> type.getValue().equals(value))
                .findAny()
                .orElseThrow(() -> new BusinessException(WRONG_SPECIES_TYPE));
    }
}
