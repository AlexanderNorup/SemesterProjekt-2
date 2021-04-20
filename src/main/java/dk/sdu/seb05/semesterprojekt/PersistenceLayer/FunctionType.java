package dk.sdu.seb05.semesterprojekt.PersistenceLayer;

import java.util.List;
import java.util.Arrays;
public enum FunctionType {

    UNKNOWN("Ukendt rolle"),
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

    public static List<FunctionType> getAllFunctionTypes(){
        return Arrays.asList(FunctionType.class.getEnumConstants());
    }

}
