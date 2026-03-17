package com.example.casualgames.entity;

import jakarta.persistence.*;


//Pelilaji
@Entity
public class Sport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Lajin nimi
    private String name;

    //Lajin kuvaus
    private String description;

    //Tyhjä konstruktori
    public Sport() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}