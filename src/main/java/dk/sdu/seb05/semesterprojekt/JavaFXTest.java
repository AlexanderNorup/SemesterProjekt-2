package dk.sdu.seb05.semesterprojekt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavaFXTest extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("JavaFXTest");
        Parent fxmlTest = FXMLLoader.load(getClass().getResource("/fxml/test.fxml"));
        Scene s = new Scene(fxmlTest);
        primaryStage.setScene(s);
        primaryStage.show();

    }
}
