package dk.sdu.seb05.semesterprojekt.PresentationLayer;

import dk.sdu.seb05.semesterprojekt.DomainLayer.DomainController;
import dk.sdu.seb05.semesterprojekt.DomainLayer.IDomainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PresentationSingleton {

    private Stage primaryStage;
    private int searchTypeId;
    private String name;
    private String searchText;
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

    public int getSearchType(){
        return searchTypeId;
    }
    public void setSearchType(int searchTypeId){
        this.searchTypeId = searchTypeId;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public String getSearchText(){
        return searchText;
    }

    public void setSearchText(String searchText){
        this.searchText = searchText;
    }

    public void goToFrontPage() throws IOException {
        Parent searchPage = FXMLLoader.load(getClass().getResource("/fxml/frontpage.fxml"));
        instance.getPrimaryStage().setScene(new Scene(searchPage));
        instance.getPrimaryStage().setTitle("Forside");
        instance.getPrimaryStage().getScene().getStylesheets().setAll(String.valueOf(getClass().getResource("/css/style.css"))); //midlertidig fix
    }

    public void changeView(String view){
        this.changeView(view, new Object());
    }

    /**
     * Method to change between scenes. Two variations, one where an object is parsed and one without.
     * @param view is the String name of the FXML document for the next scene. It gets parsed through an FXMLLoader.
     * @param additionalData is an optional parameter, which enables the possibility of sending objects between
     *                       scenes, such as IProgrammes or ICredits.
     */
    public void changeView(String view, Object additionalData){
        Parent targetPage = null;
        try {
            FXMLLoader loader = new FXMLLoader(PresentationSingleton.class.getResource("/fxml/" + view + ".fxml"));
            targetPage = loader.load();
            getPrimaryStage().setScene(new Scene(targetPage));
            instance.getPrimaryStage().getScene().getStylesheets().setAll(String.valueOf(getClass().getResource("/css/style.css"))); //midlertidig fix
            if(loader.getController() instanceof ViewArgumentAdapter){
                ViewArgumentAdapter adapter = loader.getController();
                adapter.onLaunch(additionalData);
            }
        } catch (IOException e) {
            System.out.println("Der skete en fejl med at indlæse view: " + view);
            e.printStackTrace();
        }
    }

}
