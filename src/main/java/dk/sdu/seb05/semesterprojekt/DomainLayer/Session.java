package dk.sdu.seb05.semesterprojekt.DomainLayer;


public class Session {
    private int producerID;
    private boolean isAdmin;

    public Session(){
        this.producerID = -2;
        this.isAdmin = false;
    }

    public Session(int producerID){
        this.producerID = producerID;
        this.isAdmin = false;
    }

    public Session(boolean isAdmin){
        this.producerID = -1;
        this.isAdmin = isAdmin;
    }

    public boolean isAdmin(){
        return isAdmin;
    }

    public int getProducerID(){
        return producerID;
    }

}
