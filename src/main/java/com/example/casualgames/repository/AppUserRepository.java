package com.example.casualgames.repository;

import com.example.casualgames.entity.AppUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


//Repository hoitaa AppUser tietokantaoperaatiot
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    //Hae käyttäjä käyttäjänimen perusteella
    Optional<AppUser> findByUsername(String username);
}