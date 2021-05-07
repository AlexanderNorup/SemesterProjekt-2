package dk.sdu.seb05.semesterprojekt.PersistenceLayer;

import dk.sdu.seb05.semesterprojekt.PersistenceLayer.DatabaseController.DatabaseController;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.JSONController.JSONController;

public class PersistenceFactory {

    public static IDataLayer getDataLayer(int type){
        return switch (type) {
            case 0 -> new JSONController();
            case 1 -> new DatabaseController();
            default -> throw new IllegalArgumentException("Persistence " + type + " is not supported.");
        };
    }

}
