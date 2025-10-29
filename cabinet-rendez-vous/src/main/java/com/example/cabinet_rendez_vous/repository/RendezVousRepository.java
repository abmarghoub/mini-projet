package com.example.cabinet_rendez_vous.repository;

import com.example.cabinet_rendez_vous.entities.RV_pk;
import com.example.cabinet_rendez_vous.entities.RendezVous;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface RendezVousRepository extends CrudRepository<RendezVous, RV_pk> {

    @Query("SELECT r FROM RendezVous r WHERE r.rv_pk.patientId = :patientId AND r.rv_pk.medecinId = :medecinId AND r.rv_pk.dateHeure = :dateHeure")
    Optional<RendezVous> findByCompositeKey(@Param("patientId") Long patientId,
                                            @Param("medecinId") Long medecinId,
                                            @Param("dateHeure") Date dateHeure);
}
