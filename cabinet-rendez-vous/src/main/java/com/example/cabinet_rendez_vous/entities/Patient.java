package com.example.cabinet_rendez_vous.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nom;
    private String cin;
    private LocalDate dateNaissance;
    private boolean assurance;

    private String email;
    private String password;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<RendezVous> rendezVous = new ArrayList<>();

    public Patient() {
    }

    public Patient(String nom, String cin, LocalDate dateNaissance, boolean assurance, String email, String password) {
        this.nom = nom;
        this.cin = cin;
        this.dateNaissance = dateNaissance;
        this.assurance = assurance;
        this.email = email;
        this.password = password;
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

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public boolean isAssurance() {
        return assurance;
    }

    public void setAssurance(boolean assurance) {
        this.assurance = assurance;
    }

    public List<RendezVous> getRendezVous() {
        return rendezVous;
    }

    public void setRendezVous(List<RendezVous> rendezVous) {
        this.rendezVous = rendezVous;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", cin='" + cin + '\'' +
                ", dateNaissance=" + dateNaissance +
                ", assurance=" + assurance +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", rendezVous=" + rendezVous +
                '}';
    }
}
