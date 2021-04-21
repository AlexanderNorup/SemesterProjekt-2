package dk.sdu.seb05.semesterprojekt.PersistenceLayer.JSONController;

import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProgramme;

import java.util.Comparator;

public class ProgrammeDateComparator implements Comparator<IProgramme> {
    @Override
    public int compare(IProgramme o1, IProgramme o2) {
        return o2.getAiredDate().compareTo(o1.getAiredDate());
    }
}
