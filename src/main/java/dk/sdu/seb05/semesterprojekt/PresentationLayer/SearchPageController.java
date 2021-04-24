package dk.sdu.seb05.semesterprojekt.PresentationLayer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXRadioButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.List;

public class SearchPageController implements ViewArgumentAdapter {

    public ToggleGroup searchGroup;
    public Label searchLabel;
    @FXML
    private TextField searchTextField;
    @FXML
    private JFXButton searchButton;
    @FXML
    private JFXButton backButton;
    @FXML
    private JFXListView<Object> searchResultListView;
    @FXML
    private JFXButton creditButton;
    @FXML
    private JFXRadioButton personButton;
    @FXML
    private JFXRadioButton programButton;
    @FXML
    private JFXRadioButton producentButton;

    private int searchTypeId;

    public static PresentationSingleton fulcrum;


    @Override
    public void onLaunch(Object o) {
        fulcrum = PresentationSingleton.getInstance();
        fulcrum.setTitle("Søgeresultat");
        //searchTextField.setText(fulcrum.getSearchText()); //sets the TextField to be what was searched for, unnecessary?
        searchTypeId = fulcrum.getSearchType();
        switch (searchTypeId){
            case 0:
                personButton.setSelected(true);
                break;
            case 1:
                producentButton.setSelected(true);
                break;
            case 2:
                programButton.setSelected(true);
                break;
            default:
                System.out.println("Something probably went wrong");
                programButton.setSelected(true);
        }
        List<Object> objects = (List<Object>) o;
        ObservableList<Object> thing = FXCollections.observableArrayList(objects);
        searchResultListView.setItems(thing);

        searchResultListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
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
        Object chosen =  searchResultListView.getSelectionModel().getSelectedItem();
        if(chosen == null){
            return;
        }
        System.out.println("Du har valgt følgende element: " + chosen);
        fulcrum.changeView("creditpage", chosen);
    }

    public void searchRadioHandler() {
        if(personButton.isSelected()){
            searchTextField.setPromptText("Indtast navn på en person");
            searchTypeId = 0;
        }
        if(producentButton.isSelected()){
            searchTextField.setPromptText("Indtast navnet på producenten");
            searchTypeId = 1;
        }
        if(programButton.isSelected()){
            searchTextField.setPromptText("Indtast navn på programmet");
            searchTypeId = 2;
        }
    }

    public void searchHandler() {
        String searchText = searchTextField.getText();
        if(searchText == null || searchText.trim().isEmpty()){
            return;
        }
        System.out.println("Du søgte efter: " + searchText + "!");
        fulcrum.setSearchText(searchText);
        fulcrum.setSearchType(searchTypeId);
        List results = fulcrum.getDomainLayer().search(searchTypeId, searchText);

        fulcrum.changeView("searchpage", results);
    }

    public void returnHandler() throws IOException {
        fulcrum.goToFrontPage();
    }

}
