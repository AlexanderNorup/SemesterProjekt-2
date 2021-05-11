package dk.sdu.seb05.semesterprojekt.PresentationLayer.Controllers;

import com.jfoenix.controls.*;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.FunctionType;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.ICredit;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IPerson;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProgramme;
import dk.sdu.seb05.semesterprojekt.PresentationLayer.PresentationSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class CreateCreditsController implements ViewArgumentAdapter {

    PresentationSingleton presentationSingleton;

    @FXML
    private JFXButton deleteCreditButton;
    @FXML
    private StackPane stackPane;
    @FXML
    private JFXListView<ICredit> creditsListView;
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
    private JFXComboBox<IPerson> personComboBox;
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

    ICredit credit;
    IProgramme programme;

    @Override
    public void onLaunch(Object o) {
        presentationSingleton = PresentationSingleton.getInstance();
        presentationSingleton.setTitle("Tilføj credits");
        if(o instanceof IProgramme){
            programme = (IProgramme) o;
            updateListView();
        }
        updateComboBox();
        List<FunctionType> functionTypes = presentationSingleton.getDomainLayer().getFunctionTypes();
        functionTypeExistComboBox.getItems().addAll(functionTypes);
        functionTypeNewComboBox.getItems().addAll(functionTypes);
        creditsListView.getStyleClass().add("mylistview");
    }

    public void returnHandler() throws IOException {
        presentationSingleton.goToFrontPage();
    }

    private void popupError(String errorText) {
        stackPane.setVisible(true);
        JFXDialogLayout content = new JFXDialogLayout();
        Text headingText = new Text("Der skete en fejl!");
        Text bodyText = new Text(errorText);
        headingText.getStyleClass().add("popupTextColor");
        bodyText.getStyleClass().add("popupTextColor");
        content.setHeading(headingText);
        content.setBody(bodyText);
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton button = new JFXButton("Okay");
        //makes the button close the dialog
        button.setOnAction(event -> dialog.close());
        //when the dialog is closed the StackPane get set to invisible
        dialog.setOnDialogClosed(events -> stackPane.setVisible(false));
        content.setActions(button);
        dialog.show();
    }

    private void popupDelete(){
        stackPane.setVisible(true);
        deleteCreditButton.setDisable(true);
        addExistingCreditButton.setDisable(true);
        addNewCreditButton.setDisable(true);
        creditsListView.setDisable(true);
        returnButton.setDisable(true);
        JFXDialogLayout content = new JFXDialogLayout();

        Text headingText = new Text("Ønsker du at slette følgende credit?");
        String personString = credit.getPerson().toString() + " - " + credit.getFunctionType();
        Text bodyText = new Text(personString);
        headingText.getStyleClass().add("popupTextColor");
        bodyText.getStyleClass().add("popupTextColor");
        content.setHeading(headingText);
        content.setBody(bodyText);
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton confirmButton = new JFXButton("Slet credit");
        confirmButton.setStyle("-fx-background-color: #ef0000; -fx-font-family: \"Roboto Bold\"; -fx-font-size: 14;"); //makes button go red
        JFXButton cancelButton = new JFXButton("Gå tilbage");
        dialog.setOverlayClose(false);
        //goes back to edit page
        cancelButton.setOnAction(event -> {
            dialog.close();
        });
        //goes to edit page after deleting program
        confirmButton.setOnAction(event -> {
            presentationSingleton.getDomainLayer().removeCredit(programme.getId(), credit);
            presentationSingleton.getDomainLayer().commit();
            updateListView();
            dialog.close();
        });
        //goes back to the current page to create a new program
        dialog.setOnDialogClosed(event -> {
            stackPane.setVisible(false);
            deleteCreditButton.setDisable(false);
            addExistingCreditButton.setDisable(false);
            addNewCreditButton.setDisable(false);
            returnButton.setDisable(false);
            creditsListView.setDisable(false);
        });
        content.setActions(cancelButton, confirmButton);
        dialog.show();
    }

    public Date datePicker() {
        LocalDate localDate = birthdayPicker.getValue();
        Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);
        return date;
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

    private void updateListView(){
        ObservableList<ICredit> credits = FXCollections.observableArrayList(programme.getCredits());
        creditsListView.setItems(credits);
    }

    private void updateComboBox(){
        ObservableList<IPerson> persons = FXCollections.observableArrayList(presentationSingleton.getDomainLayer().getPersons());
        personComboBox.setItems(persons);
    }

    public void addCredit(ActionEvent actionEvent) {
        if (errorHandler(actionEvent)) {
            System.out.println("Something needs filling in!");
        } else {
            if(actionEvent.getSource() == addNewCreditButton) {
                ICredit credit = presentationSingleton.getDomainLayer().createCredit(nameField.getText(), datePicker(), descriptionField.getText(), functionTypeNewComboBox.getValue());
                programme.addCredit(credit);
                presentationSingleton.getDomainLayer().commit();
                updateComboBox();
                nameField.setText(null); // resetting the fields to normal
                descriptionField.setText(null); // resetting the fields to normal
                birthdayPicker.setValue(null); // resetting the fields to normal
                functionTypeNewComboBox.setValue(null); // resetting the fields to normal
            }
            if(actionEvent.getSource() == addExistingCreditButton){
                ICredit credit = presentationSingleton.getDomainLayer().createCredit(personComboBox.getValue(), functionTypeExistComboBox.getValue());
                programme.addCredit(credit);
                presentationSingleton.getDomainLayer().commit();
                personComboBox.setValue(null); // resetting the fields to normal
                functionTypeExistComboBox.setValue(null); // resetting the fields to normal
            }
            updateListView();
        }
    }

    public void deleteCredit() {
        credit = creditsListView.getSelectionModel().getSelectedItem();
        if(credit == null){
            return;
        }
        popupDelete();
    }
}
