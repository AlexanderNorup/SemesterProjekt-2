package dk.sdu.seb05.semesterprojekt.PresentationLayer;

import com.jfoenix.controls.*;
import com.jfoenix.controls.events.JFXDialogEvent;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.FunctionType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class CreateCreditsController {

    public static PresentationSingleton fulcrum;

    @FXML
    private StackPane stackPane;
    @FXML
    private JFXListView<String> creditsListView; //Skal ændres til <ICredit> når implementation er mulig
    @FXML
    private Label nameLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private JFXButton returnButton;
    @FXML
    private JFXComboBox<FunctionType> functionTypeExistComboBox;
    @FXML
    private JFXComboBox<FunctionType> functionTypeNewComboBox;
    @FXML
    private JFXComboBox<String> personComboBox; //Skal ændres til <IPerson> når implementation er mulig
    @FXML
    private JFXButton seeCreditButton;
    @FXML
    private JFXButton addNewCreditButton;
    @FXML
    private JFXButton addExistingCreditButton;
    @FXML
    private TextField nameField;
    @FXML
    private TextField descriptionField;
    @FXML
    private DatePicker birthdayPicker;

    public void initialize() {
        fulcrum = PresentationSingleton.getInstance();
        fulcrum.setTitle("Tilføj credits");
        FunctionType[] functionTypes = FunctionType.values();
        functionTypeExistComboBox.getItems().addAll(functionTypes);
        functionTypeNewComboBox.getItems().addAll(functionTypes);
        ObservableList<String> persons = FXCollections.observableArrayList(
                "Boris Johnson - 56 år - Kamera mand",
                "Lars Andersson - 26 år - Kattemand",
                "Martin Nielsen - 33 år - Skuespiller",
                "Alexander Nørup - 20 år - Vandmands biolog");
        creditsListView.setItems(persons); // test data
        creditsListView.getStyleClass().add("mylistview");

        personComboBox.getItems().addAll("Henrik - 20 år", "Frederik - 23 år", "Malene - 28 år"); //person1 osv.. er Place holder

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
        if (functionTypeNewComboBox.getSelectionModel() == null || functionTypeNewComboBox.getSelectionModel().isEmpty()) {
            System.out.println("Du skal vælge en funktion!");
        } else {
            System.out.println("Dine credits er blevet tilføjet \n" +
                    "Person navn: " + nameField.getText() + "\n" +
                    "Deskription navn: " + descriptionField.getText() + "\n" +
                    "funktion typen er: " + functionTypeNewComboBox.getValue() + " \n"
            );
        }
    }


    public void addExistingCredit() {
        System.out.println("\n");
        if (personComboBox.getSelectionModel() == null || personComboBox.getSelectionModel().isEmpty()) {
            System.out.println("Du skal vælge en person!");
        }
        if (functionTypeExistComboBox.getSelectionModel() == null || functionTypeExistComboBox.getSelectionModel().isEmpty()) {
            System.out.println("Du skal vælge en funktion!");
        } else {
            System.out.println("Du har tilføjet: " + fulcrum.getDomainLayer().getCreditsForPerson(0) + "med funktion typen: "
                    + functionTypeExistComboBox.getValue()
            );
        }
    }

    private void popupError(String errorText) {
        stackPane.setVisible(true);
        JFXDialogLayout content = new JFXDialogLayout();
        Text text = new Text();
        text.setText(errorText);
        content.setHeading(new Text("Der skete en fejl!"));
        content.setBody(text);

        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton button = new JFXButton("Okay");
        button.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.setOnDialogClosed(new EventHandler<>() {
            @Override
            public void handle(JFXDialogEvent events) {
                stackPane.setVisible(false);
            }
        });
        content.setActions(button);
        dialog.show();

    }
    public Date datePicker() {
        LocalDate localDate = birthdayPicker.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);
        return date;
    }

    public void seeCredits() {
        creditsListView.setVisible(!creditsListView.isVisible());
        if (creditsListView.isVisible()) {
            functionTypeNewComboBox.setDisable(true);
            addNewCreditButton.setDisable(true);
            addExistingCreditButton.setDisable(true);
        } else {
            functionTypeNewComboBox.setDisable(false);
            addNewCreditButton.setDisable(false);
            addExistingCreditButton.setDisable(false);
        }
    }

    public boolean errorHandler(ActionEvent actionEvent) {
        StringBuilder errorString = new StringBuilder();
        if (actionEvent.getSource() == addNewCreditButton) {
            if (nameField.getText() == null || nameField.getText().trim().isEmpty()) {
                errorString.append("Du mangler at udfylde et navn!\n");
            }
            if (descriptionField.getText() == null || descriptionField.getText().trim().isEmpty()) {
                errorString.append("Du mangler at udfylde detaljer!\n");
            }
            if (birthdayPicker.getValue() == null){
                errorString.append("Du mangler at vælge en fødselsdato!\n");
            }
            if (functionTypeNewComboBox.getSelectionModel() == null || functionTypeNewComboBox.getSelectionModel().isEmpty()) {
                errorString.append("Du mangler at vælge en funktion!\n");
            }

        }
        else if (actionEvent.getSource() == addExistingCreditButton) {
            if (personComboBox.getSelectionModel() == null || personComboBox.getSelectionModel().isEmpty()) {
                errorString.append("Du mangler at vælge en person!\n");
            }
            if (functionTypeExistComboBox.getSelectionModel() == null || functionTypeExistComboBox.getSelectionModel().isEmpty()) {
                errorString.append("Du mangler at vælge en funktion!\n");
            }
        }
        if (errorString.toString().trim().isEmpty()) {
            return false;
        } else {
            popupError(errorString.toString());
            return true;
        }
    }


    public void addCredit(ActionEvent actionEvent) {
        if (errorHandler(actionEvent)) {
            System.out.println("Something needs filling in!");
        } else if (!errorHandler(actionEvent)) {
            if(actionEvent.getSource() == addNewCreditButton) {
                int age = 2021 - birthdayPicker.getValue().getYear(); //lazy, but just for testing
                String createdPerson = nameField.getText() + " - " + age + " år" + " - " + functionTypeNewComboBox.getValue().getDesc(); //for testing purposes
                ObservableList<String> current = creditsListView.getItems(); // gets current credits
                current.addAll(createdPerson); // adds the newly created credit to that list
                creditsListView.setItems(current); //adds them to the ListView
                nameField.setText(null); // resetting the fields to normal
                descriptionField.setText(null);
                birthdayPicker.setValue(null);
                functionTypeNewComboBox.setValue(null);
            }
            if(actionEvent.getSource() == addExistingCreditButton){
                String createdPerson = personComboBox.getValue() + " - " + functionTypeExistComboBox.getValue().getDesc();
                ObservableList<String> current = creditsListView.getItems();
                current.addAll(createdPerson);
                creditsListView.setItems(current);
                personComboBox.setValue(null);
                functionTypeExistComboBox.setValue(null);
            }
        }


    /*
    public boolean errorHandler(){
        StringBuilder stringBuilder = new StringBuilder();
        if(programTitleTextField.getText() == null || programTitleTextField.getText().trim().isEmpty()){
            stringBuilder.append("Du mangler at give dit program et navn! \n");
        }
        if(channelTextField.getText() == null || channelTextField.getText().trim().isEmpty()){
            stringBuilder.append("Du mangler at vælge en kanal! \n");
        }
        if(categoryComboBox.getSelectionModel() == null || categoryComboBox.getSelectionModel().isEmpty()){
            stringBuilder.append("Du mangler at udfylde kategori! \n");
        }
        if(dateLabel.getText() == null || dateLabel.getText().trim().isEmpty()){
            stringBuilder.append("Du mangler at vælge en dato \n");
        }
        if(stringBuilder.toString().trim().isEmpty()){
            return false;
        }
        else {
            popupError(stringBuilder.toString());
            return true;
        }

    }
     */
    }
}
