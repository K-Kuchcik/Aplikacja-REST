package com.project.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "zadanie")
@EntityListeners(AuditingEntityListener.class)
public class Zadanie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "zadanie_id")
    private Integer zadanieId;

    @NotBlank(message = "Pole nazwa nie może być puste!")
    @Size(min = 3, max = 50, message = "Nazwa musi zawierać od {min} do {max} znaków!")
    @Column(nullable = false, length = 50)
    private String nazwa;

    @NotNull(message = "Pole kolejnosc nie może być puste!")
    @Min(value = 1, message = "Kolejność musi być większa od 0!")
    @Column(nullable = false)
    private Integer kolejnosc;

    @NotBlank(message = "Pole opis nie może być puste!")
    @Size(min = 3, max = 1000, message = "Opis musi zawierać od {min} do {max} znaków!")
    @Column(nullable = false, length = 1000)
    private String opis;

    @Column(name = "dataczas_dodania", nullable = false, updatable = false)
    private LocalDateTime dataCzasDodania;

    @CreatedDate
    @Column(name = "dataczas_utworzenia", nullable = false, updatable = false)
    private LocalDateTime dataczasUtworzenia;

    @LastModifiedDate
    @Column(name = "dataczas_modyfikacji", insertable = false)
    private LocalDateTime dataczasModyfikacji;

    @NotNull(message = "Zadanie musi być przypisane do projektu!")
    @ManyToOne(optional = false)
    @JoinColumn(name = "projekt_id", nullable = false)
    @JsonIgnoreProperties({ "zadania", "studenci" })
    private Projekt projekt;

    public Zadanie() {
    }

    public Zadanie(String nazwa, String opis, Integer kolejnosc) {
        this.nazwa = nazwa;
        this.opis = opis;
        this.kolejnosc = kolejnosc;
    }

    public Zadanie(Integer zadanieId, String nazwa, String opis, Integer kolejnosc) {
        this.zadanieId = zadanieId;
        this.nazwa = nazwa;
        this.opis = opis;
        this.kolejnosc = kolejnosc;
    }

    @PrePersist
    public void prePersist() {
        if (dataCzasDodania == null) {
            dataCzasDodania = LocalDateTime.now();
        }
    }

    public Integer getZadanieId() {
        return zadanieId;
    }

    public void setZadanieId(Integer zadanieId) {
        this.zadanieId = zadanieId;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public Integer getKolejnosc() {
        return kolejnosc;
    }

    public void setKolejnosc(Integer kolejnosc) {
        this.kolejnosc = kolejnosc;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public LocalDateTime getDataCzasDodania() {
        return dataCzasDodania;
    }

    public void setDataCzasDodania(LocalDateTime dataCzasDodania) {
        this.dataCzasDodania = dataCzasDodania;
    }

    public Projekt getProjekt() {
        return projekt;
    }

    public void setProjekt(Projekt projekt) {
        this.projekt = projekt;
    }
}
