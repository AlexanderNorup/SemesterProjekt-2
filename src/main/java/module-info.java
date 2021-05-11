module semesterprojekt.main {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires javafx.media;
    requires java.sql;
    requires postgresql;
    requires json;

    opens dk.sdu.seb05.semesterprojekt.PresentationLayer;
    opens dk.sdu.seb05.semesterprojekt.DomainLayer; //Probably not needed
    opens dk.sdu.seb05.semesterprojekt.PersistenceLayer;
    opens dk.sdu.seb05.semesterprojekt.PersistenceLayer.JSONController;
    opens dk.sdu.seb05.semesterprojekt.PresentationLayer.Controllers;
}