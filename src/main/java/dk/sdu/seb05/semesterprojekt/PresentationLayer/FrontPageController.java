package dk.sdu.seb05.semesterprojekt.PresentationLayer;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProgramme;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.List;

public class FrontPageController {

    public static PresentationSingleton fulcrum;

    @FXML
    private ChoiceBox<String> producerDropdown; //Skal ændres til noget med <Producer> på et tidspunkt
    @FXML
    private RadioButton personButton;
    @FXML
    private RadioButton programButton;
    @FXML
    private RadioButton producentButton;
    @FXML
    private ToggleGroup searchGroup;
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
    private ListView<IProgramme> programListView;
    @FXML
    private ToggleButton notificationButton;
    @FXML
    private TextField searchTextField;
    @FXML
    private Label programLabel;
    @FXML
    private Button editButton;
    @FXML
    private Button createButton;
    @FXML
    private Button searchButtonTextField;
    @FXML
    private Button searchButtonListView;

    private int searchTypeId = 2;

    public void initialize(){
        fulcrum = PresentationSingleton.getInstance();
        ObservableList<IProgramme> programs = FXCollections.observableArrayList(fulcrum.getDomainLayer().getLatestProgrammes());
        programListView.setItems(programs);
        searchTextField.setText("");
        producerDropdown.setVisible(false);
        ObservableList<String> notifications = FXCollections.observableArrayList(
                "Der er 3 nye ændringer i dit program \"(2019) Badehotellet\"",
                "Der er 69 nye ændringer i dit program \"(2010) Natholdet\"",
                "Der er blevet slettet 5 personer fra dit program \"(2005) Klovn \"");
        notificationListView.setItems(notifications);
        String[] producers = {"TV 2", "Disney Channel", "DR1"};
        producerDropdown.getItems().addAll(producers);
        radioHandler();


        programListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    try {
                        searchRecentHandler();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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

    public void searchRecentHandler() throws IOException {
        IProgramme programme = programListView.getSelectionModel().getSelectedItem();
        if(programme == null){
            return;
        }
        System.out.println("Du har valgt følgende program: " + programme.getName());
        fulcrum.changeView("creditpage", programListView.getSelectionModel().getSelectedItem());
    }
    public void showNotifications(){
        notificationListView.setVisible(!notificationListView.isVisible());
    }

    public void radioHandler() {
        if(userButton.isSelected()){
            notificationButton.setVisible(false);
            notificationListView.setVisible(false);
            notificationButton.setSelected(false);
            editButton.setVisible(false);
            createButton.setVisible(false);
            producerDropdown.setVisible(false);
            producerDropdown.setValue(" ");
        }
        if(producerButton.isSelected()){
            notificationButton.setVisible(true);
            editButton.setVisible(true);
            createButton.setVisible(true);
            producerDropdown.setVisible(true);
            producerDropdown.setValue(" ");
        }
        if(adminButton.isSelected()){
            notificationButton.setVisible(true);
            editButton.setVisible(true);
            createButton.setVisible(true);
            producerDropdown.setVisible(true);
            producerDropdown.setValue(" ");
        }
    }

    private void errorAlert(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/css/dialog-pane-styles-edit.css")
                        .toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        alert.setTitle("LOGIN");
        alert.setHeaderText("MANGLER LOGIN");
        alert.setContentText("Du har ikke valgt login ude til højre \n\n" + "LOG NU IND FÖR HELVETE!");
        alert.showAndWait();
    }

    public void searchRadioHandler() {
        if(programButton.isSelected()){
            searchTextField.setPromptText("Indtast navn på programmet");
            searchTypeId = 2;
        }
        if(personButton.isSelected()){
            searchTextField.setPromptText("Indtast navn på en person");
            searchTypeId = 0;
        }
        if(producentButton.isSelected()){
            searchTextField.setPromptText("Indtast navnet på producenten");
            searchTypeId = 1;
        }
    }

    public void searchHandler() {
        String searchText = searchTextField.getText();
        System.out.println("Du søgte efter: " + searchText);
        fulcrum.setSearch(searchText);
        fulcrum.setID(searchTypeId);
        List results = fulcrum.getDomainLayer().search(searchTypeId, searchText);

        fulcrum.changeView("searchpage", results);
    }

    public void editHandler() {
        if(producerDropdown.getSelectionModel() == null || producerDropdown.getSelectionModel().isEmpty()){
            errorAlert();
        }
        else{
            String producer = producerDropdown.getValue();
            System.out.println(producer);
            fulcrum.setName(producer);
            fulcrum.changeView("editpage");
        }
    }

    public void createHandler() {
        if(producerDropdown.getSelectionModel() == null || producerDropdown.getSelectionModel().isEmpty()){
            errorAlert();
        }
        else {
            fulcrum.changeView("createpage");
        }
    }
}
