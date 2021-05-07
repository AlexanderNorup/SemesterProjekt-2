package dk.sdu.seb05.semesterprojekt.PresentationLayer;
import com.jfoenix.controls.*;
import dk.sdu.seb05.semesterprojekt.DomainLayer.Session;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProducer;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProgramme;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
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
    private JFXComboBox<IProducer> producerDropdown;
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

    private boolean darkMode = false;

    public void initialize(){
        fulcrum = PresentationSingleton.getInstance();
        darkMode = fulcrum.isDarkMode();
        ObservableList<IProgramme> programs = FXCollections.observableArrayList(fulcrum.getDomainLayer().getLatestProgrammes());
        programListView.setItems(programs);  //gets current programs and sets them to the listview
        searchTextField.setText(""); //makes the search field empty
        ObservableList<String> notifications = FXCollections.observableArrayList(
                "Der er 3 nye ændringer i dit program \"(2019) Badehotellet\"",
                "Der er 69 nye ændringer i dit program \"(2010) Natholdet\"",
                "Der er blevet slettet 5 personer fra dit program \"(2005) Klovn \"",
                "\n\n\n\n \t\t\t\t\t\t *** WORK IN PROGRESS ***");
        notificationListView.setItems(notifications);
        ObservableList<IProducer> producers = FXCollections.observableArrayList(fulcrum.getDomainLayer().getProducers());
        producerDropdown.getItems().addAll(producers);
        programListView.getStyleClass().add("mylistview"); //adds a user defined style class from the css file, as it's style (makes it pretty)
        setLoggedIn();
        radioHandler();
        toggleDarkModeButton.setSelected(darkMode);
        //if you double click an item, you will see credits for that item
        programListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                try {
                    searchRecentHandler();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //possible to press enter to search
        searchTextField.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                searchHandler();
            }
        });

    }

    private void setLoggedIn(){
        Session currentSession = fulcrum.getDomainLayer().getSession();
        if(currentSession.isAdmin()) {
            adminButton.setSelected(true);
            return;
        }
        switch (currentSession.getProducerID()){
            case -2:
                userButton.setSelected(true);
                break;
            default:
                producerButton.setSelected(true);
                IProducer producer = fulcrum.getDomainLayer().chooseProducer(currentSession.getProducerID());
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        producerDropdown.getSelectionModel().select(producer);
                    }
                });
        }
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
            editButton.setVisible(false);
            createButton.setVisible(false);
            producerDropdown.setVisible(false);
            producerDropdown.setValue(null);
            fulcrum.getDomainLayer().setSession(0, -2); // auth = 0 -> user, id=-2 -> user
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
            producerDropdown.setVisible(false);
            producerDropdown.setValue(null);
            fulcrum.getDomainLayer().setSession(2, -1); // auth = 2 -> admin, id=-1 -> admin
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
        Text headingText = new Text("Der skete en fejl!");
        Text bodyText = new Text("Du er ikke logget ind!");
        headingText.getStyleClass().add("popupTextColor");
        bodyText.getStyleClass().add("popupTextColor");
        content.setHeading(headingText);
        content.setBody(bodyText);

        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton button = new JFXButton("Okay");
        //sets the "Okay" button to close the popup
        button.setOnAction(event -> dialog.close());
        //whenever the popup gets closed, the associated StackPane gets set to invisible, so it does not interfere
        dialog.setOnDialogClosed(event -> {
            stackPane.setVisible(false);
            //enabling the buttons again
            notificationButton.setDisable(false);
            createButton.setDisable(false);
            editButton.setDisable(false);
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
        if(fulcrum.getDomainLayer().getSession().getProducerID() == -2){
            popupError();
        }
        else{
            int producer = fulcrum.getDomainLayer().getSession().getProducerID();
            System.out.println(producer);
            fulcrum.changeView("editpage");
        }
    }

    public void createHandler() {
        if(fulcrum.getDomainLayer().getSession().getProducerID() == -2){
            popupError();
        }
        else {
            fulcrum.changeView("createpage");
        }
    }

    /**
     * TODO
     * Method to enable darkMode. Still very basic and only for frontpage
     */
    public void darkMode(){
        darkMode = !darkMode;
        fulcrum.setDarkMode(darkMode);
        if(darkMode){
            fulcrum.getPrimaryStage().getScene().getStylesheets().setAll(String.valueOf(getClass().getResource("/css/darkmode.css")));
        }
        else {
            fulcrum.getPrimaryStage().getScene().getStylesheets().setAll(String.valueOf(getClass().getResource("/css/style.css")));
        }
    }

    public void sessionHandler() {
        String sessionType = (String) sessionGroup.getSelectedToggle().getUserData();
        int sessionTypeID = Integer.parseInt(sessionType);
        if (producerDropdown.getValue() != null){
            fulcrum.getDomainLayer().setSession(sessionTypeID, producerDropdown.getValue().getId());
        }
    }
}
