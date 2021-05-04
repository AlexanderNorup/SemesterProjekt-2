package dk.sdu.seb05.semesterprojekt.PersistenceLayer.DatabaseController;

public enum DatabaseState {
    BRAND_NEW, //Brand new: Needs to be "inserted"
    CLEAN, //Clean from the databaase. Dosn't need to be updated
    DIRTY, //Has been touched. Needs to be updated
    TRASH //Needs to be deleted.
}
