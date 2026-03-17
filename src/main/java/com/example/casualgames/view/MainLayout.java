package com.example.casualgames.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


//Sovelluksen yhteinen päälayout

@AnonymousAllowed
public class MainLayout extends AppLayout {

    //Konstruktori
    public MainLayout(AuthenticationContext authenticationContext) {

        //DrawerToggle avaa ja sulkee vasemman navigaation
        DrawerToggle toggle = new DrawerToggle();

        //Sovelluksen nimi headeriin
        H1 title = new H1("Casual Games");

        //Haetaan kirjautuneen käyttäjän nimi
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : "Guest";

        //Näytetään käyttäjän nimi headerissa
        Span userInfo = new Span("User: " + username);

        //Logout nappi
        Button logoutButton = new Button("Logout");
        logoutButton.addClickListener(event -> {
            VaadinSession.getCurrent().getSession().invalidate();
            VaadinSession.getCurrent().close();
            getUI().ifPresent(ui -> ui.getPage().setLocation("/logout"));
        });

        //Ylärivin sisältö
        HorizontalLayout header;

        if (authenticationContext.getAuthenticatedUser(Object.class).isPresent()) {
            header = new HorizontalLayout(toggle, title, userInfo, logoutButton);
        } else {
            header = new HorizontalLayout(toggle, title);
        }
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);

        //Navigaatiolinkit
        RouterLink homeLink = new RouterLink();
        homeLink.add(new Icon(VaadinIcon.HOME), new Span(" Home"));
        homeLink.setRoute(HomeView.class);

        RouterLink usersLink = new RouterLink();
        usersLink.add(new Icon(VaadinIcon.USERS), new Span(" Users"));
        usersLink.setRoute(UserView.class);

        RouterLink profileLink = new RouterLink();
        profileLink.add(new Icon(VaadinIcon.USER_CARD), new Span(" Profile"));
        profileLink.setRoute(ProfileView.class);

        RouterLink profilesLink = new RouterLink();
        profilesLink.add(new Icon(VaadinIcon.USER_CARD), new Span(" Profiles"));
        profilesLink.setRoute(UserProfileView.class);

        RouterLink eventsLink = new RouterLink();
        eventsLink.add(new Icon(VaadinIcon.CALENDAR), new Span(" Events"));
        eventsLink.setRoute(GameEventView.class);

        RouterLink loginLink = new RouterLink();
        loginLink.add(new Icon(VaadinIcon.SIGN_IN), new Span("Login"));
        loginLink.setRoute(LoginView.class);

        RouterLink registerLink = new RouterLink();
        registerLink.add(new Icon(VaadinIcon.USER_CHECK), new Span("Register"));
        registerLink.setRoute(RegisterView.class);

        //Navigaation sisältö
        VerticalLayout menuLayout = new VerticalLayout();

        //Navigaation otsikko
        menuLayout.add(new Span("Navigation"));

        //Home näkyy kaikille
        menuLayout.add(homeLink);

        //Jos käyttäjä ei ole kirjautunut
        if (authenticationContext.getAuthenticatedUser(Object.class).isEmpty()) {

            //Näytetään login ja register
            menuLayout.add(loginLink);
            menuLayout.add(registerLink);

        } else {

            //Admin näkee Profiles
            if (authenticationContext.hasRole("ADMIN")) {
                menuLayout.add(profilesLink);
            } else {
                //User näkee Profile
                menuLayout.add(profileLink);
            }

            //Events näkyy kirjautuneille käyttäjille
            menuLayout.add(eventsLink);

            //Users vain adminille
            if (authenticationContext.hasRole("ADMIN")) {
                menuLayout.add(usersLink);
            }
        }


        //Scroller navigaatiota varten
        Scroller scroller = new Scroller(menuLayout);

        //Footer
        Footer footer = new Footer();
        footer.add(new Span("Made by Kelju K © 2026"));

        //Drawerin kokonaisrakenne
        VerticalLayout drawerLayout = new VerticalLayout(scroller, footer);
        drawerLayout.setSizeFull();
        drawerLayout.expand(scroller);

        //Lisätään header ja drawer
        addToNavbar(header);
        addToDrawer(drawerLayout);
    }
}