package dk.sdu.seb05.semesterprojekt.PersistenceLayer;

import java.util.List;
import java.util.Arrays;
public enum FunctionType {

    ACTOR("Skuespiller"),
    PICTURE_ARTIST("Billedkunstner"),
    PICTURE_SOUND_EDIT("Billed- og lydredigering"),
    CASTING("Casting"),
    COLORGRADING("Colorgrading"),
    CONDUCTOR("Dirigent"),
    DRONE_PILOT("Dronefører"),
    DOLL_OPERATOR("Dukkefører"),
    DOLL_CREATOR("Dukkeskaber"),
    NARRATOR("Fortæller"),
    CAMERAGUY("Fotograf"),
    SOURCE_IDEA("Idé"),
    SOURCE("Forlæg"),
    GRAPHIC_DESIGN("Grafisk design"),
    SPEAKERS("Indtalere"),
    CONDUCTOR_MASTER("Kapelmester"),
    CUTTER("Klipper"),
    CONCEPT("Koncept"),
    CONSULTANT("Konsulent"),
    CHOIR("Kor"),
    CHOREOGRAPHY("Koreografi"),
    SOUND_MASTER("Lyd/Tone mester"),
    SOUND_EDIT("Lydredigering"),
    CONTRIBUTOR("Medvirkende"),
    MUSICAL_ARRANGEMENT("Musikalsk arrangement"),
    ORCHESTRA("Orkester"),
    TRANSLATOR("Oversætter"),
    PRODUCER("Tilrettelægger"),
    PRODUCTION_LEAD("Produktionsleder"),
    PROGRAM_MANAGEMENT("Programansvarlig"),
    EDITORIAL_OFFICE("Redaktion"),
    EDITOR("Redaktør"),
    REQUISITOR("Rekvisitør"),
    SET_DESIGNER("Scenograf"),
    SCRIPTER("Scripter"),
    SPECIAL_EFFECTS("Special Effects"),
    SPONSOR("Sponsor"),
    ANIMATION("Animation"),
    TEXTER("Tekster"),
    TEXT_AND_MUSIC("Tekst og musik"),
    UN_HONOURED("Uhonoreret"),
    UNKNOWN("Ukendt rolle");

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

    public static List<FunctionType> getAllFunctionTypes(){
        return Arrays.asList(FunctionType.class.getEnumConstants());
    }

}
