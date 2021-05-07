package dk.sdu.seb05.semesterprojekt.PresentationLayer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProgramme;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.io.IOException;

public class EditController {

    PresentationSingleton fulcrum;

    @FXML
    private StackPane stackPane;
    @FXML
    private JFXButton backButton;
    @FXML
    private JFXButton removeProgramButton;
    @FXML
    private JFXButton addCreditButton;
    @FXML
    private JFXListView<IProgramme> programsListView;

    IProgramme programme;

    public void initialize(){
        fulcrum = PresentationSingleton.getInstance();
        fulcrum.setTitle("Rediger programmer");

        programsListView.setItems(FXCollections.observableArrayList(
                fulcrum.getDomainLayer().getProgrammes(fulcrum.getDomainLayer().getSession().getProducerID())
        ));
        programsListView.getStyleClass().add("mylistview");
        //if you double click an item, you will see credits for that item
        programsListView.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2){
                    addCreditHandler();
            }
        });
    }

    private void popup(){
        stackPane.setVisible(true);
        removeProgramButton.setDisable(true);
        addCreditButton.setDisable(true);
        backButton.setDisable(true);
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
            fulcrum.getDomainLayer().deleteProgramme(programme.getId());
            fulcrum.getDomainLayer().commit();
            dialog.close();
        });
        //goes back to the current page to create a new program
        dialog.setOnDialogClosed(event -> {
            stackPane.setVisible(false);
            removeProgramButton.setDisable(false);
            addCreditButton.setDisable(false);
            backButton.setDisable(false);
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
        fulcrum.goToFrontPage();
    }

    public void addCreditHandler() {
        IProgramme programme = programsListView.getSelectionModel().getSelectedItem();
        if(programme == null){
            return;
        }
        fulcrum.changeView("createcreditpage", programme);
    }
}
