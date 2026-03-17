package com.example.casualgames.view;

import com.example.casualgames.entity.UserProfile;
import com.example.casualgames.service.UserProfileService;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;


//Adminin näkymä kaikille profiileille
@RolesAllowed("ADMIN")
@Route(value = "profiles", layout = MainLayout.class)
public class UserProfileView extends VerticalLayout {

    //Grid näyttää profiilit taulukossa
    private Grid<UserProfile> grid = new Grid<>(UserProfile.class);

    //Service jolla haetaan profiilit
    private final UserProfileService userProfileService;

    //Konstruktori
    public UserProfileView(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;

        //Näytetään tärkeimmät sarakkeet
        grid.setColumns("id", "bio", "phoneNumber", "age", "experienceYears", "preferredSport");

        //Näytetään myös käyttäjän username
        grid.addColumn(profile ->
                profile.getUser() != null ? profile.getUser().getUsername() : "No user"
        ).setHeader("Username");

        //Näytetään myös käyttäjän nimi
        grid.addColumn(profile ->
                profile.getUser() != null ? profile.getUser().getName() : "No name"
        ).setHeader("Name");

        //Haetaan profiilit tietokannasta
        updateGrid();

        //Lisätään grid näkymään
        add(grid);
    }

    //Päivitetään gridin data
    private void updateGrid() {
        grid.setItems(userProfileService.findAllProfiles());
    }
}