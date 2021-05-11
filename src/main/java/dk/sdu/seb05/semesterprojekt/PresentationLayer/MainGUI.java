package dk.sdu.seb05.semesterprojekt.PresentationLayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class MainGUI extends Application {

    PresentationSingleton presentationSingleton;

    @Override
    public void start(Stage primaryStage) throws Exception {

        if(!checkDatabaseAuth()){
            askForAuth();
        }

        primaryStage.setTitle("Forside");
        Parent frontpage = FXMLLoader.load(getClass().getResource("/fxml/frontpage.fxml"));
        Scene s = new Scene(frontpage);
        primaryStage.setResizable(false);
        primaryStage.setScene(s);
        primaryStage.getIcons().add(new Image(String.valueOf(getClass().getResource("/pictures/TV_2_RGB.png"))));
        presentationSingleton = PresentationSingleton.getInstance();
        s.getStylesheets().setAll(String.valueOf(getClass().getResource("/css/style.css")));
        presentationSingleton.setPrimaryStage(primaryStage);
        primaryStage.show();
    }



    private boolean checkDatabaseAuth(){
        File auth = new File("auth.txt");
        if(auth.exists()){
            try {
                String password = Files.readString(Path.of(auth.toURI()), StandardCharsets.UTF_8);
                return !password.isEmpty();
            } catch (IOException e) {
                System.out.println("Failed to load file or JSON object: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    private void askForAuth(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("PostgresSQL Database Kode");
        dialog.setHeaderText("PostgresSQL Database Kode");
        dialog.setContentText("Indtast venligst adgangskoden for at tilgå databasen:");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            try{
                String auth = result.get();
                File authFile = new File("auth.txt");
                Files.writeString(Path.of(authFile.toURI()), auth, StandardCharsets.UTF_8);
            }catch(IOException e){
                System.out.println("Could not save file: " + e.getMessage());
                e.printStackTrace();
            }
        }

    }


}
