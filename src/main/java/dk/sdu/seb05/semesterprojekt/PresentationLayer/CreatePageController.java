package dk.sdu.seb05.semesterprojekt.PresentationLayer;

import com.jfoenix.controls.*;
import com.jfoenix.controls.events.JFXDialogEvent;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.Category;
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

public class CreatePageController {
    public static PresentationSingleton fulcrum;

    @FXML
    private StackPane stackPane;
    @FXML
    private JFXComboBox<Category> categoryComboBox;
    @FXML
    private JFXButton createProgramButton;
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
    private JFXButton backButton;

    public void initialize() {
        fulcrum = PresentationSingleton.getInstance();
        fulcrum.setTitle("Opret nyt program");
        Category[] categories = Category.values();
        categoryComboBox.getItems().addAll(categories);
    }

    public void returnHandler() throws IOException {
        fulcrum.goToFrontPage();
    }

    public Date datePicker() {
        LocalDate localDate = sendDate.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);
        chosenLabel.setVisible(true);
        String test = localDate.getDayOfMonth() + ". " + monthToString(localDate.getMonthValue()) + " - " + localDate.getYear();
        dateLabel.setText(test);
        return date;
    }

    private void popupSuccess(){
        stackPane.setVisible(true);
        createProgramButton.setDisable(true);
        backButton.setDisable(true);
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Der er oprettet et program!"));
        String successString = "Du har oprettet følgende program: \n" +
                "Program navn: " + programTitleTextField.getText() + "\n" +
                "Kategori: " + categoryComboBox.getValue() + "\n" +
                "Valgt kanal: " + channelTextField.getText() + "\n" +
                "Dato: " + dateLabel.getText() + "\n";
        content.setBody(new Text(successString));
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton confirmButton = new JFXButton("Opret credits");
        JFXButton frontPageButton = new JFXButton("Tilbage til forside");
        JFXButton createPageButton = new JFXButton("Opret nyt program");
        dialog.setOverlayClose(false);
        frontPageButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    fulcrum.goToFrontPage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        confirmButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                fulcrum.changeView("createcreditpage");
            }
        });
        createPageButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                fulcrum.changeView("createpage");
            }
        });
        dialog.setOnDialogClosed(new EventHandler<>() {
            @Override
            public void handle(JFXDialogEvent events) {
                stackPane.setVisible(false);
            }
        });
        content.setActions(frontPageButton, createPageButton ,confirmButton);
        dialog.show();

    }

    private void popupError(String errorText){
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

    public void createProgramHandler() {
        if(errorHandler()){
            System.out.println("Something needs filling in!");
        }
        else if (!errorHandler()) {
            fulcrum.getDomainLayer().createProgramme(programTitleTextField.getText(),
                    categoryComboBox.getValue(),
                    channelTextField.getText(),
                    datePicker());
            System.out.println("Du har oprettet følgende program: \n" +
                    "Program navn: " + programTitleTextField.getText() + "\n" +
                    "Kategori: " + categoryComboBox.getValue() + "\n" +
                    "Valgt kanal: " + channelTextField.getText() + "\n" +
                    "Dato: " + dateLabel.getText() + "\n"
            );
            popupSuccess();
        }
    }

    public String monthToString(int month){
        String returnMonth;
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
