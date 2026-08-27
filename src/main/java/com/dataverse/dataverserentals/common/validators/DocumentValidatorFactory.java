package com.dataverse.dataverserentals.common.validators;

import org.springframework.stereotype.Component;

import com.dataverse.dataverserentals.administrations.enums.TipoDocumento;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class DocumentValidatorFactory {

    private final DniValidator dniValidator;
    private final RucValidator rucValidator;
    private final CarnetExtranjeriaValidator carnetExtranjeriaValidator;

    public DocumentValidator getValidator(TipoDocumento tipoDocumento) {
        return switch (tipoDocumento) {
            case DNI -> dniValidator;
            case RUC -> rucValidator;
            case CARNET_EXTRANJERIA -> carnetExtranjeriaValidator;
            default -> throw new IllegalArgumentException(
                    String.format("No existe validador para el tipo de documento: ", tipoDocumento));
        };
    }
}