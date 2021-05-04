package dk.sdu.seb05.semesterprojekt.PersistenceLayer.DatabaseController;

import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProducer;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProgramme;

import java.util.List;

public class DatabaseProducer implements IProducer {
    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getCompany() {
        return null;
    }

    @Override
    public void setCompany(String newCompany) {

    }

    @Override
    public List<IProgramme> getProgrammes() {
        return null;
    }

    @Override
    public void addProgramme(IProgramme programme) {

    }

    @Override
    public void removeProgramme(IProgramme programme) {

    }
}
