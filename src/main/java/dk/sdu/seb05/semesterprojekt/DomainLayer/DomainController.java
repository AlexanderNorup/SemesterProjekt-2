package dk.sdu.seb05.semesterprojekt.DomainLayer;

import dk.sdu.seb05.semesterprojekt.PersistenceLayer.*;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.JSONController.JSONController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DomainController implements IDomainController {
    private final int PERSISTENCE_TYPE = 1; //0 = JSON, 1 = RDBMS
    IDataLayer dataLayer = PersistenceFactory.getDataLayer(PERSISTENCE_TYPE);
    Session session = new Session();
    Category[] categories = Category.values();
    List<FunctionType> functionTypes = FunctionType.getAllFunctionTypes();

    @Override
    public Category[] getCategories() {
        return categories;
    }

    @Override
    public List<FunctionType> getFunctionTypes() {
        return functionTypes;
    }


    @Override
    public List<IProgramme> getProgrammes(int producerID) {
        return dataLayer.getProgrammesForProducer(producerID);
    }

    @Override
    public List<IProgramme> getProgrammes(){
        return dataLayer.getProgrammes();
    }

    @Override
    public List<IProducer> getProducers(){
        return dataLayer.getProducers();
    }

    @Override
    public List<IPerson> getPersons(){
        return dataLayer.getPersons();
    }

    @Override
    public boolean createProducer(String company, List <IProgramme> programmes) {
        String user = "Admin";
        IProducer producer = dataLayer.createProducer(company);
        for(IProgramme programme : programmes){
            programme.addProducer(producer);
        }
        dataLayer.logMessage(user + " added producer " + company);
        return true;
    }

    //Get producerID from session
    @Override
    public IProgramme createProgramme(String name, Category category, String channel, Date airedDate) {
        String user = (session.isAdmin()) ? "Admin" : "Producer";
        List<ICredit> credits = new ArrayList<>(0);
        List<IProducer> producers = new ArrayList<>(0);
        producers.add(dataLayer.getProducer(session.getProducerID()));
        IProgramme programme = dataLayer.createProgramme(name, category, channel, airedDate, credits, producers);
        dataLayer.logMessage(user + " (id="+session.getProducerID()+") added programme: " + name);
        return programme;
    }

    @Override
    public boolean createPerson(String name, Date birthdate, String description) {
        String user = (session.isAdmin()) ? "Admin" : "Producer";
        dataLayer.createPerson(name, birthdate, description);
        dataLayer.logMessage(user + " (id="+session.getProducerID()+") added person: " + name);
        return true;
    }

    @Override
    public ICredit createCredit(IPerson person, FunctionType functionType) {
        String user = (session.isAdmin()) ? "Admin" : "Producer";
        ICredit credit = dataLayer.createCredit(person, functionType);
        dataLayer.logMessage(user + " (id="+session.getProducerID()+") added credit for: " + person.getName() + " as " + functionType);
        return credit;
    }

    @Override
    public ICredit createCredit(String name, Date birthDate, String description, FunctionType functionType) {
        String user = (session.isAdmin()) ? "Admin" : "Producer";
        IPerson person = dataLayer.createPerson(name, birthDate, description);
        //createCredit returns int, maybe return -1 if credit couldn't be created
        ICredit credit = dataLayer.createCredit(person, functionType);
        dataLayer.logMessage(user + " (id="+session.getProducerID()+") added credit for: " + name + " as " + functionType);
        return credit;
    }


    @Override
    public boolean updateProducer(IProducer producer) {
        String user = "Admin";
        boolean result = dataLayer.updateProducer(producer);
        if(result){
            dataLayer.logMessage(user + " (id="+session.getProducerID()+") updated producer: " + producer.getCompany());
        }
        return result;
    }

    @Override
    public boolean updateProgramme(IProgramme programme) {

        String user = (session.isAdmin()) ? "Admin" : "Producer";
        boolean result = dataLayer.updateProgramme(programme);
        if(result){
            dataLayer.logMessage(user + " (id="+session.getProducerID()+") updated programme: " + programme.toString());
        }
        return result;
    }


    @Override
    public boolean updatePerson(IPerson person) {
        String user = (session.isAdmin()) ? "Admin" : "Producer";
        boolean result = dataLayer.updatePerson(person);
        if(result){
            dataLayer.logMessage(user + " (id="+session.getProducerID()+") updated person: " + person.toString());
        }
        return result;
    }

    @Override
    public boolean deleteProducer(int producerID) {
        String user = (session.isAdmin()) ? "Admin" : "Producer";
        boolean result = dataLayer.deleteProducer(dataLayer.getProducer(producerID));
        if(result){
            dataLayer.logMessage(user + " (id="+session.getProducerID()+") deleted producer with ID: " + producerID);
        }
        return result;
    }

    @Override
    public boolean deleteProgramme(int programmeID) {
        String user = (session.isAdmin()) ? "Admin" : "Producer";
        boolean result = dataLayer.deleteProgramme(dataLayer.getProgram(programmeID));
        if(result){
            dataLayer.logMessage(user + " (id="+session.getProducerID()+") deleted programme with ID: " + programmeID);
        }
        return result;
    }

    @Override
    public boolean deletePerson(int personID) {
        String user = (session.isAdmin()) ? "Admin" : "Producer";
        boolean result = dataLayer.deletePerson(dataLayer.getPerson(personID));
        if(result){
            dataLayer.logMessage(user + " (id="+session.getProducerID()+") deleted person with ID: " + personID);
        }
        return result;
    }

    @Override
    public List<IProgramme> getLatestProgrammes() {
        return dataLayer.getLatestProgrammes();
    }

    @Override
    public List<String> getNotifications() {
        return dataLayer.getNotifications(session.getProducerID());
    }

    @Override
    public void setSession(int auth, int id) {
        session = null;
        switch (auth) {
            case 0:
                session = new Session();
                System.out.println("Changed to user");
                break;
            case 1:
                session = new Session(id);
                IProducer producer = dataLayer.getProducer(id);
                dataLayer.logMessage("Producer with id: " + id + " logged in" + " --> " + producer.toString());
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
    public List<IProgramme> getProgrammesForPerson(int personID) {
        return dataLayer.getProgrammesForPerson(personID);
    }

    @Override
    public IProducer chooseProducer(int producerID){
        return dataLayer.getProducer(producerID);
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
        ICredit credit = dataLayer.createCredit(person, functionType);
        IProgramme programme = dataLayer.getProgram(programmeID);
        //programme.addCredit returns void so we can't verify that credit has been added.
        programme.addCredit(credit);
        String user = (session.isAdmin()) ? "Admin" : "Producer";
        dataLayer.logMessage(user + " (id="+session.getProducerID()+") added credit for "  + person.getName() + " as " + functionType + " in " + programme.getName());
        return true;
    }

    @Override
    public boolean removeCredit(int programmeID, ICredit credit) {
        IProgramme programme = dataLayer.getProgram(programmeID);
        programme.removeCredit(credit);
        dataLayer.updateProgramme(programme);
        String user = (session.isAdmin()) ? "Admin" : "Producer";
        dataLayer.logMessage(user + " (id="+session.getProducerID()+") removed credit '"+ credit.toString() +"' from " +programme.getName());
        return true;
    }

    @Override
    public void addProducer(int programmeID, IProducer producer) {
        IProgramme programme = dataLayer.getProgram(programmeID);
        programme.addProducer(producer);
        dataLayer.updateProgramme(programme);
        String user = (session.isAdmin()) ? "Admin" : "Producer";
        dataLayer.logMessage(user + " (id="+session.getProducerID()+") added producer '"+ producer.getCompany() +"' to " +programme.getName());
    }

    @Override
    public void removeProducer(int programmeID, IProducer producer) {
        IProgramme programme = dataLayer.getProgram(programmeID);
        programme.removeProducer(producer);
        dataLayer.updateProgramme(programme);
        String user = (session.isAdmin()) ? "Admin" : "Producer";
        dataLayer.logMessage(user + " (id="+session.getProducerID()+") remove producer  '"+ producer.getCompany() +"' from " +programme.getName());
    }

    @Override
    public boolean commit(){
        return dataLayer.commit();
    }
}
