package dk.sdu.seb05.semesterprojekt.PresentationLayer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProgramme;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

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
    private JFXListView<IProgramme> programsListView;

    IProgramme programme;

    public void initialize(){
        fulcrum = PresentationSingleton.getInstance();
        fulcrum.setTitle("Rediger programmer");

        programsListView.setItems(FXCollections.observableArrayList(
                fulcrum.getDomainLayer().getProgrammes(fulcrum.getDomainLayer().getSession().getProducerID())
        ));
        System.out.println(fulcrum.getName());
        programsListView.setOnMouseClicked(new EventHandler<MouseEvent>() { //if you double click an item, you will see credits for that item
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                        addCreditHandler();
                }
            }
        });
    }

    public void removeProgramHandler() {
        programme = programsListView.getSelectionModel().getSelectedItem();
        if(programme == null){
            return;
        }
        programsListView.getItems().remove(programme);
        fulcrum.getDomainLayer().deleteProgramme(programme.getId());
        fulcrum.getDomainLayer().commit();
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
