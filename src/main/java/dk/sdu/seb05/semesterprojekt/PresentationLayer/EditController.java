package dk.sdu.seb05.semesterprojekt.PresentationLayer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProgramme;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;

import java.io.IOException;

public class EditController {

    PresentationSingleton fulcrum;

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
