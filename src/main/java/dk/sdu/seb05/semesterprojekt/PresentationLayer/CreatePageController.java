package dk.sdu.seb05.semesterprojekt.PresentationLayer;

import dk.sdu.seb05.semesterprojekt.PersistenceLayer.Category;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.util.StringConverter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class CreatePageController {

    public static PresentationSingleton fulcrum;

    @FXML
    private ChoiceBox<String> categoryChoiceBox;
    @FXML
    private Button createProgramButton;
    @FXML
    private Label chosenLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private DatePicker sendDate;
    @FXML
    private TextField programTitleTextField;
    @FXML
    private TextField channelTextField;
    @FXML
    private Button backButton;

    public void initialize() {
        fulcrum = PresentationSingleton.getInstance();
        Category[] cats = Category.values(); //Domain.Category når domain kommer
        for (int i = 0; i < cats.length; i++) {
            categoryChoiceBox.getItems().add(cats[i].toString());
        }
    }

    public void returnHandler() throws IOException {
        fulcrum.goToFrontPage();
    }

    public void datePicker() {
        LocalDate date = sendDate.getValue();
        int day = date.getDayOfMonth();
        int month = date.getMonthValue();
        int year = date.getYear();
        chosenLabel.setVisible(true);
        dateLabel.setText(day + ". " + monthToString(month) + " " + year );
    }

    private void errorAlert(String text){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/css/dialog-pane-styles.css")
                        .toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        alert.setTitle("Irriterende popup");
        alert.setHeaderText(text);
        alert.setContentText(" ");
        alert.showAndWait();
    }

    private void succesAlert() throws IOException {
        Alert alert = new Alert(Alert.AlertType.NONE);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/css/dialog-pane-styles-success.css")
                        .toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        ButtonType confirmButton = new ButtonType("Tilføj Credits");
        ButtonType backButton = new ButtonType("Tilbage til forside");
        alert.getDialogPane().getButtonTypes().add(confirmButton);
        alert.getDialogPane().getButtonTypes().add(backButton);
        System.out.println(confirmButton.getButtonData());
        alert.setHeaderText("Du har nu oprettet et nyt program!");
        alert.setContentText("Vil du tilføje credits eller tilbage til forsiden?");
        Optional<ButtonType> buttonType =  alert.showAndWait();
        if(buttonType.isPresent() && buttonType.get() == backButton){
            fulcrum.goToFrontPage();
        }
        else if(buttonType.isPresent() && buttonType.get() == confirmButton){
            Parent createCreditPage = FXMLLoader.load(JavaFXTest.class.getResource("/fxml/createcreditpage.fxml"));
            fulcrum.getPrimaryStage().setScene(new Scene(createCreditPage));
            fulcrum.getPrimaryStage().setTitle("Tilføj credits");
        }
    }

    public boolean errorHandler(){
        StringBuilder stringBuilder = new StringBuilder();
        if(programTitleTextField.getText() == null || programTitleTextField.getText().trim().isEmpty()){
            stringBuilder.append("Du mangler at give dit program et navn! \n");
        }
        if(channelTextField.getText() == null || channelTextField.getText().trim().isEmpty()){
            stringBuilder.append("Du mangler at vælge en kanal! \n");
        }
        if(categoryChoiceBox.getSelectionModel() == null || categoryChoiceBox.getSelectionModel().isEmpty()){
            stringBuilder.append("Du mangler at udfylde kategori! \n");
        }
        if(dateLabel.getText() == null || dateLabel.getText().trim().isEmpty()){
            stringBuilder.append("Du mangler at vælge en dato \n");
        }
        if(stringBuilder.toString().trim().isEmpty()){
            return false;
        }
        else {
            errorAlert(stringBuilder.toString());
            return true;
        }

    }

    public void createProgramHandler() throws IOException {
        if(errorHandler()){
            System.out.println("Something needs filling in!");
        }
        else if (!errorHandler()) {
            System.out.println("Du har oprettet følgende program: \n" +
                    "Program navn: " + programTitleTextField.getText() + "\n" +
                    "Kategori: " + categoryChoiceBox.getValue() + "\n" +
                    "Valgt kanal: " + channelTextField.getText() + "\n" +
                    "Dato: " + dateLabel.getText() + "\n"
            );
            succesAlert();
        }
    }

    public String monthToString(int month){
        String returnMonth = "";
        switch (month){
            case 1:
                returnMonth = "Januar";
                break;
            case 2:
                returnMonth = "Februar";
                break;
            case 3:
                returnMonth = "Marts";
                break;
            case 4:
                returnMonth = "April";
                break;
            case 5:
                returnMonth = "Maj";
                break;
            case 6:
                returnMonth = "Juni";
                break;
            case 7:
                returnMonth = "Juli";
                break;
            case 8:
                returnMonth = "August";
                break;
            case 9:
                returnMonth = "September";
                break;
            case 10:
                returnMonth = "Oktober";
                break;
            case 11:
                returnMonth = "November";
                break;
            case 12:
                returnMonth = "December";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + month);
        }
        return returnMonth;
    }

}
