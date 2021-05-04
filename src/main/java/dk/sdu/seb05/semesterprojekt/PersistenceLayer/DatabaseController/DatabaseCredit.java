package dk.sdu.seb05.semesterprojekt.PersistenceLayer.DatabaseController;

import dk.sdu.seb05.semesterprojekt.PersistenceLayer.FunctionType;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.ICredit;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IPerson;

import java.sql.*;
import java.time.ZoneId;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseCredit implements ICredit, DatabaseObject {

    private int id;
    private IPerson person;
    private FunctionType functionType;
    private DatabaseState state;

    public DatabaseCredit(int id, IPerson person, FunctionType functionType) {
        this.id = id;
        this.person = person;
        this.functionType = functionType;
        this.state = DatabaseState.BRAND_NEW;
    }

    public PreparedStatement getStatement(Connection connection) throws SQLException {
        System.out.println("GetStatement kører i databasecredit");

        switch (this.state){
            case DIRTY:
                PreparedStatement updateStmt = connection.prepareStatement("UPDATE credits SET person = ?, function_type = ? WHERE id = ?");
                updateStmt.setInt(1, person.getId());
                updateStmt.setString(2, functionType.name());
                updateStmt.setInt(3, id);
                return updateStmt;
            case BRAND_NEW:
                PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO credits (person, function_type) VALUES (?,?)");
                insertStatement.setInt(1, person.getId());
                insertStatement.setString(2, functionType.name());
                return insertStatement;
            case TRASH:
                PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM credits WHERE id = ?");
                deleteStatement.setInt(1, id);
                return deleteStatement;
        }
        return null; //If the state is not set (or is clean), we return null.
    }


    @Override
    public String toString() {
        return getPerson().toString() + " - " + functionType;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public IPerson getPerson() {
        return this.person;
    }

    @Override
    public void setPerson(IPerson newPerson) {
        this.person = newPerson;
        this.state = DatabaseState.DIRTY;
    }

    @Override
    public FunctionType getFunctionType() {
        return this.functionType;
    }

    @Override
    public void setFunctionType(FunctionType newFunction) {
        this.functionType = newFunction;
        this.state = DatabaseState.DIRTY;
    }

    public DatabaseState getState(){
        return state;
    }

    public void setState(DatabaseState newState){
        this.state = newState;
    }
}
