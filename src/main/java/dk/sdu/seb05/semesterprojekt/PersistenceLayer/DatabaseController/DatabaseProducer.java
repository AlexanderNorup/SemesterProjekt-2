package dk.sdu.seb05.semesterprojekt.PersistenceLayer.DatabaseController;

import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProducer;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProgramme;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DatabaseProducer extends DatabaseObject implements IProducer {

    private String company;

    public DatabaseProducer(int id, String company)
    {
        this.id = id;
        this.company = company;
        this.state = DatabaseState.BRAND_NEW;
    }

    public PreparedStatement getStatement(Connection connection) throws SQLException{
        switch (this.state) {
            case DIRTY:
                PreparedStatement updateStmt = connection.prepareStatement("UPDATE producers SET company = ? WHERE id = ?");
                updateStmt.setString(1,company);
                updateStmt.setInt(2, id);
                return updateStmt;
            case BRAND_NEW:
                PreparedStatement insertStmt = connection.prepareStatement("INSERT INTO producers (company) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
                insertStmt.setString(1,company);
                return insertStmt;
            case TRASH:
                PreparedStatement deleteStmt = connection.prepareStatement("DELETE FROM producers WHERE id = ?");
                deleteStmt.setInt(1,id);
                return deleteStmt;
        }
        return null; // If the state is not set (or is clean), we return null.
    }

    @Override
    public String toString() {
        return company;
    }

    @Override
    public int getId() {
        return super.getId();
    }

    @Override
    public String getCompany() {
        return this.company;
    }

    @Override
    public void setCompany(String newCompany) {
        this.company = newCompany;
        this.state = DatabaseState.DIRTY;

    }

    @Override
    public List<IProgramme> getProgrammes() {
        return null;
    }

    @Override
    public void addProgramme(IProgramme programme) {
        //TODO: Fix producers having lists of programmes.
    }

    @Override
    public void removeProgramme(IProgramme programme) {

    }
}
