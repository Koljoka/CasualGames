package com.example.casualgames.service;

import com.example.casualgames.entity.GameEvent;
import com.example.casualgames.entity.Reservation;
import com.example.casualgames.entity.User;
import com.example.casualgames.repository.GameEventRepository;
import com.example.casualgames.repository.ReservationRepository;
import com.example.casualgames.repository.UserRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;


//Service kerros Reservation logiikkaa varten
@Service
public class ReservationService {

    //Repository tietokantaoperaatioita varten
    private final ReservationRepository reservationRepository;

    //Tapahtumien haku
    private final GameEventRepository gameEventRepository;

    //Käyttäjän haku
    private final UserRepository userRepository;

    //Konstruktori injektoi repositoryt
    public ReservationService(
            ReservationRepository reservationRepository,
            GameEventRepository gameEventRepository,
            UserRepository userRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.gameEventRepository = gameEventRepository;
        this.userRepository = userRepository;
    }

    //Liity tapahtumaan
    public Reservation joinEvent(Long gameEventId) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        GameEvent gameEvent = gameEventRepository.findById(gameEventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        //Estetään tuplailmoittautuminen
        if (reservationRepository.findByUserAndGameEvent(currentUser, gameEvent).isPresent()) {
            throw new RuntimeException("You have already joined this event");
        }

        //Tarkistetaan onko tapahtuma täynnä
        List<Reservation> reservations = reservationRepository.findByGameEvent(gameEvent);

        if (gameEvent.getMaxPlayers() != null && reservations.size() >= gameEvent.getMaxPlayers()) {
            throw new RuntimeException("This event is full");
        }

        Reservation reservation = new Reservation();
        reservation.setUser(currentUser);
        reservation.setGameEvent(gameEvent);
        reservation.setStatus("JOINED");

        return reservationRepository.save(reservation);
    }

    //Laske tapahtuman osallistujat
    public int countReservationsForEvent(GameEvent gameEvent) {
        return reservationRepository.findByGameEvent(gameEvent).size();
    }

    //Tarkista onko käyttäjä jo liittynyt tapahtumaan
    public boolean hasUserJoined(GameEvent gameEvent) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return reservationRepository.findByUserAndGameEvent(currentUser, gameEvent).isPresent();
    }
}