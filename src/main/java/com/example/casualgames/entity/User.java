package com.example.casualgames.entity;

//JPA importit tietokantaa varten
import jakarta.persistence.*;

//Validointi
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

//Päivämäärää varten
import java.time.LocalDateTime;


@Entity
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Ei tyhjiä
    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    private String city;

    @NotBlank
    private String skillLevel;


    //Tallennetaan automaattisesti käyttäjän luontiaika.
    private LocalDateTime createdAt;

    //Tyhjä konstruktori.
    public User() {
        this.createdAt = LocalDateTime.now();
    }

    //GETTERIT SETTERIT
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}