package dk.sdu.seb05.semesterprojekt.PresentationLayer;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;

public class EditController {

    PresentationSingleton fulcrum;

    @FXML
    private Button removeProgramButton;
    @FXML
    private Button removeCreditButton;
    @FXML
    private Button addCreditButton;
    @FXML
    private ListView<String> programsListView;

    public void initialize(){
        fulcrum = PresentationSingleton.getInstance();
        System.out.println(fulcrum.getName());
        if (fulcrum.getName().equals("DR1")) {
            programsListView.setItems(FXCollections.observableArrayList(
                    "(1953) Far til fire - DR 1",
                    "(2000) Blinkende Lygter - DR 1"));
        }
        if(fulcrum.getName().equals("TV 2")){
            programsListView.setItems(FXCollections.observableArrayList(
                    "(2019) Badehotellet - TV 2",
                    "(2010) Natholdet - TV 2",
                    "(2008) Bonderøven - TV 2 "));
        }
        if(fulcrum.getName().equals("Disney Channel")){
            programsListView.setItems(FXCollections.observableArrayList(
                    "(2007) Phineas og Ferb - Disney Channel"));
        }
    }

    public void removeProgramHandler() {
        programsListView.getItems().remove(programsListView.getSelectionModel().getSelectedItem());
    }


    public void returnHandler() throws IOException {
        fulcrum.goToFrontPage();
    }

    public void deleteCreditHandler() throws IOException {
        Parent deleteCreditPage = FXMLLoader.load(JavaFXTest.class.getResource("/fxml/deletecreditpage.fxml"));
        fulcrum.getPrimaryStage().setScene(new Scene(deleteCreditPage));
        fulcrum.getPrimaryStage().setTitle("Fjern credits");
    }

    public void addCreditHandler() throws IOException {
        Parent createCreditPage = FXMLLoader.load(JavaFXTest.class.getResource("/fxml/createcreditpage.fxml"));
        fulcrum.getPrimaryStage().setScene(new Scene(createCreditPage));
        fulcrum.getPrimaryStage().setTitle("Tilføj credits");
    }
}
