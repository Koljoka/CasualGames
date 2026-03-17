package com.example.casualgames.repository;

import com.example.casualgames.entity.GameEvent;
import com.example.casualgames.entity.Reservation;
import com.example.casualgames.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


//Repository hoitaa Reservation tietokantaoperaatiot
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    //Hae ilmoittautuminen käyttäjän ja tapahtuman perusteella
    Optional<Reservation> findByUserAndGameEvent(User user, GameEvent gameEvent);

    //Hae kaikki tapahtuman ilmoittautumiset
    List<Reservation> findByGameEvent(GameEvent gameEvent);
}