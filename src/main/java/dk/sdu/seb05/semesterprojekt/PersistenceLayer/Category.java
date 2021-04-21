package dk.sdu.seb05.semesterprojekt.PersistenceLayer;

public enum Category {

    UNKNOWN("Ukendt kategory"),
    DOCUMENTARY("Dokumentar"),
    NEWS("Nyhederne"),
    TOPICALITY("Aktualitet"),
    MAGAZINE("Magasin"),
    CULTURE_AND_NATURE("Kultur og Natur"),
    DRAMA("Drama"),
    TV_SERIES("TV-Serie"),
    ENTERTAINMENT("Underholdning"),
    MUSIC("Music"),
    KIDS("Børn"),
    REGIONAL_PROGRAMME("Regionalprogram"),
    SPORT("Sport"),
    MOVIE("Film");

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
