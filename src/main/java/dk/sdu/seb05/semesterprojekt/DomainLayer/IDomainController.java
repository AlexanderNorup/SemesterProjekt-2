package dk.sdu.seb05.semesterprojekt.DomainLayer;

import dk.sdu.seb05.semesterprojekt.PersistenceLayer.*;
import java.util.Date;
import java.util.List;

public interface IDomainController {
    List<IProgramme> getProgrammes(int producerID);

    boolean createProducer(String company, List <IProgramme> programmes);
    IProgramme createProgramme(String name, Category category, String channel, Date airedDate);
    boolean createPerson(String name, Date birthdate, String description);
    ICredit createCredit(IPerson person, FunctionType functionType);
    ICredit createCredit(String name, Date birthDate, String description, FunctionType functionType);

    Category[] getCategories();
    List<FunctionType> getFunctionTypes();

    boolean updateProducer(IProducer iProducer);
    boolean updateProgramme(IProgramme iProgramme);
    boolean updatePerson(IPerson iPerson);

    boolean deleteProducer(int producerID);
    boolean deleteProgramme(int programmeID);
    boolean deletePerson(int personID);

    List<IProgramme> getLatestProgrammes();
    List<IProgramme> getProgrammes();
    List<IProducer> getProducers();
    List<IPerson> getPersons();

    List<String> getNotifications();

    void setSession(int auth, int id); // 0 = "User", 1 = "Producer", 2 = "Admin"
    Session getSession(); //ProducerID, erAdmin

    IPerson choosePerson(int personID);
    List<ICredit> getCreditsForPerson(int personID);
    List<IProgramme> getProgrammesForPerson(int personID);
    IProgramme chooseProgramme(int programmeID);
    IProducer chooseProducer(int producerID);
    List search(int chosen, String query); // Person (), Producer(*), Film()

    boolean addCredit(int programmeID, IPerson person, FunctionType functionType);
    boolean removeCredit(int programmeID, ICredit credit);
    void addProducer(int programmeID, IProducer producer);
    void removeProducer(int programmeID, IProducer producer);

    boolean commit();
}
