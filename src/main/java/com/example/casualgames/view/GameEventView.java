package com.example.casualgames.view;

import com.example.casualgames.entity.GameEvent;
import com.example.casualgames.service.GameEventService;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import java.time.LocalDateTime;
import java.time.LocalTime;
import com.vaadin.flow.component.dialog.Dialog;
import com.example.casualgames.service.SportService;
import com.example.casualgames.view.GameEventForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.button.ButtonVariant;
import com.example.casualgames.service.ReservationService;


@Route(value = "events", layout = MainLayout.class)
@PermitAll
public class GameEventView extends VerticalLayout {

    //Sport service lajien hakemista varten
    private final SportService sportService;

    //Reservation service ilmoittautumisia varten
    private final ReservationService reservationService;

    //Tapahtuman lomake
    private GameEventForm eventForm = new GameEventForm();

    //Grid näyttää pelit
    private Grid<GameEvent> grid = new Grid<>(GameEvent.class);

    //Hakukenttä yleistä hakua varten
    private TextField keywordField = new TextField("Search");

    //Kaupunki suodatus
    private TextField cityField = new TextField("City");

    //Laji suodatus
    private TextField sportField = new TextField("Sport");

    //Päivämäärä suodatus
    private DatePicker startDate = new DatePicker("From date");
    private DatePicker endDate = new DatePicker("To date");

    //Hakupainike
    private Button searchButton = new Button("Search");

    //Lisää tapahtuma nappi
    private Button addEventButton = new Button("Add Event");

    //Dialog tapahtuman luomista varten
    private Dialog eventDialog = new Dialog();

    //Service
    private final GameEventService gameEventService;


    //Konstruktori
    public GameEventView(
            GameEventService gameEventService,
            SportService sportService,
            ReservationService reservationService
    ) {

        this.reservationService = reservationService;

        this.gameEventService = gameEventService;

        this.sportService = sportService;

        //Näytetään vain tärkeät sarakkeet
        grid.setColumns("id", "title", "city", "location", "eventDate", "maxPlayers");

        //Edit button column
        grid.addComponentColumn(event -> {

            if (!gameEventService.canModifyEvent(event)) {
                return null;
            }

            Button editButton = new Button("Edit");
            editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

            editButton.addClickListener(click -> {
                eventForm.setGameEvent(event);
                eventDialog.open();
            });

            return editButton;

        }).setHeader("Edit");

        //Delete button column
        grid.addComponentColumn(event -> {

            if (!gameEventService.canModifyEvent(event)) {
                return null;
            }

            Button deleteButton = new Button("Delete");
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

            deleteButton.addClickListener(click -> {

                Dialog confirmDialog = new Dialog();
                confirmDialog.add("Are you sure you want to delete this event?");

                Button confirm = new Button("Delete", e -> {
                    try {
                        gameEventService.deleteEvent(event.getId());
                        updateGrid();
                        Notification.show("Event deleted");
                    } catch (Exception ex) {
                        Notification.show("Delete failed");
                    }
                    confirmDialog.close();
                });

                Button cancel = new Button("Cancel", e -> confirmDialog.close());

                confirmDialog.add(confirm, cancel);
                confirmDialog.open();
            });

            return deleteButton;

        }).setHeader("Delete");

        //Join button column
        grid.addComponentColumn(event -> {

            Button joinButton = new Button("Join");

            //Jos käyttäjä on jo liittynyt, nappi pois käytöstä
            if (reservationService.hasUserJoined(event)) {
                joinButton.setText("Joined");
                joinButton.setEnabled(false);
            }

            joinButton.addClickListener(click -> {
                try {
                    reservationService.joinEvent(event.getId());
                    updateGrid();
                    Notification.show("Joined successfully");
                } catch (Exception e) {
                    Notification.show(e.getMessage());
                }
            });

            return joinButton;

        }).setHeader("Join");

        //Näytetään myös lajin nimi
        grid.addColumn(event ->
                event.getSport() != null ? event.getSport().getName() : "No sport"
        ).setHeader("Sport");

        //Näytetään osallistujatilanne
        grid.addColumn(event ->
                reservationService.countReservationsForEvent(event) + "/" + event.getMaxPlayers()
        ).setHeader("Players");

        //Hakupainike käynnistää haun
        searchButton.addClickListener(event -> searchEvents());

        //Avaa tapahtuma dialogin
        addEventButton.addClickListener(event -> {

            //Luodaan uusi tapahtuma
            eventForm.setGameEvent(new GameEvent());

            eventDialog.open();
        });


        //Asetetaan lajit lomakkeelle
        eventForm.setSports(sportService.findAllSports());

        //Lisätään lomake dialogiin
        eventDialog.add(eventForm);

        //Tallennetaan uusi tapahtuma
        eventForm.addSaveListener(event -> {

            if (eventForm.isValid()) {

                GameEvent gameEvent = eventForm.getGameEvent();

                try {

                    if (gameEvent.getId() == null) {
                        gameEventService.createEvent(gameEvent);
                        Notification.show("Event created");
                    } else {
                        gameEventService.updateEvent(gameEvent);
                        Notification.show("Event updated");
                    }

                    eventDialog.close();
                    updateGrid();

                } catch (Exception e) {
                    Notification.show("You are not allowed to modify this event");
                }

            } else {
                Notification.show("Please check the form fields");
            }
        });

//Suljetaan dialog ilman tallennusta
        eventForm.addCancelListener(event -> {
            eventDialog.close();
        });

        //Lisätään komponentit näkymään
        add(
                keywordField,
                cityField,
                sportField,
                startDate,
                endDate,
                searchButton,
                addEventButton,
                grid
        );

        //Ladataan aluksi kaikki pelit
        updateGrid();
    }

    //Suoritetaan haku
    private void searchEvents() {

        LocalDateTime start = null;
        LocalDateTime end = null;

        //Muutetaan DatePicker LocalDate -> LocalDateTime
        if (startDate.getValue() != null) {
            start = startDate.getValue().atStartOfDay();
        }

        if (endDate.getValue() != null) {
            end = endDate.getValue().atTime(LocalTime.MAX);
        }

        grid.setItems(
                gameEventService.searchEvents(
                        keywordField.getValue(),
                        cityField.getValue(),
                        sportField.getValue(),
                        start,
                        end
                )
        );
    }

    //Näytetään kaikki pelit
    private void updateGrid() {
        grid.setItems(gameEventService.findAllEvents());
    }
}