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
    private Button createButton;
    @FXML
    private Button searchButtonTextField;
    @FXML
    private Button searchButtonListView;

    private int id;

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
            id = 2;
        }
        if(personButton.isSelected()){
            searchTextField.setPromptText("Indtast navn på en person");
            id = 0;
        }
        if(producentButton.isSelected()){
            searchTextField.setPromptText("Indtast navnet på producenten");
            id = 1;
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

    public void editHandler() throws IOException {
        if(producerDropdown.getSelectionModel() == null || producerDropdown.getSelectionModel().isEmpty()){
            errorAlert();
        }
        else{
            String producer = producerDropdown.getValue();
            System.out.println(producer);
            fulcrum.setName(producer);

            Parent editPage = FXMLLoader.load(JavaFXTest.class.getResource("/fxml/editpage.fxml"));
            fulcrum.getPrimaryStage().setScene(new Scene(editPage));
            fulcrum.getPrimaryStage().setTitle("Rediger programmer");
        }
    }

    public void createHandler() throws IOException {
        if(producerDropdown.getSelectionModel() == null || producerDropdown.getSelectionModel().isEmpty()){
            errorAlert();
        }
        else {
            Parent createPage = FXMLLoader.load(JavaFXTest.class.getResource("/fxml/createpage.fxml"));
            fulcrum.getPrimaryStage().setScene(new Scene(createPage));
            fulcrum.getPrimaryStage().setTitle("Opret nyt program");
        }
    }
}
