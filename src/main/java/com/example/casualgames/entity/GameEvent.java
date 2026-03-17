package com.example.casualgames.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;


//Yksittäinen pelitapahtuma
@Entity
public class GameEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Tapahtuman nimi
    private String title;

    //Missä pelataan
    private String city;

    //Tarkempi pelipaikka
    private String location;

    //Pelin ajankohta
    private LocalDateTime eventDate;

    //Kuinka monta pelaajaa mahtuu
    private Integer maxPlayers;

    //Kuvaus pelistä
    private String description;

    //Tapahtuman järjestäjä
    @ManyToOne
    private User organizer;

    //1:N suhde -> monta GameEventiä kuuluu yhteen Sportiin
    @ManyToOne
    private Sport sport;

    //Tyhjä konstruktori
    public GameEvent() {
    }

    //GETTERIT SETTERIT


    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }
}

