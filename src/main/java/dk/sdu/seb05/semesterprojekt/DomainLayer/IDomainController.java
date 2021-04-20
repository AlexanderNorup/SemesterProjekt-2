package dk.sdu.seb05.semesterprojekt.DomainLayer;

import dk.sdu.seb05.semesterprojekt.PersistenceLayer.*;
import java.util.Date;
import java.util.List;

public interface IDomainController {
    List<IProgramme> getProgrammes(int producerID);

    boolean createProducer(String company, List <IProgramme> programmes);
    boolean createProgramme(String name, Category category, String channel, Date airedDate);
    boolean createPerson(String name, Date birthdate, String description);
    boolean createCredit(IPerson person, FunctionType functionType);
    boolean createCredit(String name, Date birthDate, String description, FunctionType functionType);

    boolean updateProducer(int producerID, String name);
    boolean updateProgramme(int programmeID, Date airedDate, Category category, String channel, String name);
    boolean updatePerson(int personID, Date birthdate, String description, String name);

    boolean deleteProducer(int producerID);
    boolean deleteProgramme(int programmeID);
    boolean deletePerson(int personID);

    List<IProgramme> getLatestProgrammes();

    List<String> getNotifications();

    void setSession(int auth); // 0 = "User", 1 = "Producer", 2 = "Admin"
    Session getSession(); //ProducerID, erAdmin

    IPerson choosePerson(int personID);
    List<ICredit> getCreditsForPerson(int personID);
    IProgramme chooseProgramme(int programmeID);
    List search(int chosen, String query); // Person (), Producer(*), Film()

    boolean addCredit(int programmeID, IPerson person, FunctionType functionType);
    boolean removeCredit(int programmeID, ICredit credit);
    void addProducer(int programmeID, IProducer producer);
    void removeProducer(int programmeID, IProducer producer);
}
