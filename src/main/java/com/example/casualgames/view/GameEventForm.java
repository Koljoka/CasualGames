package com.example.casualgames.view;

import com.example.casualgames.entity.GameEvent;
import com.example.casualgames.entity.Sport;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;

import java.util.List;


//Lomake pelitapahtuman tietoja varten
public class GameEventForm extends FormLayout {

    //Tapahtuman nimi
    private TextField title = new TextField("Title");

    //Kaupunki
    private TextField city = new TextField("City");

    //Pelipaikka
    private TextField location = new TextField("Location");

    //Tapahtuman ajankohta
    private DateTimePicker eventDate = new DateTimePicker("Event date");

    //Maksimipelaajat
    private IntegerField maxPlayers = new IntegerField("Max players");

    //Kuvaus
    private TextArea description = new TextArea("Description");

    //Laji
    private ComboBox<Sport> sport = new ComboBox<>("Sport");

    //Tallennus
    private Button saveButton = new Button("Save");

    //Peruutus
    private Button cancelButton = new Button("Cancel");

    //Binder sitoo kentät GameEventiin
    private Binder<GameEvent> binder = new Binder<>(GameEvent.class);


    //Konstruktori
    public GameEventForm() {

        //Pakolliset kentät
        title.setRequired(true);
        city.setRequired(true);
        location.setRequired(true);
        eventDate.setRequiredIndicatorVisible(true);
        maxPlayers.setRequiredIndicatorVisible(true);
        sport.setRequired(true);

        //Max players ei voi olla negatiivinen
        maxPlayers.setMin(1);

        //Kuvauskentän korkeus
        description.setHeight("120px");

        //Näytetään lajista nimi
        sport.setItemLabelGenerator(Sport::getName);

        //Sidotaan kentät GameEventiin
        binder.forField(title)
                .asRequired("Title is required")
                .bind(GameEvent::getTitle, GameEvent::setTitle);

        binder.forField(city)
                .asRequired("City is required")
                .bind(GameEvent::getCity, GameEvent::setCity);

        binder.forField(location)
                .asRequired("Location is required")
                .bind(GameEvent::getLocation, GameEvent::setLocation);

        binder.forField(eventDate)
                .asRequired("Event date is required")
                .bind(GameEvent::getEventDate, GameEvent::setEventDate);

        binder.forField(maxPlayers)
                .asRequired("Max players is required")
                .bind(GameEvent::getMaxPlayers, GameEvent::setMaxPlayers);

        binder.bind(description, GameEvent::getDescription, GameEvent::setDescription);

        binder.forField(sport)
                .asRequired("Sport is required")
                .bind(GameEvent::getSport, GameEvent::setSport);

        //Painikkeet samalle riville
        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);

        //Lisätään kentät lomakkeelle
        add(
                title,
                city,
                location,
                eventDate,
                maxPlayers,
                sport,
                description,
                buttons
        );
    }

    //Aseta lajit valintaan
    public void setSports(List<Sport> sports) {
        sport.setItems(sports);
    }

    //Aseta muokattava tapahtuma
    public void setGameEvent(GameEvent gameEvent) {
        binder.setBean(gameEvent);
    }

    //Palauta nykyinen tapahtuma
    public GameEvent getGameEvent() {
        return binder.getBean();
    }

    //Tallennus/peruutus kuuntelijat
    public void addSaveListener(ComponentEventListener<ClickEvent<Button>> listener) {
        saveButton.addClickListener(listener);
    }

    public void addCancelListener(ComponentEventListener<ClickEvent<Button>> listener) {
        cancelButton.addClickListener(listener);
    }

    //Tarkista onko lomake validi
    public boolean isValid() {
        return binder.validate().isOk();
    }
}