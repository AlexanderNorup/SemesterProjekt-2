package dk.sdu.seb05.semesterprojekt.PresentationLayer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;

public class SearchPageController {

    @FXML
    private TextField searchTextField;
    @FXML
    private Button searchButton;
    @FXML
    private Button backButton;
    @FXML
    private ListView<String> programListView;
    @FXML
    private Button creditButton;

    public static PresentationSingleton fulcrum;


    public void initialize(){
        fulcrum = PresentationSingleton.getInstance();
        ObservableList<String> thing = FXCollections.observableArrayList(fulcrum.getSearch());
        programListView.setItems(thing);
    }

    public void chooseHandler() throws IOException {
        System.out.println("Du har valgt følgende program: " + programListView.getSelectionModel().getSelectedItem());
        fulcrum.setName(programListView.getSelectionModel().getSelectedItem());
        Parent creditPage = FXMLLoader.load(JavaFXTest.class.getResource("/fxml/creditpage.fxml"));
        fulcrum.getPrimaryStage().setScene(new Scene(creditPage));
        fulcrum.getPrimaryStage().setTitle("Credits til " + programListView.getSelectionModel().getSelectedItem());
    }

    public void searchHandler() throws IOException {
        String searchText = searchTextField.getText();
        System.out.println(searchText);
        fulcrum.setSearch(searchText);


        Parent searchPage = FXMLLoader.load(JavaFXTest.class.getResource("/fxml/searchpage.fxml"));
        fulcrum.getPrimaryStage().setScene(new Scene(searchPage));
        fulcrum.getPrimaryStage().setTitle("Søge resultater");
    }

    public void returnHandler() throws IOException {
        fulcrum.goToFrontPage();

    }
}
