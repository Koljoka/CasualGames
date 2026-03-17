package com.example.casualgames.service;

import com.example.casualgames.entity.User;
import com.example.casualgames.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

/*
 * Service kerros sisältää sovelluksen liiketoimintalogiikan.
 * UI ei käytä repositorya suoraan vaan tämän kautta.
 */
@Service
public class UserService {

    //Repository tietokantaoperaatioita varten
    private final UserRepository userRepository;

    //Luodaan uusi domain käyttäjä rekisteröitymisen yhteydessä
    //Luodaan uusi domain käyttäjä rekisteröitymisen yhteydessä
    public User createDomainUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setName(username);
        user.setEmail(username + "@example.com");
        user.setCity("Not set");
        user.setSkillLevel("Not set");

        return userRepository.save(user);
    }

    //Tarkistus löytyykö domain käyttäjä jo
    public boolean domainUserExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    //Konstruktorin kautta injektoidaan repository, Spring luo automatic
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Hae kaikki käyttäjät tietokannasta
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    //Tallenna käyttäjä tietokantaan
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    //Hae käyttäjä käyttäjänimen perusteella
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    //Poista käyttäjä ID:n perusteella
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}

