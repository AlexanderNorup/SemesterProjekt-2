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

        //Make path to database
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

        // Connecter og laver et Statement Object til at sende SQL statements til databasen
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
            // Lukker resurcer efter hver statement
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
        connection.setAutoCommit(false); //Start by setting autocommit to false.

        //This method will run through all objects, and commit them to the database

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
                }
                //The programme is now inserted or updated into the database.
                //Now set's setup the relation to credits_list and producers_list

                for(PreparedStatement producerStmt : programme.getProducerBatchStatements(connection)){
                    if(producerStmt != null){
                        producerStmt.executeBatch();
                    }
                }


            }
        }

        connection.commit(); //Fires all commands.
        System.out.println("Committed all edits");
    }

    private void commitObjects(List objects) throws SQLException {
        for(Object object : objects){
            if(object instanceof DatabaseObject){
                DatabaseObject databaseObject = (DatabaseObject) object;
                if(databaseObject.getState() != DatabaseState.CLEAN) {
                    PreparedStatement stmt = databaseObject.getStatement(connection);
                    if (stmt != null) {
                        stmt.execute();
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
    public int createProgramme(String name, Category category, String channel, Date airedDate, List<ICredit> credits, List<IProducer> producers) {
        return 0;
    }

    @Override
    public int createPerson(String name, Date birthdate, String description) {
        return 0;
    }

    @Override
    public int createProducer(String company, List<IProgramme> programmes) {
        return 0;
    }

    @Override
    public int createCredit(IPerson person, FunctionType functionType) {
        return 0;
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
