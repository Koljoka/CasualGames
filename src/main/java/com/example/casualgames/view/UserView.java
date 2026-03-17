package com.example.casualgames.view;

import com.example.casualgames.entity.User;
import com.example.casualgames.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed("ADMIN")
@Route(value = "users", layout = MainLayout.class)
public class UserView extends VerticalLayout {

    //Grid näyttää käyttäjät taulukossa
    private Grid<User> grid = new Grid<>(User.class);

    //Lomakentät uuden käyttäjän lisäämiseen
    private TextField usernameField = new TextField("Username");
    private TextField nameField = new TextField("Name");
    private TextField emailField = new TextField("Email");
    private TextField cityField = new TextField("City");
    private TextField skillLevelField = new TextField("Skill level");

    //Tallennusnappi
    private Button saveButton = new Button("Save user");

    //Tyhjennysnappi
    private Button clearButton = new Button("Clear form");

    //Service jolla haetaan ja tallennetaan data tietokantaan
    private final UserService userService;

    //Tähän tallennetaan valittu käyttäjä muokkausta varten
    private User selectedUser;

    //Konstruktorissa Spring antaa UserService:n käyttöön
    public UserView(UserService userService) {

        this.userService = userService;

        //Määritetään gridin näkyvät sarakkeet
        grid.setColumns("id", "name", "email", "city", "skillLevel", "createdAt");

        //Lisätään Edit-nappi joka riville
        grid.addComponentColumn(user -> {
            Button editButton = new Button("Edit");

            editButton.addClickListener(event -> editUser(user));

            return editButton;
        }).setHeader("Edit");

        //Lisätään Delete-nappi joka riville
        grid.addComponentColumn(user -> {
            Button deleteButton = new Button("Delete");

            deleteButton.addClickListener(event -> {

                //Varmistusdialogi ennen poistamista
                ConfirmDialog dialog = new ConfirmDialog();
                dialog.setHeader("Delete user");
                dialog.setText(
                        "Are you sure you want to delete user:\n\n" +
                                user.getName() + " (" + user.getEmail() + ")"
                );

                dialog.setCancelable(true);
                dialog.setCancelText("Cancel");

                dialog.setConfirmText("Delete");
                dialog.addConfirmListener(confirmEvent -> {
                    userService.deleteUser(user.getId());
                    clearForm();
                    updateGrid();
                    Notification.show("User deleted");
                });

                dialog.open();
            });

            return deleteButton;
        }).setHeader("Delete");

        //Haetaan käyttäjät tietokannasta
        updateGrid();

        //Napin klikkaus tallentaa uuden käyttäjän tai päivittää vanhan
        saveButton.addClickListener(event -> saveUser());

        //Tyhjennetään lomake
        clearButton.addClickListener(event -> clearForm());

        //Lisätään komponentit näkymään
        add(usernameField, nameField, emailField, cityField, skillLevelField, saveButton, grid);
    }

    //Tuodaan valitun käyttäjän tiedot lomakkeeseen muokkausta varten
    private void editUser(User user) {
        selectedUser = user;

        usernameField.setValue(user.getUsername() != null ? user.getUsername() : "");
        nameField.setValue(user.getName() != null ? user.getName() : "");
        emailField.setValue(user.getEmail() != null ? user.getEmail() : "");
        cityField.setValue(user.getCity() != null ? user.getCity() : "");
        skillLevelField.setValue(user.getSkillLevel() != null ? user.getSkillLevel() : "");
    }

    //Tallennetaan uusi käyttäjä tai päivitetään olemassa oleva käyttäjä
    private void saveUser() {

        //Jos mitään käyttäjää ei ole valittu, luodaan uusi
        if (selectedUser == null) {
            selectedUser = new User();
        }

        //Asetetaan kenttien arvot oliolle
        selectedUser.setUsername(usernameField.getValue());
        selectedUser.setName(nameField.getValue());
        selectedUser.setEmail(emailField.getValue());
        selectedUser.setCity(nameField.getValue().isBlank() ? cityField.getValue() : cityField.getValue());
        selectedUser.setSkillLevel(skillLevelField.getValue());

        //Tallennetaan käyttäjä tietokantaan
        userService.saveUser(selectedUser);

        //Tyhjennetään lomake tallennuksen jälkeen
        clearForm();

        //Päivitetään grid
        updateGrid();

        //Näytetään pieni ilmoitus käyttäjälle
        Notification.show("User saved");
    }

    //Päivitetään gridin data tietokannasta
    private void updateGrid() {
        grid.setItems(userService.findAllUsers());
    }

    //Tyhjennetään lomakekentät
    private void clearForm() {
        usernameField.clear();
        nameField.clear();
        emailField.clear();
        cityField.clear();
        skillLevelField.clear();
        selectedUser = null;
    }
}