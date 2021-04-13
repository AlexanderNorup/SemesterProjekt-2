package dk.sdu.seb05.semesterprojekt.PersistenceLayer;

public enum FunctionType {

    CAMERAGUY("Kamera mand"),
    ACTOR("Skuespiller");

    private String description;
    FunctionType(String description) {
        this.description = description;
    }
    public String getDesc() {
        return description;
    }

    @Override
    public String toString(){
        return this.description;
    }

}
