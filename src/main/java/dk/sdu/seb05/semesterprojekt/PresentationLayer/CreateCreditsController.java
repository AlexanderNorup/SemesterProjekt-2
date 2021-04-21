package dk.sdu.seb05.semesterprojekt.PresentationLayer;

import dk.sdu.seb05.semesterprojekt.PersistenceLayer.FunctionType;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;

public class CreateCreditsController {

    public static PresentationSingleton fulcrum;
    public Label nameLabel;
    public Label descriptionLabel;
    public Button returnButton;

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

    public void datePicker() {
        LocalDate date = birthdayPicker.getValue();
        int day = date.getDayOfMonth();
        int month = date.getMonthValue();
        int year = date.getYear();
    }

    private void errorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/css/dialog-pane-styles.css")
                        .toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        alert.setTitle("Irriterende popup");
        alert.setHeaderText("Mathias for helvete");
        alert.setContentText("ctrl a + ctrl c -> ctrl v, bedste måde at kode på");
        alert.showAndWait();
    }

    private void succesAlert() throws IOException {
        Alert alert = new Alert(Alert.AlertType.NONE);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/css/dialog-pane-styles-success.css")
                        .toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        ButtonType backButton = new ButtonType("Tilbage til forside");
        alert.getDialogPane().getButtonTypes().add(backButton);
        alert.setHeaderText("Du har nu oprettet et ny credit!");
        alert.setContentText(" ");
        alert.showAndWait();
    }


}
