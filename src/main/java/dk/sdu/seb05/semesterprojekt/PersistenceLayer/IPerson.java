package dk.sdu.seb05.semesterprojekt.PersistenceLayer;

import java.util.Date;

public interface IPerson {
    int getId();

    String getName();
    void setName(String newName);

    Date getBirthdate();
    void setBirthDate(Date newBirthdate);

    String getDescription();
    void setDescription(String newDescription);
}
