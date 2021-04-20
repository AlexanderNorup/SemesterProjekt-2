package dk.sdu.seb05.semesterprojekt.PersistenceLayer.JSONController;

import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IPerson;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IDataLayer;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class JSONPerson implements IPerson {
    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getName() {

        return null;
    }

    @Override
    public void setName(String newName) {

    }

    @Override
    public Date getBirthdate() {
        return null;
    }

    @Override
    public void setBirthDate(Date newBirthdate) {

    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(String newDescription) {

    }
}
