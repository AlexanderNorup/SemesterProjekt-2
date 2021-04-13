package dk.sdu.seb05.semesterprojekt.PersistenceLayer;

public enum Category {

    DOCUMENTARY("Dokumentar"),
    NEWS("Nyhederne.");

    private String description;
    Category(String description) {
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
