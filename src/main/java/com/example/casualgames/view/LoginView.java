package com.example.casualgames.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;


//Kirjautumissivu
@AnonymousAllowed
@Route("login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    //LoginForm on Vaadinin valmis kirjautumislomake
    private LoginForm login = new LoginForm();

    //Linkkinappi rekisteröitymiseen
    private Button registerButton = new Button("No account? Register here");

    //Nappi etusivulle
    private Button homeButton = new Button("Login as a visitor");

    //Konstruktori
    public LoginView() {

        //Määritetään mihin osoitteeseen login form lähettää tiedot
        login.setAction("login");

        //Siirrytään rekisteröitymissivulle
        registerButton.addClickListener(event ->
                getUI().ifPresent(ui -> ui.navigate("register"))
        );

        //Siirrytään etusivulle
        homeButton.addClickListener(event ->
                getUI().ifPresent(ui -> ui.navigate(""))
        );

        //Lisätään komponentit näkymään
        add(login, registerButton, homeButton);
    }

    //Näytetään virhe, jos kirjautuminen epäonnistuu
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            login.setError(true);
        }
    }
}