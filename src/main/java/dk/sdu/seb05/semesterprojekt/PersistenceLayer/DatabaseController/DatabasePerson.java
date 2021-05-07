package dk.sdu.seb05.semesterprojekt.PersistenceLayer.DatabaseController;

import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IPerson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class DatabasePerson extends DatabaseObject implements IPerson {

    private String name;
    private Date birthdate;
    private String description;

    public DatabasePerson(int id, String name, Date birthdate, String description) {
        super.id = id;
        this.name = name;
        this.birthdate = birthdate;
        this.description = description;
        this.state = DatabaseState.BRAND_NEW;
    }

    public PreparedStatement getStatement(Connection connection) throws SQLException {
        if(this.getId() < 0){this.state = DatabaseState.BRAND_NEW;}
        switch (this.state){
            case DIRTY:
                PreparedStatement updateStmt = connection.prepareStatement("UPDATE persons SET name = ?, birthdate = ?, description = ? WHERE id = ?");
                updateStmt.setString(1, name);
                updateStmt.setDate(2, new java.sql.Date(birthdate.getTime()));
                updateStmt.setString(3, description);
                updateStmt.setInt(4, id);
                return updateStmt;
            case BRAND_NEW:
                PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO persons (name, birthdate, description) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
                insertStatement.setString(1, name);
                //insertStatement.setString(2, birthdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString());
                insertStatement.setDate(2, new java.sql.Date(birthdate.getTime()));
                insertStatement.setString(3, description);
                return insertStatement;
            case TRASH:
                PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM persons WHERE id = ?");
                deleteStatement.setInt(1, id);
                return deleteStatement;
        }
        return null; //If the state is not set (or is clean), we return null.
    }

    @Override
    public String toString() {
        LocalDate today = LocalDate.now();
        LocalDate birthDate =  getBirthdate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int age = Period.between(birthDate, today).getYears();
        return name + " ("+age+" år)";
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
    public Date getBirthdate() {
        return this.birthdate;
    }

    @Override
    public void setBirthDate(Date newBirthdate) {
        this.birthdate = newBirthdate;
        this.state = DatabaseState.DIRTY;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(String newDescription) {
        this.description = newDescription;
        this.state = DatabaseState.DIRTY;
    }
}
