package dk.sdu.seb05.semesterprojekt.PresentationLayer.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXRadioButton;
import dk.sdu.seb05.semesterprojekt.PresentationLayer.PresentationSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.util.List;

public class SearchPageController implements ViewArgumentAdapter {

    PresentationSingleton presentationSingleton;

    @FXML
    private ToggleGroup searchGroup;
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

    @Override
    public void onLaunch(Object o) {
        presentationSingleton = PresentationSingleton.getInstance();
        presentationSingleton.setTitle("Søgeresultat");
        searchTypeId = presentationSingleton.getSearchType();
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
        searchResultListView.getStyleClass().add("mylistview");
        searchResultListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                chooseHandler();
            }
        });
        searchTextField.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                searchHandler();
            }
        });
        searchTextField.setText(presentationSingleton.getSearchText());
    }

    public void chooseHandler() {
        Object chosen =  searchResultListView.getSelectionModel().getSelectedItem();
        if(chosen == null){
            return;
        }
        System.out.println("Du har valgt følgende element: " + chosen);
        presentationSingleton.changeView("creditpage", chosen);
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
        presentationSingleton.setSearchText(searchText);
        presentationSingleton.setSearchType(searchTypeId);
        List results = presentationSingleton.getDomainLayer().search(searchTypeId, searchText);

        presentationSingleton.changeView("searchpage", results);
    }

    public void returnHandler() throws IOException {
        presentationSingleton.goToFrontPage();
    }

}
