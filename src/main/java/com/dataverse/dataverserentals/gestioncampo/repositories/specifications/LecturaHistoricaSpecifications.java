package com.dataverse.dataverserentals.gestioncampo.repositories.specifications;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Medidor;
import com.dataverse.dataverserentals.gestioncampo.entities.LecturaHistorica;

import jakarta.persistence.criteria.Join;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LecturaHistoricaSpecifications {

    public static Specification<LecturaHistorica> byMedidorNumeroSerie(String medidorNumeroSerie) {
        if (medidorNumeroSerie == null || medidorNumeroSerie.trim().isEmpty()) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) -> {
            Join<LecturaHistorica, Medidor> lecturaHistoricaMedidor = root.join("medidor");
            return cb.equal(lecturaHistoricaMedidor.get("numeroSerie"), medidorNumeroSerie);
        };
    }

    public static Specification<LecturaHistorica> byFechaLecturaBetween(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) -> cb.between(root.get("fechaLectura"), desde, hasta);
    }

    public static Specification<LecturaHistorica> filterBy(String medidorNumeroSerie, LocalDate desde,
            LocalDate hasta) {
        return Specification.where(byMedidorNumeroSerie(medidorNumeroSerie))
                .and(byFechaLecturaBetween(desde, hasta));
    }
}
