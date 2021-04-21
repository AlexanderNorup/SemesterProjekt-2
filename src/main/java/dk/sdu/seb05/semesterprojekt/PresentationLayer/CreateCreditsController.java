package dk.sdu.seb05.semesterprojekt.PresentationLayer;

import dk.sdu.seb05.semesterprojekt.DomainLayer.DomainController;
import dk.sdu.seb05.semesterprojekt.DomainLayer.IDomainController;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.FunctionType;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IPerson;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.JSONController.JSONPerson;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;

public class CreateCreditsController {

    public static PresentationSingleton fulcrum;
    public Label nameLabel;
    public Label descriptionLabel;
    public Button returnButton;
    IDomainController iDomainController = new DomainController();

    @FXML
    private ChoiceBox<String> functionTypeExistChoiceBox;
    @FXML
    private ChoiceBox<String> functionTypeNewChoiceBox;
    @FXML
    private ChoiceBox<String> personChoiceBox;
    @FXML
    private Button seeCreditButton;
    @FXML
    private Button addNewButton;
    @FXML
    private Button addExistButton;
    @FXML
    private TextField nameField;
    @FXML
    private TextField descriptionField;
    @FXML
    private DatePicker birthdayPicker;

    public void initialize() {
        fulcrum = PresentationSingleton.getInstance();
        FunctionType[] funcs = FunctionType.values();
        for (int i = 0; i < funcs.length; i++) {

            functionTypeExistChoiceBox.getItems().add(funcs[i].toString());
            functionTypeNewChoiceBox.getItems().add(funcs[i].toString());
        }

        personChoiceBox.getItems().addAll("person1", "person2", "person3"); //person1 osv.. er Place holder

    }

    public void returnHandler() throws IOException {
        fulcrum.goToFrontPage();
    }


    public void addCredits() {
        System.out.println("\n");
        if (nameField.getText() == null || nameField.getText().trim().isEmpty()) {
            System.out.println("Du skal udfylde et navn!");
        }
        if (descriptionField.getText() == null || descriptionField.getText().trim().isEmpty()) {
            System.out.println("Du skal udfylde detaljer!");
        }
        if (functionTypeNewChoiceBox.getSelectionModel() == null || functionTypeNewChoiceBox.getSelectionModel().isEmpty()) {
            System.out.println("Du skal vælge en funktion!");
        } else {
            System.out.println("Dine credits er blevet tilføjet \n" +
                    "Person navn: " + nameField.getText()+ "\n" +
                    "Deskription navn: " + descriptionField.getText() + "\n" +
                    "funktion typen er: " + functionTypeNewChoiceBox.getValue() + " \n"
            );
        }
    }

    public void addExistingCredit() {
        System.out.println("\n");
        if (personChoiceBox.getSelectionModel() == null || personChoiceBox.getSelectionModel().isEmpty()) {
            System.out.println("Du skal vælge en person!");
        }
        if (functionTypeExistChoiceBox.getSelectionModel() == null || functionTypeExistChoiceBox.getSelectionModel().isEmpty()) {
            System.out.println("Du skal vælge en funktion!");
        } else {
            System.out.println("Du har tilføjet: " + iDomainController.getCreditsForPerson(0) + "med funktion typen: "
                    + functionTypeExistChoiceBox.getValue()
            );
        }
    }
    public void seeCredits() {

    }
}

