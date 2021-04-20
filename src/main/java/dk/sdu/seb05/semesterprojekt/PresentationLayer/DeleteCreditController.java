package dk.sdu.seb05.semesterprojekt.PresentationLayer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;

public class DeleteCreditController {

    PresentationSingleton fulcrum;

    public ListView<String> creditListView;
    public Button backButton;
    public Button deleteButton;

    public void initialize(){
        fulcrum = PresentationSingleton.getInstance();
        ObservableList<String> persons = FXCollections.observableArrayList(
                "Mikael Nielsen - 17 år - Skuespiller ",
                "Lars Andersson - 26 år - Kattemand",
                "Alexander Matzen - ? år - Professional database manager",
                "Alexander Nørup - 20 år - Vandmand");
        creditListView.setItems(persons);
    }

    public void returnHandler() throws IOException {
        fulcrum.goToFrontPage();
    }

    public void deleteHandler() {
        creditListView.getItems().remove(creditListView.getSelectionModel().getSelectedItem());
    }
}
