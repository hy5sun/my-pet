package com.example.pet.common.type;

import com.example.pet.common.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

import static com.example.pet.common.exception.ErrorCode.*;

@Getter
@AllArgsConstructor
public enum ItemCategoryType {
    DOG_FOOD("DOG_FOOD"),
    DOG_SNACKS("DOG_SNACKS"),
    DOG_SUPPLIES("DOG_SUPPLIES"),
    DOG_BEST_ITEMS("DOG_BEST_ITEMS"),
    CAT_FOOD("CAT_FOOD"),
    CAT_SNACKS("CAT_SNACKS"),
    CAT_SUPPLIES("CAT_SUPPLIES"),
    CAT_BEST_ITEMS("CAT_BEST_ITEMS");

    private final String value;

    @JsonCreator
    public static ItemCategoryType fromCategoryType(String value) {
        return Arrays.stream(values())
                .filter(type -> type.getValue().equals(value))
                .findAny()
                .orElseThrow(() -> new BusinessException(WRONG_CATEGORY_TYPE));
    }
}
