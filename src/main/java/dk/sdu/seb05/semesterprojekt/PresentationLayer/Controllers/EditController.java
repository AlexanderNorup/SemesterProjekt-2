package dk.sdu.seb05.semesterprojekt.PresentationLayer.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProgramme;
import dk.sdu.seb05.semesterprojekt.PresentationLayer.PresentationSingleton;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class EditController {

    PresentationSingleton presentationSingleton;

    @FXML
    private JFXButton editProgramButton;
    @FXML
    private StackPane stackPane;
    @FXML
    private JFXButton backButton;
    @FXML
    private JFXButton removeProgramButton;
    @FXML
    private JFXButton editCreditButton;
    @FXML
    private JFXListView<IProgramme> programsListView;

    IProgramme programme;

    public void initialize(){
        presentationSingleton = PresentationSingleton.getInstance();
        presentationSingleton.setTitle("Rediger programmer");

        programsListView.setItems(FXCollections.observableArrayList(
                presentationSingleton.getDomainLayer().getProgrammes(presentationSingleton.getDomainLayer().getSession().getProducerID())
        ));
        programsListView.getStyleClass().add("mylistview");
    }

    private void popup(){
        stackPane.setVisible(true);
        removeProgramButton.setDisable(true);
        editCreditButton.setDisable(true);
        backButton.setDisable(true);
        editProgramButton.setDisable(true);
        JFXDialogLayout content = new JFXDialogLayout();
        Text headingText = new Text("Ønsker du at fjerne følgende program?");
        String successString = programme.getName() + " - " + programme.getChannel();
        Text bodyText = new Text(successString);
        headingText.getStyleClass().add("popupTextColor");
        bodyText.getStyleClass().add("popupTextColor");
        content.setHeading(headingText);
        content.setBody(bodyText);
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton confirmButton = new JFXButton("Fjern program");
        confirmButton.setStyle("-fx-background-color: #ef0000; -fx-font-family: \"Roboto Bold\"; -fx-font-size: 14;"); //makes button go red
        JFXButton cancelButton = new JFXButton("Gå tilbage");
        dialog.setOverlayClose(false);
        //goes back to edit page
        cancelButton.setOnAction(event -> {
            dialog.close();
        });
        //goes to edit page after deleting program
        confirmButton.setOnAction(event -> {
            programsListView.getItems().remove(programme);
            presentationSingleton.getDomainLayer().deleteProgramme(programme.getId());
            presentationSingleton.getDomainLayer().commit();
            dialog.close();
        });
        //goes back to the current page to create a new program
        dialog.setOnDialogClosed(event -> {
            stackPane.setVisible(false);
            removeProgramButton.setDisable(false);
            editCreditButton.setDisable(false);
            backButton.setDisable(false);
            editProgramButton.setDisable(false);
        });
        content.setActions(cancelButton, confirmButton);
        dialog.show();
    }

    public void removeProgramHandler() {
        programme = programsListView.getSelectionModel().getSelectedItem();
        if(programme == null){
            return;
        }
        popup();
    }


    public void returnHandler() throws IOException {
        presentationSingleton.goToFrontPage();
    }

    public void editCreditHandler() {
        IProgramme programme = programsListView.getSelectionModel().getSelectedItem();
        if(programme == null){
            return;
        }
        presentationSingleton.changeView("createcreditpage", programme);
    }

    public void editProgramHandler() {
        IProgramme programme = programsListView.getSelectionModel().getSelectedItem();
        if(programme == null){
            return;
        }
        presentationSingleton.changeView("createpage", programme);
    }
}
