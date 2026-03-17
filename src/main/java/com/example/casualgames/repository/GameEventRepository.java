package com.example.casualgames.repository;

import com.example.casualgames.entity.GameEvent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


//Repository hoitaa GameEvent tietokantaoperaatiot
@Repository
public interface GameEventRepository extends JpaRepository<GameEvent, Long>, JpaSpecificationExecutor<GameEvent> {

}