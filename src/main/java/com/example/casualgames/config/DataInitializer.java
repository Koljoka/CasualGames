package com.example.casualgames.config;

import com.example.casualgames.entity.AppUser;
import com.example.casualgames.service.AppUserService;
import com.example.casualgames.service.UserService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.casualgames.entity.Sport;
import com.example.casualgames.repository.SportRepository;


//Lisää aloituskäyttäjät tietokantaan käynnistyksen yhteydessä
@Component
public class DataInitializer implements CommandLineRunner {

    private final AppUserService appUserService;
    private final UserService userService;
    private final SportRepository sportRepository;

    public DataInitializer(AppUserService appUserService, UserService userService, SportRepository sportRepository) {
        this.appUserService = appUserService;
        this.userService = userService;
        this.sportRepository = sportRepository;
    }

    @Override
    public void run(String... args) {

        //Lisätään admin jos sitä ei vielä ole
        if (!appUserService.usernameExists("admin")) {
            appUserService.registerUser("admin", "admin123", "ADMIN");
        }

        //Lisätään user jos sitä ei vielä ole
        if (!appUserService.usernameExists("user")) {
            appUserService.registerUser("user", "user123", "USER");
        }

        //Synkronoidaan kaikki AppUserit myös User-tauluun
        for (AppUser appUser : appUserService.findAllAppUsers()) {
            if (!userService.domainUserExists(appUser.getUsername())) {
                userService.createDomainUser(appUser.getUsername());
            }

            //Lisätään oletuslajit jos niitä ei vielä ole
            if (sportRepository.findByName("Football").isEmpty()) {
                Sport sport = new Sport();
                sport.setName("Football");
                sport.setDescription("Jalkapallo");
                sportRepository.save(sport);
            }

            if (sportRepository.findByName("Basketball").isEmpty()) {
                Sport sport = new Sport();
                sport.setName("Basketball");
                sport.setDescription("Koripallo");
                sportRepository.save(sport);
            }

            if (sportRepository.findByName("Padel").isEmpty()) {
                Sport sport = new Sport();
                sport.setName("Padel");
                sport.setDescription("Padel");
                sportRepository.save(sport);
            }
        }
    }
}