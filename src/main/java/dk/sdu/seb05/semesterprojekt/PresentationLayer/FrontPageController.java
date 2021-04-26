package dk.sdu.seb05.semesterprojekt.PresentationLayer;
import com.jfoenix.controls.*;
import com.jfoenix.controls.events.JFXDialogEvent;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProgramme;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;

public class FrontPageController {

    public static PresentationSingleton fulcrum;

    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private StackPane stackPane;
    @FXML
    private JFXToggleButton toggleDarkModeButton;
    @FXML
    private JFXButton searchButton;
    @FXML
    private JFXComboBox<String> producerDropdown;
    @FXML
    private JFXButton darkModeButton;
    @FXML
    private JFXRadioButton personButton;
    @FXML
    private JFXRadioButton programButton;
    @FXML
    private JFXRadioButton producentButton;
    @FXML
    private ToggleGroup searchGroup;
    @FXML
    private ToggleGroup sessionGroup;
    @FXML
    private JFXRadioButton userButton;
    @FXML
    private JFXRadioButton adminButton;
    @FXML
    private JFXRadioButton producerButton;
    @FXML
    private ListView<String> notificationListView;
    @FXML
    private ListView<IProgramme> programListView;
    @FXML
    private TextField searchTextField;
    @FXML
    private JFXButton notificationButton;
    @FXML
    private JFXButton editButton;
    @FXML
    private JFXButton createButton;
    @FXML
    private JFXButton searchButtonListView;

    private int searchTypeId = 2;

    private int darkModeCounter = 0;

    public void initialize(){
        fulcrum = PresentationSingleton.getInstance();
        ObservableList<IProgramme> programs = FXCollections.observableArrayList(fulcrum.getDomainLayer().getLatestProgrammes());
        programListView.setItems(programs);  //gets current programs and sets them to the listview
        searchTextField.setText(""); //makes the search field empty
        producerDropdown.setVisible(false);
        programButton.setSelected(true);
        userButton.setSelected(true);
        ObservableList<String> notifications = FXCollections.observableArrayList(
                "Der er 3 nye ændringer i dit program \"(2019) Badehotellet\"",
                "Der er 69 nye ændringer i dit program \"(2010) Natholdet\"",
                "Der er blevet slettet 5 personer fra dit program \"(2005) Klovn \"");
        notificationListView.setItems(notifications);
        String[] producers = {"TV 2", "Disney Channel", "DR1"};
        producerDropdown.getItems().addAll(producers);
        radioHandler();
        programListView.getStyleClass().add("mylistview"); //adds a user defined style class from the css file, as it's style (makes it pretty)
        programListView.setOnMouseClicked(new EventHandler<MouseEvent>() { //if you double click an item, you will see credits for that item
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

        searchTextField.setOnKeyPressed(new EventHandler<KeyEvent>() { //possible to press enter to search
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

    /**
     * Method to show notifications
     * TODO
     * Right now it's just a list with some static strings
     */
    public void showNotifications(){
        notificationListView.setVisible(!notificationListView.isVisible());
    }

    public void radioHandler() {
        if(userButton.isSelected()){
            notificationButton.setVisible(false);
            notificationListView.setVisible(false);
            //notificationButton.setSelected(false);
            editButton.setVisible(false);
            createButton.setVisible(false);
            producerDropdown.setVisible(false);
            producerDropdown.setValue(null);
        }
        if(producerButton.isSelected()){
            notificationButton.setVisible(true);
            editButton.setVisible(true);
            createButton.setVisible(true);
            producerDropdown.setVisible(true);
            producerDropdown.setValue(null);
        }
        if(adminButton.isSelected()){
            notificationButton.setVisible(true);
            editButton.setVisible(true);
            createButton.setVisible(true);
            producerDropdown.setVisible(true);
            producerDropdown.setValue(null);
        }
    }

    /**
     * Shows a popup if the producer/admin using the program is not logged in, but tries to use one of three buttons:
     * notificationButton, editButton or createButton.
     */
    private void popupError(){
        stackPane.setVisible(true);
        //disabling the three buttons so they can't be spam clicked when popup is shown
        notificationButton.setDisable(true);
        createButton.setDisable(true);
        editButton.setDisable(true);
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Der skete en fejl!"));
        content.setBody(new Text("Du er ikke logget ind!"));

        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton button = new JFXButton("Okay");
        button.setOnAction(new EventHandler<ActionEvent>() { //sets the "Okay" button to close the popup
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        //whenever the popup gets closed, the associated StackPane gets set to invisible, so it does not interfere
        dialog.setOnDialogClosed(new EventHandler<JFXDialogEvent>() {
            @Override
            public void handle(JFXDialogEvent event) {
                stackPane.setVisible(false);
                //enabling the buttons again
                notificationButton.setDisable(false);
                createButton.setDisable(false);
                editButton.setDisable(false);
            }
        });
        content.setActions(button);
        dialog.show();

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
        if(searchText == null || searchText.trim().isEmpty()){
            return;
        }
        System.out.println("Du søgte efter: " + searchText);
        fulcrum.setSearchText(searchText);
        fulcrum.setSearchType(searchTypeId);
        List results = fulcrum.getDomainLayer().search(searchTypeId, searchText);

        fulcrum.changeView("searchpage", results);
    }

    public void editHandler() {
        if(producerDropdown.getSelectionModel() == null || producerDropdown.getSelectionModel().isEmpty()){
            popupError();
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
            popupError();
        }
        else {
            fulcrum.changeView("createpage");
        }
    }

    public void setupUI(){
        fulcrum.getPrimaryStage().getScene().getStylesheets().setAll(String.valueOf(getClass().getResource("/css/style.css")));
    }

    /**
     * TODO
     * Method to enable darkMode. Still very basic and only for frontpage
     */
    public void darkMode(){
        darkModeCounter++;
        System.out.println(darkModeCounter);
        if(darkModeCounter % 2 != 0){
            System.out.println("I tried to make it dark");
            fulcrum.getPrimaryStage().getScene().getStylesheets().setAll(String.valueOf(getClass().getResource("/css/darkmode.css")));
        }
        if(darkModeCounter % 2 == 0){
            System.out.println("I tried to make it light");
            fulcrum.getPrimaryStage().getScene().getStylesheets().setAll(String.valueOf(getClass().getResource("/css/style.css")));
        }
    }

}
