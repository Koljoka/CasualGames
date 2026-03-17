package com.example.casualgames.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;


//Ilmoittautuminen peliin
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Milloin käyttäjä liittyi peliin
    private LocalDateTime joinDate;

    //Ilmoittautumisen tila
    private String status;

    //M:N suhteen toinen puoli -> monta varausta voi kuulua yhdelle käyttäjälle
    @ManyToOne
    private User user;

    //M:N suhteen toinen puoli -> monta varausta voi kuulua yhdelle pelille
    @ManyToOne
    private GameEvent gameEvent;

    //Tyhjä konstruktori
    public Reservation() {
        this.joinDate = LocalDateTime.now();
    }

    //GETTERIT SETTERIT

    public Long getId() {
        return id;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GameEvent getGameEvent() {
        return gameEvent;
    }

    public void setGameEvent(GameEvent gameEvent) {
        this.gameEvent = gameEvent;
    }
}