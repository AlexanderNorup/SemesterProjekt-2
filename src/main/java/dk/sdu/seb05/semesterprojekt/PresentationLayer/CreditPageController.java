package dk.sdu.seb05.semesterprojekt.PresentationLayer;

import dk.sdu.seb05.semesterprojekt.DomainLayer.DomainController;
import dk.sdu.seb05.semesterprojekt.DomainLayer.IDomainController;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.ICredit;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IPerson;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProducer;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProgramme;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class CreditPageController implements ViewArgumentAdapter{
    public Button creditButton;
    @FXML
    private Button backButton;
    @FXML
    private Label contentLabel;
    @FXML
    private TextArea creditTextArea;
    @FXML
    private ListView<Object> creditListView;

    public static PresentationSingleton fulcrum;

    private IProgramme programme;

    @Override
    public void onLaunch(Object o) {
        fulcrum = PresentationSingleton.getInstance();
        //Gets called when view is changed to.
        if(o instanceof IProgramme){
            programme = (IProgramme) o;
            fulcrum.setTitle("Credits for: " + programme.getName());
            contentLabel.setText(programme.getName());
            ObservableList<Object> persons = FXCollections.observableArrayList((programme.getCredits()));
            creditListView.setItems(persons);
            creditListView.getSelectionModel().selectFirst();
        }else if(o instanceof ICredit){
            IPerson person = ( (ICredit) o ).getPerson();
            contentLabel.setText(person.getName());
            fulcrum.setTitle("Credits for: " + person.getName());
            ObservableList<Object> programmes = FXCollections.observableArrayList( fulcrum.getDomainLayer().getProgrammesForPerson(person.getId()) );
            creditListView.setItems(programmes);
        }else if(o instanceof IPerson){
            IPerson person = (IPerson) o;
            contentLabel.setText(person.getName());
            fulcrum.setTitle("Credits for: " + person.getName());
            ObservableList<Object> programmes = FXCollections.observableArrayList( fulcrum.getDomainLayer().getProgrammesForPerson(person.getId()) );
            creditListView.setItems(programmes);
        } else if(o instanceof IProducer){
            IProducer producer = (IProducer) o;
            contentLabel.setText(producer.getCompany());
            fulcrum.setTitle("Credits for: " + producer.getCompany());
            ObservableList<Object> programmes = FXCollections.observableArrayList(fulcrum.getDomainLayer().getProgrammes(producer.getId()));
            creditListView.setItems(programmes);
        }


        creditListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    try {
                        seeCreditsHandler();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public void returnHandler() throws IOException {
        fulcrum.goToFrontPage();
    }

    public void seeCreditsHandler() throws IOException {
        if(creditListView.getSelectionModel().getSelectedItem() == null){
            return;
        }
        System.out.println("Du har valgt: " + creditListView.getSelectionModel().getSelectedItem());
        //fulcrum.setName(creditListView.getSelectionModel().getSelectedItem());
        fulcrum.changeView("creditpage", creditListView.getSelectionModel().getSelectedItem());
    }


}
