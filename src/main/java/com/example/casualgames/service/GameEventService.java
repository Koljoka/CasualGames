package com.example.casualgames.service;

import com.example.casualgames.entity.AppUser;
import com.example.casualgames.entity.GameEvent;
import com.example.casualgames.entity.User;
import com.example.casualgames.repository.GameEventRepository;
import com.example.casualgames.repository.GameEventSpecification;
import com.example.casualgames.repository.UserRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


//Service kerros GameEvent logiikkaa varten
@Service
public class GameEventService {

    //Repository tietokantaoperaatioita varten
    private final GameEventRepository gameEventRepository;

    //Käyttäjän haku käyttäjänimellä
    private final UserRepository userRepository;

    //Autentikointikäyttäjän haku roolia varten
    private final AppUserService appUserService;

    //Konstruktori injektoi repositoryt
    public GameEventService(
            GameEventRepository gameEventRepository,
            UserRepository userRepository,
            AppUserService appUserService
    ) {
        this.gameEventRepository = gameEventRepository;
        this.userRepository = userRepository;
        this.appUserService = appUserService;
    }

    //Hae kaikki pelit
    public List<GameEvent> findAllEvents() {
        return gameEventRepository.findAll();
    }

    //Hae peli id:n perusteella
    public Optional<GameEvent> findById(Long id) {
        return gameEventRepository.findById(id);
    }

    //Hae pelit hakuehtojen perusteella
    public List<GameEvent> searchEvents(
            String keyword,
            String city,
            String sportName,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {

        Specification<GameEvent> spec =
                GameEventSpecification.filterEvents(
                        keyword,
                        city,
                        sportName,
                        startDate,
                        endDate
                );

        return gameEventRepository.findAll(spec);
    }

    //Tallenna uusi peli kirjautuneelle käyttäjälle
    public GameEvent createEvent(GameEvent gameEvent) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Käyttäjää ei löytynyt"));

        gameEvent.setOrganizer(currentUser);

        return gameEventRepository.save(gameEvent);
    }

    //Päivitä olemassa oleva peli
    public GameEvent updateEvent(GameEvent updatedEvent) {

        GameEvent existingEvent = gameEventRepository.findById(updatedEvent.getId())
                .orElseThrow(() -> new RuntimeException("Tapahtumaa ei löytynyt"));

        if (!canModifyEvent(existingEvent)) {
            throw new RuntimeException("Sinulla ei ole oikeutta muokata tätä tapahtumaa");
        }

        existingEvent.setTitle(updatedEvent.getTitle());
        existingEvent.setCity(updatedEvent.getCity());
        existingEvent.setLocation(updatedEvent.getLocation());
        existingEvent.setEventDate(updatedEvent.getEventDate());
        existingEvent.setMaxPlayers(updatedEvent.getMaxPlayers());
        existingEvent.setDescription(updatedEvent.getDescription());
        existingEvent.setSport(updatedEvent.getSport());

        return gameEventRepository.save(existingEvent);
    }

    //Poista peli käyttöoikeuksien mukaan
    public void deleteEvent(Long id) {

        GameEvent gameEvent = gameEventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tapahtumaa ei löytynyt"));

        if (!canModifyEvent(gameEvent)) {
            throw new RuntimeException("Sinulla ei ole oikeutta poistaa tätä tapahtumaa");
        }

        gameEventRepository.delete(gameEvent);
    }

    //Tarkistetaan saako käyttäjä muokata tai poistaa tapahtuman
    public boolean canModifyEvent(GameEvent gameEvent) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<AppUser> appUserOptional = appUserService.findByUsername(username);

        if (appUserOptional.isPresent() && "ADMIN".equals(appUserOptional.get().getRole())) {
            return true;
        }

        return gameEvent.getOrganizer() != null
                && gameEvent.getOrganizer().getUsername().equals(username);
    }
}