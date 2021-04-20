package dk.sdu.seb05.semesterprojekt.PersistenceLayer;

import java.util.Date;
import java.util.List;

public interface IProgramme {
    String getName();
    void setName(String newName);

    Category getCategory();
    void setCategory(Category newCategory);

    String getChannel();
    void setChannel(String newChannel);

    Date getAiredDate();
    void setAiredDate(Date newDate);

    List<ICredit> getCredits();
    void addCredit(IPerson person, FunctionType role);
    void removeCredit(IPerson person);

    List<IProducer> getProducers();
    void addProducer(IProducer producer);
    void removeProducer(IProducer producer);
}
