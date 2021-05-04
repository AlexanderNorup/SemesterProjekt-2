package dk.sdu.seb05.semesterprojekt.PersistenceLayer.DatabaseController;

import dk.sdu.seb05.semesterprojekt.PersistenceLayer.Category;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.ICredit;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProducer;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProgramme;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.util.*;

public class DatabaseProgramme implements IProgramme, DatabaseObject {
    private int id;
    private String name;
    private Category category;
    private String channel;
    private Date airedDate;
    //private List<ICredit> credits;
    private HashMap<IProducer, DatabaseState> producers;
    private HashMap<ICredit, DatabaseState> credits;
    private DatabaseState state;

    public DatabaseProgramme(int id, String name, Category category, String channel, Date airedDate, List<ICredit> credits, List<IProducer> producers) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.channel = channel;
        this.airedDate = airedDate;
        this.producers = new HashMap<>();
        this.credits = new HashMap<>();
        //this.credits = credits;
        for(ICredit credit : credits){
            this.credits.put(credit, DatabaseState.CLEAN);
        }
        for(IProducer producer : producers){
            this.producers.put(producer, DatabaseState.CLEAN);
        }
        this.state = DatabaseState.BRAND_NEW;
    }

    public PreparedStatement getStatement(Connection connection) throws SQLException {
        switch (this.state){
            case DIRTY:
                PreparedStatement updateStmt = connection.prepareStatement("UPDATE programmes SET name = ?, category = ?, channel = ?, aireddate = ? WHERE id = ?");
                updateStmt.setString(1, name);
                updateStmt.setString(2, category.name());
                updateStmt.setString(3, channel);
                updateStmt.setDate(4, new java.sql.Date(airedDate.getTime()));
                updateStmt.setInt(5, id);
                return updateStmt;
            case BRAND_NEW:
                PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO programmes (name, category, channel, airedDate) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                insertStatement.setString(1, name);
                insertStatement.setString(2, category.name());
                insertStatement.setString(3, channel);
                insertStatement.setDate(4, new java.sql.Date(airedDate.getTime()));
                return insertStatement;
            case TRASH:
                PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM programmes WHERE id = ?");
                deleteStatement.setInt(1, id);
                return deleteStatement;
        }
        return null; //If the state is not set (or is clean), we return null.
    }

    public PreparedStatement[] getProducerBatchStatements(Connection connection) throws SQLException{
        PreparedStatement updateStatement = connection.prepareStatement("UPDATE producer_list SET programme = ? WHERE producer = ?");
        PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO producer_list (programme, producer) VALUES  (?,?)", Statement.RETURN_GENERATED_KEYS);
        PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM producer_list WHERE programme = ? AND producer = ?");

        for(Map.Entry<IProducer, DatabaseState> keyValueSet : this.producers.entrySet()){
            //Løber igennem alle producers
            if(keyValueSet.getValue() == DatabaseState.DIRTY){
                //Produceren er blevet ændret
                updateStatement.setInt(1, this.id);
                updateStatement.setInt(2, keyValueSet.getKey().getId());
                updateStatement.addBatch();
            } else if( keyValueSet.getValue() == DatabaseState.BRAND_NEW){
                //Produceren er helt ny
                insertStatement.setInt(1, this.id);
                insertStatement.setInt(2, keyValueSet.getKey().getId());
                insertStatement.addBatch();
            }else if(keyValueSet.getValue() == DatabaseState.TRASH){
                //Produceren skal smides ud.
                deleteStatement.setInt(1, this.id);
                deleteStatement.setInt(2, keyValueSet.getKey().getId());
                deleteStatement.addBatch();
            }
        }

        return new PreparedStatement[]{updateStatement, insertStatement, deleteStatement};
    }

    public PreparedStatement[] getCreditBatchStatements(Connection connection) throws SQLException{
        PreparedStatement updateStatement = connection.prepareStatement("UPDATE credits_list SET programme = ? WHERE credit = ?");
        PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO credits_list (programme, credit) VALUES  (?,?)", Statement.RETURN_GENERATED_KEYS);
        PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM credits_list WHERE programme = ? AND credit = ?");

        for(Map.Entry<ICredit, DatabaseState> keyValueSet : this.credits.entrySet()){
            //Løber igennem alle credits
            if(keyValueSet.getValue() == DatabaseState.DIRTY){
                //credits er blevet ændret
                updateStatement.setInt(1, this.id);
                updateStatement.setInt(2, keyValueSet.getKey().getId());
                updateStatement.addBatch();
            } else if( keyValueSet.getValue() == DatabaseState.BRAND_NEW){
                //credits er helt ny
                insertStatement.setInt(1, this.id);
                insertStatement.setInt(2, keyValueSet.getKey().getId());
                insertStatement.addBatch();
            }else if(keyValueSet.getValue() == DatabaseState.TRASH){
                //credits skal smides ud.
                deleteStatement.setInt(1, this.id);
                deleteStatement.setInt(2, keyValueSet.getKey().getId());
                deleteStatement.addBatch();
            }
        }

        return new PreparedStatement[]{updateStatement, insertStatement, deleteStatement};
    }


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
        return new ArrayList<>(this.credits.keySet());
    }

    @Override
    public void addCredit(ICredit credit) {
        if(credits.containsKey(credit)){
            credits.put(credit, DatabaseState.DIRTY);
        }else{
            credits.put(credit, DatabaseState.BRAND_NEW);
        }
    }

    @Override
    public void removeCredit(ICredit credit) {
        if(credits.containsKey(credit)){
            credits.put(credit, DatabaseState.TRASH);
        }
    }

    @Override
    public List<IProducer> getProducers() {
        return new ArrayList<>(this.producers.keySet());
    }

    @Override
    public void addProducer(IProducer producer) {
        if(producers.containsKey(producer)){
            //Produceren er allerede på programmet
            producers.put(producer, DatabaseState.DIRTY);
        }else {
            producers.put(producer, DatabaseState.BRAND_NEW);
        }
    }

    @Override
    public void removeProducer(IProducer producer) {
        if(producers.containsKey(producer)) {
            producers.put(producer, DatabaseState.TRASH);
        }
    }

    public DatabaseState getState(){
        return state;
    }

    public void setState(DatabaseState newState){
        this.state = newState;
    }

    @Override
    public void setId(int newId) {
        this.id = newId;
    }
}
