package dk.sdu.seb05.semesterprojekt.PersistenceLayer;

import java.util.List;
import java.util.Date;

public interface IDataLayer {
    List<IPerson> getPersons();
    List<IProgramme> getProgrammes();
    List<IProducer> getProducers();

    IProgramme getProgram(int programId);
    IProducer getProducer(int producerId);
    IPerson getPerson(int personId);
    ICredit getCredit(int creditId);

    boolean updateProgramme(IProgramme iProgramme);
    boolean updatePerson(IPerson iPerson);
    boolean updateProducer(IProducer iProducer);
    boolean updateCredit(ICredit iCredit);

    int createProgramme(String name, Category category, String channel, Date airedDate, List<ICredit> credits, List<IProducer> producers);
    int createPerson(String name, Date birthdate, String description);
    int createProducer(String company, List <IProgramme> programmes);
    int createCredit(IPerson person, FunctionType functionType);

    boolean deleteProgramme(IProgramme iProgramme);
    boolean deletePerson(IPerson iPerson);
    boolean deleteProducer(IProducer iProducer);
    boolean deleteCredit(ICredit iCredit);

    String exportData();
    String exportData(int producerId);

    String getStatistics();
    String getStatistics(int producerId);

    List<IProgramme> searchForProgramme(String query);
    List<IPerson> searchForPerson(String query);
    List<IProducer> searchForProducer(String query);

    List<IProgramme> getProgrammesForPerson(int personId);
    List<ICredit> getCreditsForPerson(int personId);

    List<IProgramme> getLatestProgrammes();

    List<String> getNotifications(int producerId);

    boolean commit();

    void logMessage(String message);
}
