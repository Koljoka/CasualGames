package com.example.casualgames.repository;

import com.example.casualgames.entity.User;

//Spring Data JPA
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


//Repository hoitaa tietokantaoperaatiot
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //Hae käyttäjä käyttäjänimen perusteella
    Optional<User> findByUsername(String username);
}