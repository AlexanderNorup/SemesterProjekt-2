package dk.sdu.seb05.semesterprojekt.PersistenceLayer.DatabaseController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface DatabaseObject {

    PreparedStatement getStatement(Connection connection) throws SQLException;
    DatabaseState getState();
    void setState(DatabaseState newState);
    void setId(int newId);

}
