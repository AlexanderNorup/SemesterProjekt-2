package dk.sdu.seb05.semesterprojekt.PresentationLayer;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class CreditPageController {
    @FXML
    private Label contentLabel;
    @FXML
    private TextArea creditTextArea;

    public static PresentationSingleton fulcrum;

    public void initialize(){
        fulcrum = PresentationSingleton.getInstance();
        contentLabel.setText(fulcrum.getName());
        System.out.println(fulcrum.getName());
    }
}
