package com.example.casualgames.view;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;


//Sovelluksen etusivu
@AnonymousAllowed
@Route(value = "", layout = MainLayout.class)
public class HomeView extends VerticalLayout {

    //Konstruktori
    public HomeView() {

        //Sovelluksen logo, ei toimi näin
        Image logo = new Image("src/images/CasualGames.png", "X");
        logo.setWidth("500px");


        //Sivun otsikko
        H1 title = new H1("Welcome to Casual Games");

        //Lyhyt kuvaus sovelluksesta
        Paragraph description = new Paragraph(
                "Find and join casual sports events in your city."
        );

        //Asetetaan layout
        setSizeFull();
        setSpacing(true);
        setPadding(true);
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        //Lisätään komponentit näkymään
        add(logo, title, description);
    }
}