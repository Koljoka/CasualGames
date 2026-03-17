package com.example.casualgames.repository;

import com.example.casualgames.entity.User;
import com.example.casualgames.entity.UserProfile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


//Repository hoitaa UserProfile tietokantaoperaatiot
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    //Hae profiili käyttäjän perusteella
    Optional<UserProfile> findByUser(User user);
}