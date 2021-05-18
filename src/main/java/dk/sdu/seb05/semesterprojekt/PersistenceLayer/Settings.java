package dk.sdu.seb05.semesterprojekt.PersistenceLayer;

import dk.sdu.seb05.semesterprojekt.PersistenceLayer.DatabaseController.DatabaseController;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Settings {

    private File file;
    private String url;
    private String username;
    private String password;
    private boolean useJSON;

    private Settings(File file, String url, String username, String password, boolean useJSON) {
        this.file = file;
        this.url = url;
        this.username = username;
        this.password = password;
        this.useJSON = useJSON;
    }

    public static Settings getDefaultSettings(File f) {
        return new Settings(f, "jdbc:postgresql://hosting.alexandernorup.com:5432/tv2", "java", "", false);
    }

    public static Settings loadSettings(File f) {
        try {
            String jsonFile = Files.readString(Path.of(f.toURI()), StandardCharsets.UTF_8);
            JSONObject object = new JSONObject(jsonFile);
            boolean useJson = false;
            String url = "";
            String user = "";
            String password = "";
            if (object.has("useJSONDataLayer")) {
                useJson = object.getBoolean("useJSONDataLayer");
            }
            if (object.has("connectionString")) {
                url = object.getString("connectionString");
            }
            if (object.has("useJSONDataLayer")) {
                user = object.getString("username");
            }
            if (object.has("useJSONDataLayer")) {
                password = object.getString("password");
            }

            return new Settings(f, url, user, password, useJson);
        } catch (IOException | JSONException e) {
            System.out.println("Failed to load file or JSON object: " + e.getMessage());
            return getDefaultSettings(f);
        }
    }

    public boolean saveSettings() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("useJSONDataLayer", useJSON);
            jsonObject.put("connectionString", url);
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            Files.writeString(Path.of(file.toURI()), jsonObject.toString(2), StandardCharsets.UTF_8);
            return true;
        } catch (IOException | JSONException e) {
            System.out.println("Could not save JSON settings file.." + e.getMessage());
            System.exit(1);
        }
        return false;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean hasPassword(){
        return !password.trim().isEmpty();
    }

    public boolean useJSON() {
        return useJSON;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUseJSON(boolean useJSON) {
        this.useJSON = useJSON;
    }
}
