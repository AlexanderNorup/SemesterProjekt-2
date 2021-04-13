package dk.sdu.seb05.semesterprojekt.PresentationLayer;

import javafx.stage.Stage;

public class PresentationSingleton {

    private Stage primaryStage;
    private String name;
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

    public void setSearch(String name){

    }

}
