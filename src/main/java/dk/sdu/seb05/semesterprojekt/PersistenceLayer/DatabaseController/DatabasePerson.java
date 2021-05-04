package dk.sdu.seb05.semesterprojekt.PersistenceLayer.DatabaseController;

import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IPerson;

import java.util.Date;

public class DatabasePerson implements IPerson {
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
        return null;
    }

    @Override
    public void setDescription(String newDescription) {

    }
}
