package com.example.casualgames.service;

import com.example.casualgames.entity.UserProfile;
import com.example.casualgames.repository.UserProfileRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.example.casualgames.entity.User;
import com.example.casualgames.entity.UserProfile;
import java.util.List;


//Service kerros UserProfile logiikkaa varten
@Service
public class UserProfileService {

    //Repository tietokantaoperaatioita varten
    private final UserProfileRepository userProfileRepository;

    //Konstruktori injektoi repositoryn
    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    //Hae käyttäjän profiili tai luo uusi jos sitä ei ole
    public UserProfile getOrCreateProfile(User user) {

        //Yritetään hakea profiili tietokannasta
        Optional<UserProfile> existingProfile = userProfileRepository.findByUser(user);

        //Jos löytyy, palautetaan se
        if (existingProfile.isPresent()) {
            return existingProfile.get();
        }

        //Muuten luodaan uusi profiili
        UserProfile newProfile = new UserProfile();
        newProfile.setUser(user);

        return userProfileRepository.save(newProfile);
    }

    //Hae kaikki profiilit
    public List<UserProfile> findAllProfiles() {
        return userProfileRepository.findAll();
    }

    //Tallenna profiili
    public UserProfile saveProfile(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    //Poista profiili ID:n perusteella
    public void deleteProfile(Long id) {
        userProfileRepository.deleteById(id);
    }
}