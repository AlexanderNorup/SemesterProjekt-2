package dk.sdu.seb05.semesterprojekt.PresentationLayer;

import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProgramme;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.List;

public class SearchPageController implements ViewArgumentAdapter {

    @FXML
    private TextField searchTextField;
    @FXML
    private Button searchButton;
    @FXML
    private Button backButton;
    @FXML
    private ListView<Object> programListView;
    @FXML
    private Button creditButton;

    public static PresentationSingleton fulcrum;


    @Override
    public void onLaunch(Object o) {
        fulcrum = PresentationSingleton.getInstance();
        fulcrum.setTitle("Søgeresultater");
        searchTextField.setText(fulcrum.getSearch());

        List<Object> objects = (List<Object>) o;
        ObservableList<Object> thing = FXCollections.observableArrayList(objects);
        programListView.setItems(thing);

        programListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    chooseHandler();
                }
            }
        });

        searchTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER){
                    searchHandler();
                }
            }
        });

    }


    public void chooseHandler() {
        Object chosen =  programListView.getSelectionModel().getSelectedItem();
        if(chosen == null){
            return;
        }
        System.out.println("Du har valgt følgende element: " + chosen);
        fulcrum.changeView("creditpage", chosen);
    }

    public void searchHandler() {
        String searchText = searchTextField.getText();
        System.out.println("Du søgte efter: " + searchText);
        fulcrum.setSearch(searchText);
        //TODO: Change chosen search type here.
        List results = fulcrum.getDomainLayer().search(2, searchText);

        fulcrum.changeView("searchpage", results);
    }

    public void returnHandler() throws IOException {
        fulcrum.goToFrontPage();
    }

}
