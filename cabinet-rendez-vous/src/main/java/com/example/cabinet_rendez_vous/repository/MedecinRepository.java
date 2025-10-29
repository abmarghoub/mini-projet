package com.example.cabinet_rendez_vous.repository;

import com.example.cabinet_rendez_vous.entities.Medecin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedecinRepository extends CrudRepository<Medecin,Long> {
    Optional<Medecin> findByEmail(String email);
}
