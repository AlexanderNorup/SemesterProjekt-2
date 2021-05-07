package dk.sdu.seb05.semesterprojekt.PresentationLayer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.ICredit;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IPerson;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProducer;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProgramme;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.IOException;

public class CreditPageController implements ViewArgumentAdapter{

    @FXML
    private TextArea descriptionTextArea;
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
            setLayoutElse();
        }else if(o instanceof ICredit){
            IPerson person = ( (ICredit) o ).getPerson();
            contentLabel.setText(person.getName());
            listDescriptionLabel.setText("Medvirkende i: ");
            fulcrum.setTitle("Credits for: " + person.getName());
            ObservableList<Object> programmes = FXCollections.observableArrayList( fulcrum.getDomainLayer().getProgrammesForPerson(person.getId()) );
            creditListView.setItems(programmes);
            descriptionTextArea.setText(person.getDescription());
            setLayoutPerson();
        }else if(o instanceof IPerson){
            IPerson person = (IPerson) o;
            contentLabel.setText(person.getName());
            listDescriptionLabel.setText("Medvirkende i: ");
            fulcrum.setTitle("Credits for: " + person.getName());
            ObservableList<Object> programmes = FXCollections.observableArrayList( fulcrum.getDomainLayer().getProgrammesForPerson(person.getId()) );
            creditListView.setItems(programmes);
            descriptionTextArea.setText(person.getDescription());
            setLayoutPerson();
        } else if(o instanceof IProducer){
            IProducer producer = (IProducer) o;
            contentLabel.setText(producer.getCompany());
            listDescriptionLabel.setText("Producerede programmer: ");
            fulcrum.setTitle("Credits for: " + producer.getCompany());
            ObservableList<Object> programmes = FXCollections.observableArrayList(fulcrum.getDomainLayer().getProgrammes(producer.getId()));
            creditListView.setItems(programmes);
            setLayoutElse();
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

    public void setLayoutPerson(){
        creditListView.setPrefHeight(245);
        creditListView.setPrefWidth(433);
        creditListView.setLayoutX(84);
        creditListView.setLayoutY(141);
        listDescriptionLabel.setLayoutX(84);
        listDescriptionLabel.setLayoutY(119);
        creditButton.setLayoutX(520);
        creditButton.setLayoutY(141);
        descriptionTextArea.setDisable(false);
        descriptionTextArea.setVisible(true);
        descriptionTextArea.setWrapText(true);
        descriptionTextArea.setEditable(false);
    }

    public void setLayoutElse(){
        creditListView.setPrefHeight(320);
        creditListView.setPrefWidth(433);
        creditListView.setLayoutX(84);
        creditListView.setLayoutY(66);
        listDescriptionLabel.setLayoutX(84);
        listDescriptionLabel.setLayoutY(44);
        creditButton.setLayoutX(520);
        creditButton.setLayoutY(66);
        descriptionTextArea.setDisable(true);
        descriptionTextArea.setVisible(false);
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
