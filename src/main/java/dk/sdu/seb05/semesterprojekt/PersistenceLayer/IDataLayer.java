package dk.sdu.seb05.semesterprojekt.PersistenceLayer;

import java.util.List;

public interface IDataLayer {
    List<IPerson> getPersons();
    List<IProgramme> getProgrammes();
    List<IProducer> getProducers();

    IProgramme getProgram(int programId);
    IProducer getProducer(int programId);
    IPerson getPerson(int programId);

    boolean updateProgramme(IProgramme iProgramme);
    boolean updatePerson(IPerson iPerson);
    boolean updateProducer(IProducer iProducer);

    boolean createProgramme(IProgramme iProgramme);
    boolean createPerson(IPerson iPerson);
    boolean createProducer(IProducer iProducer);

    boolean deleteProgramme(IProgramme iProgramme);
    boolean deletePerson(IPerson iPerson);
    boolean deleteProducer(IProducer iProducer);

    String exportData();
    String exportData(int producerId);

    String getStatistics();
    String getStatistics(int producerId);

    List<IProgramme> searchForProgramme(String query);
    List<IPerson> searchForPerson(String query);
    List<IProducer> searchForProducer(String query);

    List<IProgramme> getLatestProgrammes();

    List<String> getNotifications(int producerId);

    void logMessage(String message);
}
