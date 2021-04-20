package dk.sdu.seb05.semesterprojekt.PresentationLayer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.TextFieldListCell;

import java.io.IOException;

public class CreditPageController {
    public Button creditButton;
    @FXML
    private Button backButton;
    @FXML
    private Label contentLabel;
    @FXML
    private TextArea creditTextArea;
    @FXML
    private ListView<String> creditListView;

    public static PresentationSingleton fulcrum;

    public void initialize(){
        fulcrum = PresentationSingleton.getInstance();
        contentLabel.setText(fulcrum.getName());
        System.out.println(fulcrum.getName());
        ObservableList<String> persons = FXCollections.observableArrayList(
                "Mikael Nielsen - 17 år - Skuespiller ",
                "Annelise Jensen - 34 år - Lydmand",
                "Alexander Nørup - 20 år - Vandmand");
        creditListView.setItems(persons);
        creditListView.getSelectionModel().selectFirst();
    }

    public void returnHandler() throws IOException {
        fulcrum.goToFrontPage();
    }

    public void seeCreditsHandler() throws IOException {
        System.out.println("Du har valgt: " + creditListView.getSelectionModel().getSelectedItem());
        fulcrum.setName(creditListView.getSelectionModel().getSelectedItem());
        Parent creditPage = FXMLLoader.load(JavaFXTest.class.getResource("/fxml/creditpage.fxml"));
        fulcrum.getPrimaryStage().setScene(new Scene(creditPage));
        fulcrum.getPrimaryStage().setTitle("Credits til " + creditListView.getSelectionModel().getSelectedItem());
    }
}
