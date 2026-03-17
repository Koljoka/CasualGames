package com.example.casualgames.repository;

import com.example.casualgames.entity.Sport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


//Repository hoitaa Sport tietokantaoperaatiot
@Repository
public interface SportRepository extends JpaRepository<Sport, Long> {

    //Hae laji nimen perusteella
    Optional<Sport> findByName(String name);
}