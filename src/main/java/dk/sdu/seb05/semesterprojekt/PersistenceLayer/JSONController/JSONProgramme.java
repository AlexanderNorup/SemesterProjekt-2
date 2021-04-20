package dk.sdu.seb05.semesterprojekt.PersistenceLayer.JSONController;

import dk.sdu.seb05.semesterprojekt.PersistenceLayer.*;

import java.util.Date;
import java.util.List;

public class JSONProgramme implements IProgramme {
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
    public void addCredit(IPerson person, FunctionType role) {

    }

    @Override
    public void removeCredit(IPerson person) {

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
