package com.example.casualgames.entity;

import jakarta.persistence.*;


//Autentikointia varten oma käyttäjäentiteetti
@Entity
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Kirjautumisessa käytettävä käyttäjänimi
    @Column(unique = true)
    private String username;

    //Hashattu salasana
    private String passwordHash;

    //Käyttäjän rooli
    private String role;

    //Tyhjä konstruktori
    public AppUser() {
    }

    //GETTERIT SETTERIT
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}