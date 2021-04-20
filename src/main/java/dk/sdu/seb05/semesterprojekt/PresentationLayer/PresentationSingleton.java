package dk.sdu.seb05.semesterprojekt.PresentationLayer;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.stage.Stage;

import java.io.IOException;

public class PresentationSingleton {

    private Stage primaryStage;
    private String name;
    private String search;
    private static PresentationSingleton instance;

    private PresentationSingleton(){
    }

    public static PresentationSingleton getInstance(){
        if(instance == null){
            instance = new PresentationSingleton();
        }
        return instance;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getSearch(){
        return search;
    }

    public void setSearch(String search){
        this.search = search;
    }

    public void goToFrontPage() throws IOException {
        Parent searchPage = FXMLLoader.load(getClass().getResource("/fxml/frontpage.fxml"));
        instance.getPrimaryStage().setScene(new Scene(searchPage));
        instance.getPrimaryStage().setTitle("Forside");
    }

}
