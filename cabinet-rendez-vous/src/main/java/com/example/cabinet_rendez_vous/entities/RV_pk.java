package com.example.cabinet_rendez_vous.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Embeddable
public class RV_pk implements Serializable {

    private long patientId;
    private long medecinId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateHeure;

    // Getters/Setters/Constructors/ToString...

    public RV_pk() {
    }

    public RV_pk(long patientId, long medecinId, Date dateHeure) {
        this.patientId = patientId;
        this.medecinId = medecinId;
        this.dateHeure = dateHeure;
    }

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public long getMedecinId() {
        return medecinId;
    }

    public void setMedecinId(long medecinId) {
        this.medecinId = medecinId;
    }

    public Date getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(Date dateHeure) {
        this.dateHeure = dateHeure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RV_pk rv_pk = (RV_pk) o;
        return patientId == rv_pk.patientId && medecinId == rv_pk.medecinId && Objects.equals(dateHeure, rv_pk.dateHeure);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patientId, medecinId, dateHeure);
    }

    @Override
    public String toString() {
        return "RV_pk{" +
                "patientId=" + patientId +
                ", medecinId=" + medecinId +
                ", dateHeure=" + dateHeure +
                '}';
    }
}
