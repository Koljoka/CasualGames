package com.example.casualgames.view;

import com.example.casualgames.service.AppUserService;
import com.example.casualgames.service.UserService;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;


//Rekisteröitymissivu
@AnonymousAllowed
@Route("register")
public class RegisterView extends VerticalLayout {

    //Käyttäjänimi
    private TextField usernameField = new TextField("Username");

    //Salasana
    private PasswordField passwordField = new PasswordField("Password");

    //Salasanan vahvistus
    private PasswordField confirmPasswordField = new PasswordField("Confirm password");

    //Rekisteröitymisnappi
    private Button registerButton = new Button("Register");

    //Nappi takaisin login-sivulle
    private Button backToLoginButton = new Button("Back to login");

    //Service autentikointikäyttäjän tallentamista varten
    private final AppUserService appUserService;

    //Service domain-käyttäjän tallentamista varten
    private final UserService userService;

    //Konstruktori
    public RegisterView(AppUserService appUserService, UserService userService) {
        this.appUserService = appUserService;
        this.userService = userService;

        //Napin klikkaus suorittaa rekisteröinnin
        registerButton.addClickListener(event -> registerUser());

        //Siirrytään takaisin login-sivulle
        backToLoginButton.addClickListener(event ->
                getUI().ifPresent(ui -> ui.navigate("login"))
        );

        //Lisätään komponentit näkymään
        add(usernameField, passwordField, confirmPasswordField, registerButton, backToLoginButton);
    }

    //Rekisteröidään uusi käyttäjä
    private void registerUser() {

        String username = usernameField.getValue();
        String password = passwordField.getValue();
        String confirmPassword = confirmPasswordField.getValue();

        //Tarkistetaan että kaikki kentät on täytetty
        if (username.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            Notification.show("All fields are required");
            return;
        }

        //Tarkistetaan että salasanat täsmäävät
        if (!password.equals(confirmPassword)) {
            Notification.show("Passwords do not match");
            return;
        }

        //Tarkistetaan että käyttäjänimi ei ole jo käytössä
        if (appUserService.usernameExists(username)) {
            Notification.show("Username is already taken");
            return;
        }

        //Tallennetaan autentikointikäyttäjä USER-roolilla
        appUserService.registerUser(username, password, "USER");

        //Luodaan samalla domain-käyttäjä jos sitä ei vielä ole
        if (!userService.domainUserExists(username)) {
            userService.createDomainUser(username);
        }

        Notification.show("Registration successful");

        //Siirrytään login-sivulle onnistuneen rekisteröinnin jälkeen
        getUI().ifPresent(ui -> ui.navigate("login"));
    }
}