package com.project.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.EntityListeners;


@Entity
@Table(name = "projekt")
@EntityListeners(AuditingEntityListener.class)
public class Projekt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "projekt_id")
    private Integer projektId;

    @NotBlank(message = "Pole nazwa nie może być puste!")
    @Size(min = 3, max = 50, message = "Nazwa musi zawierać od {min} do {max} znaków!")
    @Column(nullable = false, length = 50)
    private String nazwa;

    @NotBlank(message = "Pole opis nie może być puste!")
    @Size(min = 3, max = 1000, message = "Opis musi zawierać od {min} do {max} znaków!")
    @Column(nullable = false, length = 1000)
    private String opis;

    @Column(name = "dataczas_utworzenia", nullable = false, updatable = false)
    private LocalDateTime dataCzasUtworzenia;

    @Column(name = "dataczas_modyfikacji")
    private LocalDateTime dataCzasModyfikacji;

    @Column(name = "data_oddania")
    private LocalDate dataOddania;

    @OneToMany(mappedBy = "projekt")
    @JsonIgnoreProperties({ "projekt" })
    private List<Zadanie> zadania = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "projekt_student", joinColumns = {
            @JoinColumn(name = "projekt_id") }, inverseJoinColumns = { @JoinColumn(name = "student_id") })
    @JsonIgnoreProperties({ "projekty" })
    private Set<Student> studenci = new HashSet<>();


    public Projekt() {
    }

    public Projekt(String nazwa, String opis) {
        this.nazwa = nazwa;
        this.opis = opis;
    }

    public Projekt(String nazwa, String opis, LocalDate dataOddania) {
        this.nazwa = nazwa;
        this.opis = opis;
        this.dataOddania = dataOddania;
    }

    public Projekt(Integer projektId, String nazwa, String opis, LocalDate dataOddania) {
        this.projektId = projektId;
        this.nazwa = nazwa;
        this.opis = opis;
        this.dataOddania = dataOddania;
    }

    public Projekt(Integer projektId, String nazwa, String opis, LocalDateTime dataCzasUtworzenia,
            LocalDate dataOddania) {
        this.projektId = projektId;
        this.nazwa = nazwa;
        this.opis = opis;
        this.dataCzasUtworzenia = dataCzasUtworzenia;
        this.dataOddania = dataOddania;
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime teraz = LocalDateTime.now();
        if (dataCzasUtworzenia == null) {
            dataCzasUtworzenia = teraz;
        }
        dataCzasModyfikacji = teraz;
    }

    @PreUpdate
    public void preUpdate() {
        dataCzasModyfikacji = LocalDateTime.now();
    }

    public Integer getProjektId() {
        return projektId;
    }

    public void setProjektId(Integer projektId) {
        this.projektId = projektId;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public LocalDateTime getDataCzasUtworzenia() {
        return dataCzasUtworzenia;
    }

    public void setDataCzasUtworzenia(LocalDateTime dataCzasUtworzenia) {
        this.dataCzasUtworzenia = dataCzasUtworzenia;
    }

    public LocalDateTime getDataCzasModyfikacji() {
        return dataCzasModyfikacji;
    }

    public void setDataCzasModyfikacji(LocalDateTime dataCzasModyfikacji) {
        this.dataCzasModyfikacji = dataCzasModyfikacji;
    }

    public LocalDate getDataOddania() {
        return dataOddania;
    }

    public void setDataOddania(LocalDate dataOddania) {
        this.dataOddania = dataOddania;
    }

    public List<Zadanie> getZadania() {
        return zadania;
    }

    public void setZadania(List<Zadanie> zadania) {
        this.zadania = zadania;
    }

    public Set<Student> getStudenci() {
        return studenci;
    }

    public void setStudenci(Set<Student> studenci) {
        this.studenci = studenci;
    }
}
