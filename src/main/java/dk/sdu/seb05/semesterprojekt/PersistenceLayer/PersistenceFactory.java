package dk.sdu.seb05.semesterprojekt.PersistenceLayer;

import dk.sdu.seb05.semesterprojekt.PersistenceLayer.DatabaseController.DatabaseController;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.JSONController.JSONController;
import javafx.scene.control.TextInputDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class PersistenceFactory {

    private static Settings settings;

    public static IDataLayer getDataLayer(){
        File settingsFile = new File("auth.json");
        if(settingsFile.exists()) {
            settings = Settings.loadSettings(settingsFile);
        }else{
            settings = Settings.getDefaultSettings(settingsFile);
        }

        while(!checkDatabaseAuth()){
            askForAuth();
        }

        return switch (getDataLayerType()) {
            case 0 -> new JSONController();
            case 1 -> new DatabaseController();
            default -> throw new IllegalStateException("Unexpected value: " + getDataLayerType());
        };
    }


    private static int getDataLayerType(){
        return settings.useJSON() ? 0 : 1;
    }

    private static boolean checkDatabaseAuth(){

        if(settings.useJSON()){
            return true; //Accept the auth, since we use JSON.
        }

        if(!settings.hasPassword()) {
            return false;
        }

        return new DatabaseController().checkConnection();
    }

    private static void askForAuth(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("PostgresSQL Database Kode");
        dialog.setHeaderText("PostgresSQL Database Kode");
        dialog.setContentText("Indtast venligst adgangskoden for at tilgå databasen:");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            String auth = result.get();
            settings.setPassword(auth);
            settings.saveSettings();
        }else{
            System.exit(1);
        }
    }
}
