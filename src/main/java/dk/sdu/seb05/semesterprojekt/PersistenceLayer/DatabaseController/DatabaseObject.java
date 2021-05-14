package dk.sdu.seb05.semesterprojekt.PersistenceLayer.DatabaseController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class DatabaseObject {
    protected int id;
    protected DatabaseState state;

    abstract PreparedStatement getStatement(Connection connection) throws SQLException;

    public DatabaseState getState(){
        return state;
    }

    public void setState(DatabaseState newState){
        this.state = newState;
    }

    public void setId(int newId) {
        this.id = newId;
    }

    int getId(){
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DatabaseObject){
            if(((DatabaseObject) obj).getId() == this.getId() //Hvis de har samme id
                    && obj.getClass().equals(this.getClass())){ //OG hvis de har samme class
                return true;                                    //Så er de ens
            }
        }
        return false;
    }
}
