package com.example.cabinet_rendez_vous.entities;


import jakarta.persistence.*;

import java.util.Date;

@Entity
public class RendezVous {

    @EmbeddedId
    private RV_pk rv_pk;

    private String nom;
    private String statut;
    private String motif;

    @MapsId("patientId")
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @MapsId("medecinId")
    @ManyToOne
    @JoinColumn(name = "medecin_id")
    private Medecin medecin;

    public RendezVous() {
    }

    public RendezVous(String nom, String statut, String motif, Patient patient, Medecin medecin, Date dateHeure) {
        this.nom = nom;
        this.statut = statut;
        this.motif = motif;
        this.patient = patient;
        this.medecin = medecin;
        this.rv_pk = new RV_pk(patient.getId(), medecin.getId(), dateHeure);
    }

    public RV_pk getRv_pk() {
        return rv_pk;
    }

    public void setRv_pk(RV_pk rv_pk) {
        this.rv_pk = rv_pk;
    }


    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    @Override
    public String toString() {
        return "RendezVous{" +
                "rv_pk=" + rv_pk +
                ", nom='" + nom + '\'' +
                ", statut='" + statut + '\'' +
                ", motif='" + motif + '\'' +
                '}';
    }
}
