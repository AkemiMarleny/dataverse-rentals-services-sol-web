package com.dataverse.dataverserentals.core.enums;

import java.util.Arrays;
import java.util.Optional;

import com.dataverse.dataverserentals.common.exceptions.FieldConstraintException;

public class EnumFactory {

    public static <T extends Enum<T> & Codificable> Optional<T> fromCode(Class<T> enumClass, String code) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter((e -> e.getCode().equals(code)))
                .findFirst();
    }

    public static <T extends Enum<T> & Codificable> T tryFromCode(Class<T> enumClass, String code)
            throws FieldConstraintException {
        return EnumFactory.fromCode(enumClass, code)
                .orElseThrow(() -> new FieldConstraintException(
                        enumClass.getSimpleName(),
                        String.format("No se encontró el valor %s", code)));
    }

}