package dk.sdu.seb05.semesterprojekt.PresentationLayer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;

public class FrontPageController {

    public static PresentationSingleton fulcrum;

    @FXML
    private ToggleGroup sessionGroup;
    @FXML
    private RadioButton userButton;
    @FXML
    private RadioButton adminButton;
    @FXML
    private RadioButton producerButton;
    @FXML
    private ListView<String> notificationListView;
    @FXML
    private ListView<String> programListView;
    @FXML
    private ToggleButton notificationButton;
    @FXML
    private TextField searchTextField;
    @FXML
    private Label programLabel;
    @FXML
    private Button editButton;
    @FXML
    private Button searchButtonTextField;
    @FXML
    private Button searchButtonListView;


    public void initialize(){
        fulcrum = PresentationSingleton.getInstance();
        System.out.println("initialize(); kører i JavaFXTestController");
        ObservableList<String> programs = FXCollections.observableArrayList(
                "(2019) Badehotellet - TV 2",
                "(1978) Matador - DR 2",
                "(1953) Far til fire - DR 1",
                "(2021) Stormester - TV 2 Zulu",
                "(2010) Natholdet - TV 2",
                "(2008) Bonderøven - TV 2 ",
                "(2018) Hvem vil være millionær? - TV 2 Charlie",
                "(2005) Klovn - TV 2 Zulu",
                "(2000) Blinkende Lygter - DR 1");
        programListView.setItems(programs);

        ObservableList<String> notifications = FXCollections.observableArrayList(
                "Der er 3 nye ændringer i dit program \"(2019) Badehotellet\"",
                "Der er 69 nye ændringer i dit program \"(2010) Natholdet\"",
                "Der er blevet slettet 5 personer fra dit program \"(2005) Klovn \"");
        notificationListView.setItems(notifications);
        radioHandler();

    }

    public void searchRecentHandler() throws IOException {
        String selected = programListView.getSelectionModel().getSelectedItem();
        System.out.println("Du har valgt følgende program: " + selected);
        fulcrum.setName(selected);
        Parent creditPage = FXMLLoader.load(JavaFXTest.class.getResource("/fxml/creditpage.fxml"));
        fulcrum.getPrimaryStage().setScene(new Scene(creditPage));
        fulcrum.getPrimaryStage().setTitle("Credits til " + selected);
    }

    public void showNotifications(){
        notificationListView.setVisible(!notificationListView.isVisible());
    }

    public void radioHandler() {
        if(producerButton.isSelected() || adminButton.isSelected()){
            notificationButton.setVisible(true);
            editButton.setVisible(true);
        }
        if(userButton.isSelected()){
            notificationButton.setVisible(false);
            notificationListView.setVisible(false);
            notificationButton.setSelected(false);
            editButton.setVisible(false);
        }
    }

    public void searchHandler() throws IOException {
        String searchText = searchTextField.getText();
        System.out.println("Du søgte efter: " + searchText);
        fulcrum.setSearch(searchText);

        Parent searchPage = FXMLLoader.load(JavaFXTest.class.getResource("/fxml/searchpage.fxml"));
        fulcrum.getPrimaryStage().setScene(new Scene(searchPage));
        fulcrum.getPrimaryStage().setTitle("Søge resultater");

    }

    public void editHandler() {
        //TODO
        /*fulcrum.getSessionID();
        fori(sessionID){
        getAllPrograms()
        }
        */
    }
}
