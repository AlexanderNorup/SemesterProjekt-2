package dk.sdu.seb05.semesterprojekt;

import dk.sdu.seb05.semesterprojekt.PresentationLayer.JavaFXTest;
import javafx.application.Application;
import org.json.JSONException;
import org.json.JSONObject;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World!!!");
        System.out.println("Hello, I am on a new branch!");

        //JSON Test
        JSONObject obj = new JSONObject();
        try {
            obj.put("name", "Test");
            obj.put("num", 10);
            obj.put("balance", 1850.18);
            obj.put("is_vip", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("JSON Output: " + obj);

        Application.launch(JavaFXTest.class, args);
    }

}
