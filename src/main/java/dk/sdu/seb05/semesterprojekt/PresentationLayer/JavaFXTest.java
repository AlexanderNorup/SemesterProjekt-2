package dk.sdu.seb05.semesterprojekt.PresentationLayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class JavaFXTest extends Application {

    public static PresentationSingleton instance;

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Forside");
        Parent fxmlTest = FXMLLoader.load(getClass().getResource("/fxml/frontpage.fxml"));
        Scene s = new Scene(fxmlTest);
        primaryStage.setScene(s);
        primaryStage.getIcons().add(new Image(String.valueOf(getClass().getResource("/pictures/TV_2_RGB.png"))));
        instance = PresentationSingleton.getInstance();
        instance.setPrimaryStage(primaryStage);
        primaryStage.show();
    }
}
