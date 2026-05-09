package com.project.model;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "student",
        indexes = {
                @Index(name = "idx_nazwisko", columnList = "nazwisko", unique = false),
                @Index(name = "idx_email", columnList = "email", unique = true),
                @Index(name = "idx_nr_indeksu", columnList = "nr_indeksu", unique = true)
        })
public class Student implements UserDetails {

    @Id
    @GeneratedValue
    @Column(name = "student_id")
    private Integer studentId;

    private String imie;

    private String nazwisko;

    @Column(name = "nr_indeksu", unique = true)
    private String nrIndeksu;

    @NotEmpty(message = "Nie podano adresu e-mail")
    @Email(message = "Niepoprawny format adresu e-mail")
    @Column(length = 100, nullable = false, unique = true)
    private String email;

    private Boolean stacjonarny;

    @JsonProperty(access = Access.WRITE_ONLY)
    @Size(min = 8, max = 64, message = "Hasło musi składać się z przynajmniej {min} i nie przekraczać {max} znaków")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "student_role",
            joinColumns = {@JoinColumn(name = "student_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roles;

    @JsonIgnoreProperties({"studenci"})
    @ManyToMany(mappedBy = "studenci")
    private Set<Projekt> projekty;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles
                .stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}