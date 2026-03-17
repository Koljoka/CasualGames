package com.example.casualgames.service;

import com.example.casualgames.entity.AppUser;
import com.example.casualgames.repository.AppUserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


//Service kerros AppUser logiikkaa varten
@Service
public class AppUserService {

    //Hae kaikki autentikointikäyttäjät
    public List<AppUser> findAllAppUsers() {
        return appUserRepository.findAll();
    }

    //Repository tietokantaoperaatioita varten
    private final AppUserRepository appUserRepository;

    //Salasanan hashaukseen
    private final PasswordEncoder passwordEncoder;

    //Konstruktori injektoi repositoryn ja password encoderin
    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //Hae kaikki käyttäjät
    public List<AppUser> findAllUsers() {
        return appUserRepository.findAll();
    }

    //Hae käyttäjä käyttäjänimen perusteella
    public Optional<AppUser> findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    //Tallennetaan uusi käyttäjä hashatulla salasanalla
    public AppUser registerUser(String username, String rawPassword, String role) {
        AppUser appUser = new AppUser();
        appUser.setUsername(username);
        appUser.setPasswordHash(passwordEncoder.encode(rawPassword));
        appUser.setRole(role);

        return appUserRepository.save(appUser);
    }

    //Tarkistus löytyykö käyttäjänimi jo
    public boolean usernameExists(String username) {
        return appUserRepository.findByUsername(username).isPresent();
    }
}