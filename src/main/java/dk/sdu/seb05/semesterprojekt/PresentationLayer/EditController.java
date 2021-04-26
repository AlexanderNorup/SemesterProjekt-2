package dk.sdu.seb05.semesterprojekt.PresentationLayer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
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
    private JFXButton backButton;
    @FXML
    private JFXButton removeProgramButton;
    @FXML
    private JFXButton removeCreditButton;
    @FXML
    private JFXButton addCreditButton;
    @FXML
    private JFXListView<String> programsListView;

    public void initialize(){
        fulcrum = PresentationSingleton.getInstance();
        fulcrum.setTitle("Rediger programmer");
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

    public void deleteCreditHandler() {
        fulcrum.changeView("deletecreditpage");
    }

    public void addCreditHandler() {
        fulcrum.changeView("createcreditpage");
    }
}
