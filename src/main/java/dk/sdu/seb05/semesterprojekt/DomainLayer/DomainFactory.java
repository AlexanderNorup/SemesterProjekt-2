package dk.sdu.seb05.semesterprojekt.DomainLayer;


public class DomainFactory {

    public static IDomainController getDomainLayer(){
        return new DomainController();
    }

}
