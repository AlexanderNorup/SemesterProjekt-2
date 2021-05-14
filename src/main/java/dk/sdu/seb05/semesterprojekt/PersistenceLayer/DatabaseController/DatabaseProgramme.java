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

public class DatabaseProgramme extends DatabaseObject implements IProgramme {

    private String name;
    private Category category;
    private String channel;
    private Date airedDate;

    private HashMap<IProducer, DatabaseState> producers;
    private HashMap<ICredit, DatabaseState> credits;

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
        if(this.getId() < 0){this.state = DatabaseState.BRAND_NEW;}
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
                PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM programmes WHERE id = ? ");
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

    public void OnFinishedCommit(){
        ArrayList<ICredit> creditsToDelete = new ArrayList<>();
        for(Map.Entry<ICredit, DatabaseState> keyValueSet : this.credits.entrySet()){
            if(keyValueSet.getValue() == DatabaseState.TRASH){
                System.out.println("Marking credit '" + keyValueSet.getKey().toString() + "' on " + this.getName() + " for deletion.");
                creditsToDelete.add(keyValueSet.getKey());
                continue;
            }
            this.credits.put(keyValueSet.getKey(), DatabaseState.CLEAN);
        }
        for(Map.Entry<IProducer, DatabaseState> keyValueSet : this.producers.entrySet()){
            if(keyValueSet.getValue() == DatabaseState.TRASH){
                System.out.println("Deleted " + keyValueSet.getKey().getCompany() + " on " + this.getName());
                this.producers.remove(keyValueSet.getKey());
                continue;
            }
            this.producers.put(keyValueSet.getKey(), DatabaseState.CLEAN);
        }

        for(ICredit credit : creditsToDelete){
            this.credits.remove(credit);
        }

    }


    @Override
    public int getId() {
        return super.getId();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String newName) {
        this.name = newName;
        this.state = DatabaseState.DIRTY;
    }

    @Override
    public Category getCategory() {
        return this.category;
    }

    @Override
    public void setCategory(Category newCategory) {
        this.category = newCategory;
        this.state = DatabaseState.DIRTY;
    }

    @Override
    public String getChannel() {
        return this.channel;
    }

    @Override
    public void setChannel(String newChannel) {
        this.channel = newChannel;
        this.state = DatabaseState.DIRTY;
    }

    @Override
    public Date getAiredDate() {
        return this.airedDate;
    }

    @Override
    public void setAiredDate(Date newDate) {
        this.airedDate = newDate;
        this.state = DatabaseState.DIRTY;
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
        this.state = DatabaseState.DIRTY;
    }

    @Override
    public void removeCredit(ICredit credit) {
        credits.put(credit, DatabaseState.TRASH);
        this.state = DatabaseState.DIRTY;
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
        this.state = DatabaseState.DIRTY;
    }

    @Override
    public void removeProducer(IProducer producer) {
        if(producers.containsKey(producer)) {
            producers.put(producer, DatabaseState.TRASH);
        }
        this.state = DatabaseState.DIRTY;
    }

    @Override
    public String toString() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(airedDate);//cal.get(Calendar.YEAR)
        String date = cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.YEAR);
        return "(" + date + ") " + getName() + " - " + getChannel();
    }
}
