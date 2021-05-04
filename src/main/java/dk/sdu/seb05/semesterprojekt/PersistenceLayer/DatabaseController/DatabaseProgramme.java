package dk.sdu.seb05.semesterprojekt.PersistenceLayer.DatabaseController;

import dk.sdu.seb05.semesterprojekt.PersistenceLayer.Category;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.ICredit;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProducer;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProgramme;

import java.util.Date;
import java.util.List;

public class DatabaseProgramme implements IProgramme {
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
    public Category getCategory() {
        return null;
    }

    @Override
    public void setCategory(Category newCategory) {

    }

    @Override
    public String getChannel() {
        return null;
    }

    @Override
    public void setChannel(String newChannel) {

    }

    @Override
    public Date getAiredDate() {
        return null;
    }

    @Override
    public void setAiredDate(Date newDate) {

    }

    @Override
    public List<ICredit> getCredits() {
        return null;
    }

    @Override
    public void addCredit(ICredit credit) {

    }

    @Override
    public void removeCredit(ICredit credit) {

    }

    @Override
    public List<IProducer> getProducers() {
        return null;
    }

    @Override
    public void addProducer(IProducer producer) {

    }

    @Override
    public void removeProducer(IProducer producer) {

    }
}
