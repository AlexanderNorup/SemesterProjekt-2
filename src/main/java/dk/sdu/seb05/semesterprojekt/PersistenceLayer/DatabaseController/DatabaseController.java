package dk.sdu.seb05.semesterprojekt.PersistenceLayer.DatabaseController;

import dk.sdu.seb05.semesterprojekt.PersistenceLayer.*;
import org.postgresql.util.PSQLException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseController implements IDataLayer {

    private List<IPerson> persons;
    private List<IProgramme> programmes;
    private List<IProducer> producers;
    private List<ICredit> credits;
    private Connection connection;

    public DatabaseController(){
        persons = new ArrayList<>();
        programmes = new ArrayList<>();
        producers = new ArrayList<>();
        credits = new ArrayList<>();

        //the path to database
        String url = "jdbc:postgresql://hosting.alexandernorup.com:5432/tv2"; //Connection Url
        String user = "java";
        String password = null;
        try {
            File passwordFile = new File("auth.txt");
            password = Files.readString(Path.of(passwordFile.toURI()), StandardCharsets.UTF_8).trim();
        } catch (IOException e) {
            System.out.println("Kunne ikke indlæse adgangskoden.");
            e.printStackTrace();
        }

        //Connects and makes a Statement Object to send SQL Statements to the database
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException throwables) {
            System.out.println("Kunne ikke forbinde til databasen.");
            throwables.printStackTrace();
        }
    }


    public boolean checkConnection(){
        if(connection == null) return false;
        try  (Statement statement = connection.createStatement();)
        {
            ResultSet resultSet = statement.executeQuery("SELECT VERSION()");
            // Closes resorces after the statement
            if (resultSet.next()) {
                System.out.println("Database Version: " + resultSet.getString(1));
                return true;
            }
        } catch(SQLException dataEx ) {
            System.out.println("SQLException i checkConnection(): " + dataEx.getMessage());
            dataEx.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        DatabaseController dataLayer = new DatabaseController();
        if(dataLayer.checkConnection()){
            System.out.println("Vi er klar til at bruge databasen!");
            try {
                dataLayer.saveData();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else{
            System.out.println("Vi kunne ikke forbinde til databasen :/");
        }
    }

    public void saveData() throws SQLException {
        long start_time = System.currentTimeMillis();
        connection.setAutoCommit(false); //Start by setting autocommit to false, because we only want to commit if everything's good.

        //This method will run through all objects, and commit them to the database
/*
        IPerson person = new DatabasePerson(-1,"Egon", new Date(), "Har hest");
        ICredit credit = new DatabaseCredit(-1, person, FunctionType.ANIMATION);
        IProducer producer = new DatabaseProducer(-1, "SDU");
        IProgramme programme__ = new DatabaseProgramme(-1, "Stormester", Category.TV_SERIES, "TV3", new Date(), new ArrayList<>(), new ArrayList<>());
        programme__.addCredit(credit);
        programme__.addProducer(producer);

        programmes.add(programme__);
        producers.add(producer);
        persons.add(person);
        credits.add(credit);
;*/



        //We start with persons
        commitObjects(persons);
        //Then we save credits
        commitObjects(credits);
        //Then we save producers
        commitObjects(producers);

        //Programme is a little special
        for(IProgramme programme_ : programmes){
            DatabaseProgramme programme = (DatabaseProgramme) programme_;
            if(programme.getState() != DatabaseState.CLEAN){
                PreparedStatement stmt = programme.getStatement(connection);
                if (stmt != null) {
                    stmt.execute();
                    if(programme.getState() == DatabaseState.BRAND_NEW) {
                        //If the programme was brand new (and thus just only inserted), we give it it's insertID back.
                        ResultSet resultSet = stmt.getGeneratedKeys();
                        if (resultSet.next()) {
                            int insertKey = resultSet.getInt(1);
                            programme.setId(insertKey);
                        }
                    }
                }

                //The programme is now inserted or updated into the database.
                //Now set's setup the relation to credits_list and producers_list

                for(PreparedStatement producerStmt : programme.getProducerBatchStatements(connection)){
                    if(producerStmt != null){
                        producerStmt.executeBatch();
                    }
                }

                for(PreparedStatement creditStmt : programme.getCreditBatchStatements(connection)){
                    if(creditStmt != null){
                        creditStmt.executeBatch();
                    }
                }
            }
        }

        connection.commit(); //Fires all commands.
        long end_time = System.currentTimeMillis();
        System.out.println("Committed all edits. Took " + (end_time-start_time) + "ms");

        resetStates(persons);
        resetStates(credits);
        resetStates(producers);
        resetStates(programmes);

        //TODO: Lav JavaDoc
    }

    private void resetStates(List objects){
        for(Object object : objects) {
            if (object instanceof DatabaseObject) {
                DatabaseObject databaseObject = (DatabaseObject) object;
                databaseObject.setState(DatabaseState.CLEAN);
            }
        }
    }

    private void commitObjects(List objects) throws SQLException {
        for(Object object : objects){
            if(object instanceof DatabaseObject){
                DatabaseObject databaseObject = (DatabaseObject) object;
                if(databaseObject.getState() != DatabaseState.CLEAN) {
                    PreparedStatement stmt = databaseObject.getStatement(connection);
                    if (stmt != null) {
                        stmt.execute();
                        if(databaseObject.getState() == DatabaseState.BRAND_NEW) {
                            //If the object was brand new (and thus just only inserted), we give it it's insertID back.
                            ResultSet resultSet = stmt.getGeneratedKeys();
                            if (resultSet.next()) {
                                int insertKey = resultSet.getInt(1);
                                databaseObject.setId(insertKey);
                            }
                        }
                    }
                }
            }
        }
    }



    @Override
    public List<IPerson> getPersons() {
        return this.persons;
    }

    @Override
    public List<IProgramme> getProgrammes() {
        return this.programmes;
    }

    @Override
    public List<IProducer> getProducers() {
        return null;
    }

    @Override
    public IProgramme getProgram(int programId) {
        return null;
    }

    @Override
    public IProducer getProducer(int producerId) {
        return null;
    }

    @Override
    public IPerson getPerson(int personId) {
        return null;
    }

    @Override
    public ICredit getCredit(int creditId) {
        return null;
    }

    @Override
    public boolean updateProgramme(IProgramme iProgramme) {
        return false;
    }

    @Override
    public boolean updatePerson(IPerson iPerson) {
        return false;
    }

    @Override
    public boolean updateProducer(IProducer iProducer) {
        return false;
    }

    @Override
    public boolean updateCredit(ICredit iCredit) {
        return false;
    }

    @Override
    public IProgramme createProgramme(String name, Category category, String channel, Date airedDate, List<ICredit> credits, List<IProducer> producers) {
        return null;
    }

    @Override
    public IPerson createPerson(String name, Date birthdate, String description) {
        return null;
    }

    @Override
    public IProducer createProducer(String company, List<IProgramme> programmes) {
        return null;
    }

    @Override
    public ICredit createCredit(IPerson person, FunctionType functionType) {
        return null;
    }

    @Override
    public boolean deleteProgramme(IProgramme iProgramme) {
        return false;
    }

    @Override
    public boolean deletePerson(IPerson iPerson) {
        return false;
    }

    @Override
    public boolean deleteProducer(IProducer iProducer) {
        return false;
    }

    @Override
    public boolean deleteCredit(ICredit iCredit) {
        return false;
    }

    @Override
    public String exportData() {
        return null;
    }

    @Override
    public String exportData(int producerId) {
        return null;
    }

    @Override
    public String getStatistics() {
        return null;
    }

    @Override
    public String getStatistics(int producerId) {
        return null;
    }

    @Override
    public List<IProgramme> searchForProgramme(String query) {
        return null;
    }

    @Override
    public List<IPerson> searchForPerson(String query) {
        return null;
    }

    @Override
    public List<IProducer> searchForProducer(String query) {
        return null;
    }

    @Override
    public List<IProgramme> getProgrammesForPerson(int personId) {
        return null;
    }

    @Override
    public List<ICredit> getCreditsForPerson(int personId) {
        return null;
    }

    @Override
    public List<IProgramme> getLatestProgrammes() {
        return null;
    }

    @Override
    public List<String> getNotifications(int producerId) {
        return null;
    }

    @Override
    public boolean commit() {
        return false;
    }

    @Override
    public void logMessage(String message) {

    }
}
