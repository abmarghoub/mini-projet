package com.example.cabinet_rendez_vous.entities;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Medecin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nom;
    private String specialite;

    private String email;
    private String password;

    @OneToMany(mappedBy = "medecin", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<RendezVous> rendezVous = new ArrayList<>();

    public Medecin() {
    }

    public Medecin(String nom, String specialite, String email, String password) {
        this.nom = nom;
        this.specialite = specialite;
        this.email = email;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public List<RendezVous> getRendezVous() {
        return rendezVous;
    }

    public void setRendezVous(List<RendezVous> rendezVous) {
        this.rendezVous = rendezVous;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Medecin{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", specialite='" + specialite + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", rendezVous=" + rendezVous +
                '}';
    }
}
