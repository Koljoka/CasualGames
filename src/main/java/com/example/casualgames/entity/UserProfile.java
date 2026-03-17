package com.example.casualgames.entity;

import jakarta.persistence.*;


//Lisätiedot käyttäjästä 1:1 suhde User entiteettiin
@Entity
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Lyhyt esittely käyttäjästä
    private String bio;

    //Puhelinnumero
    private String phoneNumber;

    //Ikä
    private Integer age;

    //Kuinka monta vuotta harrastanut
    private Integer experienceYears;

    //Suosikkilaji
    private String preferredSport;

    //1:1 suhde Useriin
    @OneToOne
    private User user;

    //Tyhjä konstruktori
    public UserProfile() {}

    //GETTERIT SETTERIT

    public Long getId() {
        return id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getPreferredSport() {
        return preferredSport;
    }

    public void setPreferredSport(String preferredSport) {
        this.preferredSport = preferredSport;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}