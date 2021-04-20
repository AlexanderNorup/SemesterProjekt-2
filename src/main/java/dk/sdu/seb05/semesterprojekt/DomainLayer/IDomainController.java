package dk.sdu.seb05.semesterprojekt.DomainLayer;

import dk.sdu.seb05.semesterprojekt.PersistenceLayer.Category;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProducer;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProgramme;

import java.util.Date;
import java.util.List;

public interface IDomainController {

    boolean createProducer(String companyName);
    boolean createProgramme(String programName, Category category, String channel, Date airedDate, IProducer producerID);
    boolean createPerson(String name, Date birthdate, String description);

    boolean updateProducer(int producerID);
    boolean updateProgramme(int programmeID);
    boolean updatePerson(int personID);

    boolean deleteProducer(int producerID);
    boolean deleteProgramme(int programmeID);
    boolean deletePerson(int personID);

    List<IProgramme> getLatestProgrammes();

    List<String> getNotifications();

    void setSession(int auth); // 0 = "User", 1 = "Producer", 2 = "Admin"
    Session getSession(); //ProducerID, erAdmin
}
