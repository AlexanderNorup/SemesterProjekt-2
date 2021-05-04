package dk.sdu.seb05.semesterprojekt.PersistenceLayer.DatabaseController;

import dk.sdu.seb05.semesterprojekt.PersistenceLayer.FunctionType;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.ICredit;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IPerson;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseCredit implements ICredit {

    private int id;
    private IPerson person;
    private FunctionType functionType;

    public DatabaseCredit(int id, IPerson person, FunctionType functionType) {
        this.id = id;
        this.person = person;
        this.functionType = functionType;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public IPerson getPerson() {
        return null;
    }

    @Override
    public void setPerson(IPerson newPerson) {

    }

    @Override
    public FunctionType getFunctionType() {
        return null;
    }

    @Override
    public void setFunctionType(FunctionType newFunction) {

    }
}
