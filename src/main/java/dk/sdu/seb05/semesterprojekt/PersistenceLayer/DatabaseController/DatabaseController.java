package dk.sdu.seb05.semesterprojekt.PersistenceLayer.DatabaseController;

import dk.sdu.seb05.semesterprojekt.PersistenceLayer.*;
import org.postgresql.util.PSQLException;

import javax.xml.transform.Result;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseController implements IDataLayer {
    public static final long DEFAULT_CACHE_INVALIDATION_TIME = (60 * 10 * 1000); //10 minutes.

    private long programmeLastFetch = -1;
    private long personLastFetch = -1;
    private long producerLastFetch = -1;

    private int tempInsertId = -1;
    private Set<CachedDatabaseObject> persons;
    private Set<CachedDatabaseObject> programmes;
    private Set<CachedDatabaseObject> producers;
    private Set<CachedDatabaseObject> credits;
    private Connection connection;

    public DatabaseController() {
        persons = new TreeSet<>();
        programmes = new TreeSet<>();
        producers = new TreeSet<>();
        credits = new TreeSet<>();

        //TODO: Læs ind fra fil
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


    public boolean checkConnection() {
        if (connection == null) return false;
        try (Statement statement = connection.createStatement();) {
            ResultSet resultSet = statement.executeQuery("SELECT VERSION()");
            // Closes resorces after the statement
            if (resultSet.next()) {
                System.out.println("Database Version: " + resultSet.getString(1));
                return true;
            }
        } catch (SQLException dataEx) {
            System.out.println("SQLException i checkConnection(): " + dataEx.getMessage());
            dataEx.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        DatabaseController dataLayer = new DatabaseController();
        if (dataLayer.checkConnection()) {
            System.out.println("Vi er klar til at bruge databasen!");
            try {
                long start_time = System.currentTimeMillis();
                List<IProgramme> searchResults = dataLayer.searchForProgramme("stor");
                long end_time = System.currentTimeMillis();
                System.out.println("Found " + searchResults.size() + " program search results in: " + (end_time-start_time) + "ms");
                System.out.println(searchResults);
                //dataLayer.saveData();
            } catch (Exception throwables) {
                throwables.printStackTrace();
            }
        } else {
            System.out.println("Vi kunne ikke forbinde til databasen :/");
        }
    }

    public void saveData() throws SQLException {
        long start_time = System.currentTimeMillis();
        connection.setAutoCommit(false); //Start by setting autocommit to false, because we only want to commit if everything's good.

        //We start with persons
        commitObjects(persons);
        //Then we save credits
        commitObjects(credits);
        //Then we save producers
        commitObjects(producers);

        //Programme is a little special
        for (CachedDatabaseObject programme_ : programmes) {
            DatabaseProgramme programme = (DatabaseProgramme) programme_.getObject();
            if (programme.getState() != DatabaseState.CLEAN) {
                PreparedStatement stmt = programme.getStatement(connection);
                if (stmt != null) {
                    stmt.execute();
                    if (programme.getState() == DatabaseState.BRAND_NEW) {
                        //If the programme was brand new (and thus just only inserted), we give it it's insertID back.
                        ResultSet resultSet = stmt.getGeneratedKeys();
                        if (resultSet.next()) {
                            int insertKey = resultSet.getInt(1);
                            programme.setId(insertKey);
                        }
                    }
                    stmt.close();
                }

                //The programme is now inserted or updated into the database.
                //Now set's setup the relation to credits_list and producers_list

                for (PreparedStatement producerStmt : programme.getProducerBatchStatements(connection)) {
                    if (producerStmt != null) {
                        producerStmt.executeBatch();
                        producerStmt.close();
                    }
                }

                for (PreparedStatement creditStmt : programme.getCreditBatchStatements(connection)) {
                    if (creditStmt != null) {
                        creditStmt.executeBatch();
                        creditStmt.close();
                    }
                }
            }
        }



        connection.commit(); //Fires all commands.
        long end_time = System.currentTimeMillis();
        System.out.println("Committed all edits. Took " + (end_time - start_time) + "ms");

        resetStates(persons);
        resetStates(credits);
        resetStates(producers);
        resetStates(programmes);

        for(CachedDatabaseObject programme : programmes){
            if(programme.getObject() instanceof DatabaseProgramme){
                ((DatabaseProgramme) programme.getObject()).OnFinishedCommit();
            }
        }

        //TODO: Lav JavaDoc
    }

    private void resetStates(Set<CachedDatabaseObject> objects) {
        for (CachedDatabaseObject object : objects) {
            DatabaseObject databaseObject = object.getObject();
            databaseObject.setState(DatabaseState.CLEAN);
        }
    }

    private void commitObjects(Set<CachedDatabaseObject> objects) throws SQLException {
        for (CachedDatabaseObject object : objects) {
            DatabaseObject databaseObject = object.getObject();
            if (databaseObject.getState() != DatabaseState.CLEAN) {
                PreparedStatement stmt = databaseObject.getStatement(connection);
                if (stmt != null) {
                    stmt.execute();
                    if (databaseObject.getState() == DatabaseState.BRAND_NEW) {
                        //If the object was brand new (and thus just only inserted), we give it it's insertID back.
                        ResultSet resultSet = stmt.getGeneratedKeys();
                        if (resultSet.next()) {
                            int insertKey = resultSet.getInt(1);
                            databaseObject.setId(insertKey);
                        }
                    }else if(databaseObject.getState() == DatabaseState.TRASH){
                        objects.remove(object);
                    }
                    stmt.close();
                }
            }
        }
    }

    @Override
    public List<IPerson> getPersons() {
        List<IPerson> personsList = new ArrayList<>();

        if (System.currentTimeMillis() - personLastFetch <= DEFAULT_CACHE_INVALIDATION_TIME) {
            //If it is less than 10 minutes since we last fecthed all persons, we just use the old fetch.
            for (CachedDatabaseObject cachedDatabaseObject : persons) {
                if (cachedDatabaseObject.getObject() instanceof IPerson) {
                    personsList.add((IPerson) cachedDatabaseObject.getObject());
                }
            }
            return personsList;
        }
        //We could not find the persons in the cache. So we just download them all again
        try (PreparedStatement stmt = connection.prepareStatement("SELECT id, name, birthdate, description FROM persons;")) {
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                DatabasePerson person = getPerson(resultSet);
                personsList.add(person);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        personLastFetch = System.currentTimeMillis();
        return personsList;
    }

    private DatabasePerson getPerson(ResultSet resultSet) {
        try {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            java.util.Date birthdate = new Date(resultSet.getDate("birthdate").getTime());
            String description = resultSet.getString("description");
            DatabasePerson person = new DatabasePerson(id, name, birthdate, description);
            person.setState(DatabaseState.CLEAN);
            this.persons.add(new CachedDatabaseObject(person));
            return person;
        } catch (SQLException e) {
            System.out.println("Could not parse ResultSet as an DatabasePerson: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<IProgramme> getProgrammes() {
        List<IProgramme> programmeList = new ArrayList<>();

        if (System.currentTimeMillis() - programmeLastFetch <= DEFAULT_CACHE_INVALIDATION_TIME) {
            //If it is less than 10 minutes since we last fecthed all programmes, we just use the old fetch.
            for (CachedDatabaseObject cachedDatabaseObject : programmes) {
                if (cachedDatabaseObject.getObject() instanceof IProgramme) {
                    programmeList.add((IProgramme) cachedDatabaseObject.getObject());
                }
            }
            return programmeList;
        }

        try (PreparedStatement stmt = connection.prepareStatement("SELECT id, name, category, channel, aireddate FROM programmes;")) {
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                DatabaseProgramme programmes = getProgramme(resultSet);
                programmeList.add(programmes);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        programmeLastFetch = System.currentTimeMillis();
        return programmeList;
    }

    private DatabaseProgramme getProgramme(ResultSet resultSet) {
        try {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String category_ = resultSet.getString("category");
            Category category;
            try {
                category = Category.valueOf(category_);
            } catch (IllegalArgumentException e) {
                category = Category.UNKNOWN;
            }
            String channel = resultSet.getString("channel");
            java.util.Date airedDate = new Date(resultSet.getDate("aireddate").getTime());


            //Fetch all credits for this programme.
            PreparedStatement creditStmt = connection.prepareStatement("SELECT C.id, C.function_type FROM credits C " +
                    "INNER JOIN credits_list cl on C.id = cl.credit " +
                    "WHERE cl.programme = ?;");
            creditStmt.setInt(1, id);
            ResultSet creditResults = creditStmt.executeQuery();
            ArrayList<ICredit> credits = new ArrayList<>();
            while (creditResults.next()) {
                credits.add(getCredit(creditResults.getInt("id")));
            }
            creditStmt.close();

            //Fetch all producers for this programme.
            PreparedStatement producerStmt = connection.prepareStatement("SELECT PR.id FROM  producers PR " +
                    "INNER JOIN producer_list pl on PR.id = pl.producer " +
                    "WHERE pl.programme = ?;");
            producerStmt.setInt(1, id);
            ResultSet producerResults = producerStmt.executeQuery();
            ArrayList<IProducer> producers = new ArrayList<>();
            while (producerResults.next()) {
                producers.add(getProducer(producerResults.getInt("id")));
            }
            producerStmt.close();

            DatabaseProgramme programme = new DatabaseProgramme(id, name, category, channel, airedDate, credits, producers);
            programme.setState(DatabaseState.CLEAN);
            this.programmes.add(new CachedDatabaseObject(programme));
            return programme;
        } catch (SQLException e) {
            System.out.println("Could not parse ResultSet as an DatabaseProgramme: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<IProducer> getProducers() {
        List<IProducer> producerList = new ArrayList<>();

        if (System.currentTimeMillis() - producerLastFetch <= DEFAULT_CACHE_INVALIDATION_TIME) {
            //If it is less than 10 minutes since we last fecthed all producers, we just use the old fetch.
            for (CachedDatabaseObject cachedDatabaseObject : producers) {
                if (cachedDatabaseObject.getObject() instanceof IProducer) {
                    producerList.add((IProducer) cachedDatabaseObject.getObject());
                }
            }
            return producerList;
        }

        try (PreparedStatement stmt = connection.prepareStatement("SELECT id, company FROM producers;")) {
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                DatabaseProducer producer = getProducer(resultSet);
                producerList.add(producer);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        producerLastFetch = System.currentTimeMillis();
        return producerList;
    }

    private DatabaseProducer getProducer(ResultSet resultSet) {
        try {
            int id = resultSet.getInt("id");
            String company = resultSet.getString("company");


            DatabaseProducer producer = new DatabaseProducer(id, company);
            producer.setState(DatabaseState.CLEAN);
            this.producers.add(new CachedDatabaseObject(producer));
            return producer;
        } catch (SQLException e) {
            System.out.println("Could not parse ResultSet as an DatabaseProducer: " + e.getMessage());
        }
        return null;
    }

    private DatabaseCredit getCredit(ResultSet resultSet) {
        try {
            int id = resultSet.getInt("id");
            int personId = resultSet.getInt("person");
            String functionType_ = resultSet.getString("function_type");
            FunctionType functionType;
            try {
                functionType = FunctionType.valueOf(functionType_);
            } catch (IllegalArgumentException e) {
                functionType = FunctionType.UNKNOWN;
            }

            IPerson person = getPerson(personId);

            DatabaseCredit credit = new DatabaseCredit(id, person, functionType);
            credit.setState(DatabaseState.CLEAN);
            this.credits.add(new CachedDatabaseObject(credit));
            return credit;
        } catch (SQLException e) {
            System.out.println("Could not parse ResultSet as an DatabasePerson: " + e.getMessage());
        }
        return null;
    }

    //Our search algorith
    public DatabaseObject findInCache(Set<CachedDatabaseObject> cache, int idToFind) {
        //TODO: Binary Search
        for (CachedDatabaseObject o : cache) {
            if (o.getObject().getId() == idToFind) {
                if (o.isExpired()) {
                    //We found the one we need, but it's too old, so we'd rather get a new instance from the database.
                    return null;
                }
                return o.getObject();
            }
        }
        return null;
    }


    @Override
    public IProgramme getProgram(int programId) {
        DatabaseObject databaseObject = findInCache(programmes, programId);
        if (databaseObject instanceof IProgramme) {
            return (IProgramme) databaseObject;
        }
        //The object is not in the cache, therefore we try to ask the database.

        IProgramme programme = null;
        try (PreparedStatement stmt = connection.prepareStatement("SELECT id, name, category, channel, aireddate FROM programmes WHERE id = ?")) {
            stmt.setInt(1, programId);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                programme = getProgramme(resultSet);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return programme;
    }

    @Override
    public IProducer getProducer(int producerId) {
        DatabaseObject databaseObject = findInCache(producers, producerId);
        if (databaseObject instanceof IProducer) {
            return (IProducer) databaseObject;
        }
        //The object is not in the cache, therefore we try to ask the database.

        IProducer producer = null;

        try (PreparedStatement stmt = connection.prepareStatement("SELECT id, company FROM producers WHERE id = ?")) {
            stmt.setInt(1, producerId);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                producer = getProducer(resultSet);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return producer;
    }

    @Override
    public IPerson getPerson(int personId) {
        DatabaseObject databaseObject = findInCache(persons, personId);
        if (databaseObject instanceof IPerson) {
            return (IPerson) databaseObject;
        }
        //The object is not in the cache, therefore we try to ask the database.

        IPerson person = null;
        try (PreparedStatement stmt = connection.prepareStatement("SELECT id, name, birthdate, description FROM persons where id = ?")) {
            stmt.setInt(1, personId);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                person = getPerson(resultSet);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return person;
    }

    @Override
    public ICredit getCredit(int creditId) {
        DatabaseObject databaseObject = findInCache(credits, creditId);
        if (databaseObject instanceof ICredit) {
            return (ICredit) databaseObject;
        }
        //The object is not in the cache, therefore we try to ask the database.

        ICredit credit = null;

        try (PreparedStatement stmt = connection.prepareStatement("SELECT id, person, function_type FROM credits where id = ?")) {
            stmt.setInt(1, creditId);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                credit = getCredit(resultSet);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return credit;
    }

    @Override
    public boolean updateProgramme(IProgramme iProgramme) {
        if(iProgramme instanceof DatabaseObject){
            ((DatabaseObject) iProgramme).setState(DatabaseState.DIRTY);
            CachedDatabaseObject newProgramme = new CachedDatabaseObject((DatabaseObject) iProgramme);
            programmes.remove(newProgramme); //Adding and removing to update, since add() in treeset is ignored if exists.
            programmes.add(newProgramme);
        }
        return true;
    }

    @Override
    public boolean updatePerson(IPerson iPerson) {
        if(iPerson instanceof DatabaseObject){
            ((DatabaseObject) iPerson).setState(DatabaseState.DIRTY);
            CachedDatabaseObject newPerson = new CachedDatabaseObject((DatabaseObject) iPerson);
            persons.remove(newPerson);
            persons.add(newPerson);
        }
        return true;
    }

    @Override
    public boolean updateProducer(IProducer iProducer) {
        if(iProducer instanceof DatabaseObject){
            ((DatabaseObject) iProducer).setState(DatabaseState.DIRTY);
            CachedDatabaseObject newProducer = new CachedDatabaseObject((DatabaseObject) iProducer);
            producers.remove(newProducer);
            producers.add(newProducer);
        }
        return true;
    }

    @Override
    public boolean updateCredit(ICredit iCredit) {
        if(iCredit instanceof DatabaseObject){
            ((DatabaseObject) iCredit).setState(DatabaseState.DIRTY);
            CachedDatabaseObject newCredit = new CachedDatabaseObject((DatabaseObject) iCredit);
            credits.remove(newCredit);
            credits.add(newCredit);
        }
        return true;
    }

    @Override
    public IProgramme createProgramme(String name, Category category, String channel, Date airedDate, List<ICredit> credits, List<IProducer> producers) {
        DatabaseProgramme programme = new DatabaseProgramme(tempInsertId--, name, category, channel, airedDate, new ArrayList<>(), new ArrayList<>());
        for(ICredit credit : credits){
            programme.addCredit(credit); //Doing it here, and not in the constructor for DatabaseProgramme, because otherwise the credit gets the wrong state of "CLEAN".
        }
        for(IProducer producer : producers){
            programme.addProducer(producer);
        }
        programme.setState(DatabaseState.BRAND_NEW);
        this.programmes.add(new CachedDatabaseObject(programme));
        return programme;
    }

    @Override
    public IPerson createPerson(String name, Date birthdate, String description) {
        DatabasePerson person = new DatabasePerson(tempInsertId--, name, birthdate, description);
        person.setState(DatabaseState.BRAND_NEW);
        this.persons.add(new CachedDatabaseObject(person));
        return person;
    }

    @Override
    public IProducer createProducer(String company) {
        DatabaseProducer producer = new DatabaseProducer(tempInsertId--, company);
        producer.setState(DatabaseState.BRAND_NEW);
        producers.add(new CachedDatabaseObject(producer));
        return producer;
    }

    @Override
    public ICredit createCredit(IPerson person, FunctionType functionType) {
        DatabaseCredit credit = new DatabaseCredit(tempInsertId--, person, functionType);
        credit.setState(DatabaseState.BRAND_NEW);
        credits.add(new CachedDatabaseObject(credit));
        return credit;
    }

    @Override
    public boolean deleteProgramme(IProgramme iProgramme) {
        if(iProgramme instanceof DatabaseProgramme){
            ((DatabaseProgramme) iProgramme).setState(DatabaseState.TRASH);
            return true;
        }
        return false;
    }

    @Override
    public boolean deletePerson(IPerson iPerson) {
        if(iPerson instanceof DatabasePerson){
            ((DatabasePerson) iPerson).setState(DatabaseState.TRASH);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteProducer(IProducer iProducer) {
        if(iProducer instanceof DatabaseProducer){
            ((DatabaseProducer) iProducer).setState(DatabaseState.TRASH);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteCredit(ICredit iCredit) {
        if(iCredit instanceof DatabaseCredit){
            ((DatabaseCredit) iCredit).setState(DatabaseState.TRASH);
        }
        return false;
    }

    @Override
    public String exportData() {
        throw new UnsupportedOperationException("Exporting is not supported");
    }

    @Override
    public String exportData(int producerId) {
        throw new UnsupportedOperationException("Exporting is not supported");
    }

    @Override
    public String getStatistics() {
        throw new UnsupportedOperationException("Statistics is not supported");
    }

    @Override
    public String getStatistics(int producerId) {
        throw new UnsupportedOperationException("Statistics is not supported");
    }

    @Override
    public List<IProgramme> searchForProgramme(String query) {
        List<IProgramme> programmes = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("--Starter med programnavn\n" +
                                                                    "SELECT 1 as sorting, PRO.* FROM programmes PRO\n" +
                                                                    "WHERE LOWER(PRO.name) LIKE LOWER(?)\n" +
                                                                    "UNION\n" +
                                                                    "--Indeholder programnavn:\n" +
                                                                    "SELECT 2 as sorting, PRO.* FROM programmes PRO\n" +
                                                                    "WHERE LOWER(PRO.name) LIKE LOWER(?)\n" +
                                                                    "UNION\n" +
                                                                    "-- FIND BY PERSON\n" +
                                                                    "SELECT 3 as sorting, PRO.* FROM programmes PRO\n" +
                                                                    "INNER JOIN credits_list cl on PRO.id = cl.programme\n" +
                                                                    "INNER JOIN credits c on cl.credit = c.id\n" +
                                                                    "INNER JOIN persons pers on c.person = pers.id\n" +
                                                                    "WHERE LOWER(pers.name) LIKE LOWER(?)\n" +
                                                                    "UNION\n" +
                                                                    "-- FIND BY PRODUCER\n" +
                                                                    "SELECT 4 as sorting, PRO.* FROM programmes PRO\n" +
                                                                    "INNER JOIN producer_list pl on PRO.id = pl.programme\n" +
                                                                    "INNER JOIN producers produ on pl.producer = produ.id\n" +
                                                                    "WHERE LOWER(produ.company) LIKE LOWER(?)\n" +
                                                                    "ORDER BY sorting;");
            stmt.setString(1, query + "%");
            stmt.setString(2, "%" + query + "%");
            stmt.setString(3, "%" + query + "%");
            stmt.setString(4, "%" + query + "%");
            ResultSet resultSet = stmt.executeQuery();
            while(resultSet.next()){
                IProgramme programme = getProgramme(resultSet);
                if(!programmes.contains(programme)) {
                    programmes.add(programme);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return programmes;
    }

    @Override
    public List<IPerson> searchForPerson(String query) {
        List<IPerson> persons = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("--Starter med Person navn\n" +
                                                                    "SELECT 1 as sorting, PERS.* FROM persons PERS\n" +
                                                                    "WHERE LOWER(PERS.name) LIKE LOWER(?)\n" +
                                                                    "UNION\n" +
                                                                    "--Indeholder person navn:\n" +
                                                                    "SELECT 2 as sorting, PERS.* FROM persons PERS\n" +
                                                                    "WHERE LOWER(PERS.name) LIKE LOWER(?)\n" +
                                                                    "UNION\n" +
                                                                    "-- FIND BY PRORAM NAME\n" +
                                                                    "SELECT 3 as sorting, PERS.* FROM persons PERS\n" +
                                                                    "INNER JOIN credits c on PERS.id = c.person\n" +
                                                                    "INNER JOIN credits_list cl on c.id = cl.credit\n" +
                                                                    "INNER JOIN programmes p on p.id = cl.programme\n" +
                                                                    "WHERE LOWER(p.name) LIKE LOWER(?)\n" +
                                                                    "ORDER BY sorting;");
            stmt.setString(1, query + "%");
            stmt.setString(2, "%" + query + "%");
            stmt.setString(3, "%" + query + "%");
            ResultSet resultSet = stmt.executeQuery();
            while(resultSet.next()){
                IPerson person = getPerson(resultSet);
                if(!persons.contains(person)) {
                    persons.add(person);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return persons;
    }

    @Override
    public List<IProducer> searchForProducer(String query) {
        List<IProducer> producers = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("--Starter med Producer navn\n" +
                                                                    "SELECT 1 as sorting, PROD.* FROM producers PROD\n" +
                                                                    "WHERE LOWER(PROD.company) LIKE LOWER(?)\n" +
                                                                    "UNION\n" +
                                                                    "--Indeholder producer navn:\n" +
                                                                    "SELECT 2 as sorting, PROD.* FROM producers PROD\n" +
                                                                    "WHERE LOWER(PROD.company) LIKE LOWER(?)\n" +
                                                                    "UNION\n" +
                                                                    "-- FIND BY PRORAM NAME\n" +
                                                                    "SELECT 3 as sorting, PROD.* FROM producers PROD\n" +
                                                                    "INNER JOIN producer_list PROD_LIST ON PROD.id = PROD_LIST.producer\n" +
                                                                    "INNER JOIN programmes P on PROD_LIST.programme = P.id\n" +
                                                                    "WHERE LOWER(P.name) LIKE LOWER(?)\n" +
                                                                    "ORDER BY sorting;");
            stmt.setString(1, query + "%");
            stmt.setString(2, "%" + query + "%");
            stmt.setString(3, "%" + query + "%");
            ResultSet resultSet = stmt.executeQuery();
            while(resultSet.next()){
                IProducer producer = getProducer(resultSet);
                if(!producers.contains(producer)) {
                    producers.add(producer);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return producers;
    }

    @Override
    public List<IProgramme> getProgrammesForPerson(int personId) {
        List<IProgramme> programmes = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT PRO.* FROM programmes PRO\n" +
                                                                    "INNER JOIN credits_list cl on PRO.id = cl.programme\n" +
                                                                    "INNER JOIN credits c on c.id = cl.credit\n" +
                                                                    "INNER JOIN persons p on p.id = c.person\n" +
                                                                    "WHERE p.id = ?;");
            stmt.setInt(1, personId);
            ResultSet resultSet = stmt.executeQuery();
            while(resultSet.next()){
                IProgramme programme = getProgramme(resultSet);
                programmes.add(programme);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return programmes;
    }

    @Override
    public List<IProgramme> getProgrammesForProducer(int producerId) {
        List<IProgramme> programmes = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT PRO.* FROM programmes PRO\n" +
                                                                    "INNER JOIN producer_list pl on PRO.id = pl.programme\n" +
                                                                    "INNER JOIN producers p on pl.producer = p.id\n" +
                                                                    "WHERE p.id = ?;");
            stmt.setInt(1, producerId);
            ResultSet resultSet = stmt.executeQuery();
            while(resultSet.next()){
                IProgramme programme = getProgramme(resultSet);
                programmes.add(programme);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return programmes;
    }

    @Override
    public List<ICredit> getCreditsForPerson(int personId) {
        List<ICredit> credits = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT C.id, C.person, C.function_type FROM credits C\n" +
                    "INNER JOIN persons p on p.id = c.person\n" +
                    "WHERE p.id = ?;");
            stmt.setInt(1, personId);
            ResultSet resultSet = stmt.executeQuery();
            while(resultSet.next()){
                ICredit credit = getCredit(resultSet);
                credits.add(credit);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return credits;
    }

    @Override
    public List<IProgramme> getLatestProgrammes() {
        List<IProgramme> programmes = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT id, name, category, channel, aireddate FROM programmes ORDER BY aireddate DESC LIMIT 15;");
            ResultSet resultSet = stmt.executeQuery();
            while(resultSet.next()){
                IProgramme programme = getProgramme(resultSet);
                programmes.add(programme);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return programmes;
    }

    @Override
    public List<String> getNotifications(int producerId) {
        return Arrays.asList("Test notification 1", "Test notification 2");
    }

    @Override
    public boolean commit() {
        try{
            saveData();
            return true;
        }catch (Exception throwables) {
            System.out.println("An error occoured when saving: " + throwables.getMessage() + "\nTrying to rollback!");
            try {
                connection.rollback(); //Undoes any changes that might have occoured
                System.out.println("Rollback succeded!");
            } catch (SQLException e) {
                System.out.println("Rollback failed: " + e.getMessage());
            }
        }
        return false;
    }

    @Override
    public void logMessage(String message) {
        System.out.println("[Temp-Log-Database] " + message);
    }
}
