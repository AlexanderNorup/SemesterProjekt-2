package dk.sdu.seb05.semesterprojekt.PersistenceLayer;

import java.util.List;

public interface IProducer {
    int getId();

    String getCompany();
    void setCompany(String newCompany);

    List<IProgramme> getProgrammes();
    void addProgramme(IProgramme programme);
    void removeProgramme(IProgramme programme);
}
