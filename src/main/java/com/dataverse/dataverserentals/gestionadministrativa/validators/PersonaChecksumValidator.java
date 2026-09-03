package com.dataverse.dataverserentals.gestionadministrativa.validators;

import org.springframework.stereotype.Component;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Persona;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.PersonaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PersonaChecksumValidator {

    private final PersonaRepository personaRepository;

    public boolean existsByChecksum(Persona persona) {
        return switch (persona.getTipoPersonaCode()) {
            case NATURAL_SIN_NEGOCIO ->
                personaRepository.existsByPersonaNaturalChecksum(persona.getPersonaNaturalChecksum());
            case NATURAL_CON_NEGOCIO ->
                personaRepository.existsByPersonaNaturalNegocioChecksum(persona.getPersonaNaturalNegocioChecksum());
            case JURIDICA ->
                personaRepository.existsByPersonaJuridicaChecksum(persona.getPersonaJuridicaChecksum());
        };
    }

    public Long countByChecksum(Persona persona) {
        return switch (persona.getTipoPersonaCode()) {
            case NATURAL_SIN_NEGOCIO ->
                personaRepository.countByPersonaNaturalChecksumAndIdNot(persona.getPersonaNaturalChecksum(),
                        persona.getId());
            case NATURAL_CON_NEGOCIO ->
                personaRepository.countByPersonaNaturalNegocioChecksumAndIdNot(
                        persona.getPersonaNaturalNegocioChecksum(), persona.getId());
            case JURIDICA ->
                personaRepository.countByPersonaJuridicaChecksumAndIdNot(persona.getPersonaJuridicaChecksum(),
                        persona.getId());
        };
    }
}