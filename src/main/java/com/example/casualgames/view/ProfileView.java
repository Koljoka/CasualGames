package com.example.casualgames.view;

import com.example.casualgames.entity.User;
import com.example.casualgames.entity.UserProfile;
import com.example.casualgames.service.UserProfileService;
import com.example.casualgames.service.UserService;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


//Käyttäjän oma profiilinäkymä
@RolesAllowed({"USER", "ADMIN"})
@Route(value = "profile", layout = MainLayout.class)
public class ProfileView extends VerticalLayout {

    //Esittelyteksti
    private TextArea bioField = new TextArea("Bio");

    //Puhelinnumero
    private TextField phoneNumberField = new TextField("Phone number");

    //Ikä
    private IntegerField ageField = new IntegerField("Age");

    //Kokemus vuosina
    private IntegerField experienceYearsField = new IntegerField("Experience years");

    //Suosikkilaji
    private TextField preferredSportField = new TextField("Preferred sport");

    //Tallennusnappi
    private Button saveButton = new Button("Save profile");

    //Service käyttäjän hakemista varten
    private final UserService userService;

    //Service profiilin hakemista varten
    private final UserProfileService userProfileService;

    //Kirjautuneen käyttäjän profiili
    private UserProfile currentProfile;

    //Konstruktori
    public ProfileView(UserService userService, UserProfileService userProfileService) {
        this.userService = userService;
        this.userProfileService = userProfileService;

        //Haetaan kirjautuneen käyttäjän profiili
        loadCurrentUserProfile();

        //Tallennetaan profiili
        saveButton.addClickListener(event -> saveProfile());

        //Lisätään komponentit näkymään
        add(
                bioField,
                phoneNumberField,
                ageField,
                experienceYearsField,
                preferredSportField,
                saveButton
        );
    }

    //Haetaan kirjautuneen käyttäjän profiili
    private void loadCurrentUserProfile() {

        //Haetaan kirjautuneen käyttäjän tunnus
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        //Haetaan User-entiteetti käyttäjänimen perusteella
        User currentUser = userService.findByUsername(username);

        //Jos User löytyy, haetaan tai luodaan profiili
        if (currentUser != null) {
            currentProfile = userProfileService.getOrCreateProfile(currentUser);

            //Tuodaan tiedot lomakkeeseen
            bioField.setValue(currentProfile.getBio() != null ? currentProfile.getBio() : "");
            phoneNumberField.setValue(currentProfile.getPhoneNumber() != null ? currentProfile.getPhoneNumber() : "");
            preferredSportField.setValue(currentProfile.getPreferredSport() != null ? currentProfile.getPreferredSport() : "");

            if (currentProfile.getAge() != null) {
                ageField.setValue(currentProfile.getAge());
            }

            if (currentProfile.getExperienceYears() != null) {
                experienceYearsField.setValue(currentProfile.getExperienceYears());
            }
        }
    }

    //Tallennetaan profiili
    private void saveProfile() {

        if (currentProfile == null) {
            Notification.show("Profile could not be loaded");
            return;
        }

        currentProfile.setBio(bioField.getValue());
        currentProfile.setPhoneNumber(phoneNumberField.getValue());
        currentProfile.setAge(ageField.getValue());
        currentProfile.setExperienceYears(experienceYearsField.getValue());
        currentProfile.setPreferredSport(preferredSportField.getValue());

        userProfileService.saveProfile(currentProfile);

        Notification.show("Profile saved");
    }
}