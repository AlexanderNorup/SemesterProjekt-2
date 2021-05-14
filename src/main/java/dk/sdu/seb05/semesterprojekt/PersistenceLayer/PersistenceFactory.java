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

    public static IDataLayer getDataLayer(){

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
        try {
            File passwordFile = new File("auth.json");
            String json = Files.readString(Path.of(passwordFile.toURI()), StandardCharsets.UTF_8).trim();
            JSONObject jsonObject = new JSONObject(json);
            if(jsonObject.has("useJSONDataLayer") && !jsonObject.getBoolean("useJSONDataLayer")){
                return 1; //Database
            }
        } catch (IOException | JSONException e) {
            System.out.println("Could not read auth.json to determine persistence-layer type. Defaults to JSON. ");
            e.printStackTrace();
        }
        return 0; //JSON
    }

    private static boolean checkDatabaseAuth(){
        File auth = new File("auth.json");
        if(auth.exists()){
            try {
                String jsonFile = Files.readString(Path.of(auth.toURI()), StandardCharsets.UTF_8);
                JSONObject object = new JSONObject(jsonFile);
                if(object.has("useJSONDataLayer") && object.getBoolean("useJSONDataLayer")){
                    return true; //Accept the auth, since we use JSON.
                }

                if(!object.has("password")) {
                    return false;
                }
                return new DatabaseController().checkConnection();
            } catch (IOException | JSONException e) {
                System.out.println("Failed to load file or JSON object: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    private static void askForAuth(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("PostgresSQL Database Kode");
        dialog.setHeaderText("PostgresSQL Database Kode");
        dialog.setContentText("Indtast venligst adgangskoden for at tilgå databasen:");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            try{
                String auth = result.get();
                File authFile = new File("auth.json");
                JSONObject jsonObject = new JSONObject();
                if (!jsonObject.has("connectionString")) jsonObject.put("connectionString", "jdbc:postgresql://hosting.alexandernorup.com:5432/tv2");
                if (!jsonObject.has("username")) jsonObject.put("username", "java");
                if (!jsonObject.has("useJSONDataLayer")) jsonObject.put("useJSONDataLayer", false);
                jsonObject.put("password", auth);
                Files.writeString(Path.of(authFile.toURI()), jsonObject.toString(2), StandardCharsets.UTF_8);
            }catch(IOException | JSONException e){
                System.out.println("Could not save file: " + e.getMessage());
                e.printStackTrace();
            }
        }else{
            System.exit(1);
        }
    }
}
