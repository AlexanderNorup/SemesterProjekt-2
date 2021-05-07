package dk.sdu.seb05.semesterprojekt.PresentationLayer;

import com.jfoenix.controls.*;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.Category;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProgramme;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class CreatePageController implements ViewArgumentAdapter {
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

    private boolean isUpdating;

    IProgramme programme;

    public void onLaunch(Object o) {
        fulcrum = PresentationSingleton.getInstance();
        fulcrum.setTitle("Opret nyt program");
        isUpdating = false;
        Category[] categories = fulcrum.getDomainLayer().getCategories();
        categoryComboBox.getItems().addAll(categories);
        if(o instanceof IProgramme){
            programme = (IProgramme) o;
            programTitleTextField.setText(programme.getName());
            categoryComboBox.getSelectionModel().select(programme.getCategory());
            LocalDate programmeLocalDate = programme.getAiredDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            sendDate.setValue(programmeLocalDate);
            channelTextField.setText(programme.getChannel());
            createProgramButton.setText("Opdater program");
            isUpdating = true;
        }
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
        Text headingText;
        String context;
        if(isUpdating) {
            headingText = new Text("Der er opdateret et program!");
            context = "opdateret";
        }
        else{
            headingText = new Text("Der er oprettet et program!");
            context = "oprettet";
        }
        String successString = "Du har " + context + " følgende program: \n" +
                "Program navn: " + programTitleTextField.getText() + "\n" +
                "Kategori: " + categoryComboBox.getValue() + "\n" +
                "Valgt kanal: " + channelTextField.getText() + "\n" +
                "Dato: " + dateLabel.getText() + "\n";
        Text bodyText = new Text(successString);
        headingText.getStyleClass().add("popupTextColor");
        bodyText.getStyleClass().add("popupTextColor");
        content.setHeading(headingText);
        content.setBody(bodyText);
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton confirmButton = new JFXButton("Tilføj credits");
        JFXButton frontPageButton = new JFXButton("Tilbage til forside");
        JFXButton createPageButton = new JFXButton("Opret nyt program");
        dialog.setOverlayClose(false);
        //goes to front page
        frontPageButton.setOnAction(event -> {
            try {
                fulcrum.goToFrontPage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        //goes to createcredit page for the created program
        confirmButton.setOnAction(event -> {
            if(programme == null){
                return;
            }
            fulcrum.changeView("createcreditpage", programme);
        });
        //goes back to the current page to create a new program
        createPageButton.setOnAction(event -> fulcrum.changeView("createpage"));
        dialog.setOnDialogClosed(event -> stackPane.setVisible(false));
        content.setActions(frontPageButton, createPageButton ,confirmButton);
        dialog.show();

    }

    private void popupError(String errorText){
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
        button.setOnAction(event -> dialog.close());
        dialog.setOnDialogClosed(events -> stackPane.setVisible(false));
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
        else {
            if (isUpdating) {
                fulcrum.getDomainLayer().updateProgramme(programme.getId(),
                        datePicker(),
                        categoryComboBox.getValue(),
                        channelTextField.getText(),
                        programTitleTextField.getText());
            } else {
                programme = fulcrum.getDomainLayer().createProgramme(programTitleTextField.getText(),
                        categoryComboBox.getValue(),
                        channelTextField.getText(),
                        datePicker());
                System.out.println("Du har oprettet følgende program: \n" +
                        "Program navn: " + programTitleTextField.getText() + "\n" +
                        "Kategori: " + categoryComboBox.getValue() + "\n" +
                        "Valgt kanal: " + channelTextField.getText() + "\n" +
                        "Dato: " + dateLabel.getText() + "\n"
                );
            }
            fulcrum.getDomainLayer().commit();
            popupSuccess();
        }
    }

    public String monthToString(int month){
        return switch (month) {
            case 1 -> "Januar";
            case 2 -> "Februar";
            case 3 -> "Marts";
            case 4 -> "April";
            case 5 -> "Maj";
            case 6 -> "Juni";
            case 7 -> "Juli";
            case 8 -> "August";
            case 9 -> "September";
            case 10 -> "Oktober";
            case 11 -> "November";
            case 12 -> "December";
            default -> throw new IllegalArgumentException("Unexpected value: " + month);
        };
    }

}
