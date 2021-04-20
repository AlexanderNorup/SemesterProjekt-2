package dk.sdu.seb05.semesterprojekt.DomainLayer;


public class Session {
    public int producerID;
    public boolean isAdmin;

    public Session(){
        this.producerID = 0;
        this.isAdmin = false;
    }

    public Session(int producerID){
        this.producerID = producerID;
        this.isAdmin = false;
    }

    public Session(boolean isAdmin){
        this.producerID = 0;
        this.isAdmin = isAdmin;
    }

    public boolean isAdmin(){
        return isAdmin;
    }

    public int getProducerID(){
        return producerID;
    }

}
