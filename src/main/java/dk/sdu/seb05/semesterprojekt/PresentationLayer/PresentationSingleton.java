package dk.sdu.seb05.semesterprojekt.PresentationLayer;

import dk.sdu.seb05.semesterprojekt.DomainLayer.DomainController;
import dk.sdu.seb05.semesterprojekt.DomainLayer.IDomainController;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.stage.Stage;

import java.io.IOException;

public class PresentationSingleton {

    private Stage primaryStage;
    private int id;
    private String name;
    private String search;
    private IDomainController domainController;
    private static PresentationSingleton instance;

    private PresentationSingleton(){
        domainController = new DomainController();
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

    public void setTitle(String title){
        getPrimaryStage().setTitle(title);
    }

    public IDomainController getDomainLayer(){
        return domainController;
    }

    public int getID(){
        return id;
    }
    public void setID(int id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
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

    public void changeView(String view){
        this.changeView(view, new Object());
    }

    public void changeView(String view, Object addtionalData){
        Parent targetPage = null;
        try {
            FXMLLoader loader = new FXMLLoader(PresentationSingleton.class.getResource("/fxml/"+view+".fxml"));
            targetPage = loader.load();
            getPrimaryStage().setScene(new Scene(targetPage));
            if(loader.getController() instanceof ViewArgumentAdapter){
                ViewArgumentAdapter adapter = loader.getController();
                adapter.onLaunch(addtionalData);
            }
        } catch (IOException e) {
            System.out.println("Der skete en fejl med at indlæse view: " + view);
            e.printStackTrace();
        }
    }

}
