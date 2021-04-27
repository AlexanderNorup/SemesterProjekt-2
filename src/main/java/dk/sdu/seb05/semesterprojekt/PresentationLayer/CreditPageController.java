package dk.sdu.seb05.semesterprojekt.PresentationLayer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.ICredit;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IPerson;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProducer;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProgramme;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class CreditPageController implements ViewArgumentAdapter{

    @FXML
    private Label listDescriptionLabel;
    @FXML
    private JFXButton creditButton;
    @FXML
    private JFXButton backButton;
    @FXML
    private Label contentLabel;
    @FXML
    private JFXListView<Object> creditListView;

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
            listDescriptionLabel.setText("Rolleliste: ");
            ObservableList<Object> persons = FXCollections.observableArrayList((programme.getCredits()));
            creditListView.setItems(persons);
            creditListView.getSelectionModel().selectFirst();
        }else if(o instanceof ICredit){
            IPerson person = ( (ICredit) o ).getPerson();
            contentLabel.setText(person.getName());
            listDescriptionLabel.setText("Medvirkende i: ");
            fulcrum.setTitle("Credits for: " + person.getName());
            ObservableList<Object> programmes = FXCollections.observableArrayList( fulcrum.getDomainLayer().getProgrammesForPerson(person.getId()) );
            creditListView.setItems(programmes);
        }else if(o instanceof IPerson){
            IPerson person = (IPerson) o;
            contentLabel.setText(person.getName());
            listDescriptionLabel.setText("Medvirkende i: ");
            fulcrum.setTitle("Credits for: " + person.getName());
            ObservableList<Object> programmes = FXCollections.observableArrayList( fulcrum.getDomainLayer().getProgrammesForPerson(person.getId()) );
            creditListView.setItems(programmes);
        } else if(o instanceof IProducer){
            IProducer producer = (IProducer) o;
            contentLabel.setText(producer.getCompany());
            listDescriptionLabel.setText("Producerede programmer: ");
            fulcrum.setTitle("Credits for: " + producer.getCompany());
            ObservableList<Object> programmes = FXCollections.observableArrayList(fulcrum.getDomainLayer().getProgrammes(producer.getId()));
            creditListView.setItems(programmes);
        }
        creditListView.getStyleClass().add("mylistview");
        //Opsætter dobbeltklik på
        creditListView.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2){
                try {
                    seeCreditsHandler();
                } catch (IOException e) {
                    e.printStackTrace();
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
        fulcrum.changeView("creditpage", creditListView.getSelectionModel().getSelectedItem());
    }


}
