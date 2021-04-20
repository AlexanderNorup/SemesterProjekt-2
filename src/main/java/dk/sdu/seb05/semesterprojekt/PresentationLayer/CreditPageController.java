package dk.sdu.seb05.semesterprojekt.PresentationLayer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.IOException;

public class CreditPageController {
    @FXML
    private Button backButton;
    @FXML
    private Label contentLabel;
    @FXML
    private TextArea creditTextArea;

    public static PresentationSingleton fulcrum;

    public void initialize(){
        fulcrum = PresentationSingleton.getInstance();
        contentLabel.setText(fulcrum.getName());
        System.out.println(fulcrum.getName());
        creditTextArea.appendText("Mikael Nielsen - 17 år - Skuespiller\n");
        creditTextArea.appendText("Annelise Jensen - 34 år - Lydmand\n");
        creditTextArea.appendText("Alexander Nørup - 20 år - Vandmand");
    }

    public void returnHandler() throws IOException {
        Parent searchPage = FXMLLoader.load(getClass().getResource("/fxml/frontpage.fxml"));
        fulcrum.getPrimaryStage().setScene(new Scene(searchPage));
        fulcrum.getPrimaryStage().setTitle("Forside");

    }
}
