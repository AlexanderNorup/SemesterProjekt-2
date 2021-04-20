package dk.sdu.seb05.semesterprojekt.DomainLayer;

import dk.sdu.seb05.semesterprojekt.PersistenceLayer.*;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.JSONController.JSONController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DomainController implements IDomainController {
    IDataLayer dataLayer = new JSONController();
    Session session = new Session();
    Category[] categories = Category.values();
    List<FunctionType> functionTypes = FunctionType.getAllFunctionTypes();
    List<IProgramme> programmes = dataLayer.getProgrammes();
    List<IProducer> producers = dataLayer.getProducers();
    List<IPerson> person = dataLayer.getPersons();

    @Override
    public List<IProgramme> getProgrammes(int producerID) {
        return dataLayer.getProducer(producerID).getProgrammes();
    }

    @Override
    public boolean createProducer(String company, List <IProgramme> programmes) {
        String user = "Admin";
        dataLayer.createProducer(company, programmes);
        dataLayer.logMessage(user + " added producer " + company);
        return true;
    }

    //Get producerID from session
    @Override
    public boolean createProgramme(String name, Category category, String channel, Date airedDate) {
        String user = (session.isAdmin) ? "Admin" : "Producer";
        List<ICredit> credits = new ArrayList<>(0);
        List<IProducer> producers = new ArrayList<>(0);
        producers.add(dataLayer.getProducer(session.producerID));
        dataLayer.createProgramme(name, category, channel, airedDate, credits, producers);
        dataLayer.logMessage(user + " added programme: " + name);
        return true;
    }

    @Override
    public boolean createPerson(String name, Date birthdate, String description) {
        String user = (session.isAdmin) ? "Admin" : "Producer";
        dataLayer.createPerson(name, birthdate, description);
        dataLayer.logMessage(user + " added person: " + name);
        return true;
    }

    @Override
    public boolean createCredit(IPerson person, FunctionType functionType) {
        String user = (session.isAdmin) ? "Admin" : "Producer";
        dataLayer.createCredit(person, functionType);
        dataLayer.logMessage(user + " added credit for: " + person.getName() + " as " + functionType);
        return true;
    }

    @Override
    public boolean createCredit(String name, Date birthDate, String description, FunctionType functionType) {
        String user = (session.isAdmin) ? "Admin" : "Producer";
        int personID = dataLayer.createPerson(name, birthDate, description);
        //createCredit returns int, maybe return -1 if credit couldn't be created
        dataLayer.createCredit(dataLayer.getPerson(personID), functionType);
        dataLayer.logMessage(user + " added credit for: " + name + " as " + functionType);
        return true;
    }


    @Override
    public boolean updateProducer(int producerID, String newName) {
        IProducer producer = dataLayer.getProducer(producerID);
        producer.setCompany(newName);
        String user = "Admin";
        boolean result = dataLayer.updateProducer(producer);
        if(result){
            dataLayer.logMessage(user + " updated producer: " + newName);
        }
        return result;
    }

    @Override
    public boolean updateProgramme(int programmeID, Date newDate, Category newCategory, String newChannel, String newName ) {
        IProgramme programme = dataLayer.getProgram(programmeID);
        programme.setAiredDate(newDate);
        programme.setCategory(newCategory);
        programme.setChannel(newChannel);
        programme.setName(newName);
        String user = (session.isAdmin) ? "Admin" : "Producer";
        boolean result = dataLayer.updateProgramme(programme);
        if(result){
            dataLayer.logMessage(user + " updated programme: " + newName);
        }
        return result;
    }

    @Override
    public boolean updatePerson(int personID, Date birthDate, String description, String name) {
        IPerson person = dataLayer.getPerson(personID);
        person.setBirthDate(birthDate);
        person.setDescription(description);
        person.setName(name);
        String user = (session.isAdmin) ? "Admin" : "Producer";
        boolean result = dataLayer.updatePerson(person);
        if(result){
            dataLayer.logMessage(user + " updated person: " + name);
        }
        return result;
    }

    @Override
    public boolean deleteProducer(int producerID) {
        String user = (session.isAdmin) ? "Admin" : "Producer";
        boolean result = dataLayer.deleteProducer(dataLayer.getProducer(producerID));
        if(result){
            dataLayer.logMessage(user + " deleted producer with ID: " + producerID);
        }
        return result;
    }

    @Override
    public boolean deleteProgramme(int programmeID) {
        String user = (session.isAdmin) ? "Admin" : "Producer";
        boolean result = dataLayer.deleteProgramme(dataLayer.getProgram(programmeID));
        if(result){
            dataLayer.logMessage(user + " deleted programme with ID: " + programmeID);
        }
        return result;
    }

    @Override
    public boolean deletePerson(int personID) {
        String user = (session.isAdmin) ? "Admin" : "Producer";
        boolean result = dataLayer.deletePerson(dataLayer.getPerson(personID));
        if(result){
            dataLayer.logMessage(user + " deleted person with ID: " + personID);
        }
        return result;
    }

    @Override
    public List<IProgramme> getLatestProgrammes() {
        return dataLayer.getLatestProgrammes();
    }

    @Override
    public List<String> getNotifications() {
        return dataLayer.getNotifications(session.producerID);
    }

    @Override
    public void setSession(int auth) {
        session = null;
        switch (auth) {
            case 0:
                session = new Session();
                break;
            case 1:
                session = new Session(1);
                dataLayer.logMessage("Producer with id: " + 1 + " logged in");
                break;
            case 2:
                session = new Session(true);
                dataLayer.logMessage("Admin has logged in");
                break;
        }
    }

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public IPerson choosePerson(int personID) {
        return dataLayer.getPerson(personID);
    }

    @Override
    public List<ICredit> getCreditsForPerson(int personID) {
        return dataLayer.getCreditsForPerson(personID);
    }


    @Override
    public IProgramme chooseProgramme(int programmeID) {
        return dataLayer.getProgram(programmeID);
    }

    @Override
    public List search(int chosen, String query) {
        switch (chosen){
            case 0:
                return dataLayer.searchForPerson(query);
            case 1:
                return dataLayer.searchForProducer(query);
            case 2:
                return dataLayer.searchForProgramme(query);
            default: return new ArrayList<String>(1);
        }
    }

    @Override
    public boolean addCredit(int programmeID, IPerson person, FunctionType functionType) {
        int creditID = dataLayer.createCredit(person, functionType);
        IProgramme programme = dataLayer.getProgram(programmeID);
        //programme.addCredit returns void so we can't verify that credit has been added.
        programme.addCredit(dataLayer.getCredit(creditID));
        String user = (session.isAdmin) ? "Admin" : "Producer";
        dataLayer.logMessage(user + " added credit for "  + person.getName() + " as " + functionType + " in " + programme.getName());
        return true;
    }

    @Override
    public boolean removeCredit(int programmeID, ICredit credit) {
        dataLayer.getProgram(programmeID).removeCredit(credit);
        return true;
    }

    @Override
    public void addProducer(int programmeID, IProducer producer) {
        dataLayer.getProgram(programmeID).addProducer(producer);
        String user = (session.isAdmin) ? "Admin" : "Producer";
        dataLayer.logMessage(user + " added producer to " );
    }

    @Override
    public void removeProducer(int programmeID, IProducer producer) {

    }
}