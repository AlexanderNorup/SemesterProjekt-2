package dk.sdu.seb05.semesterprojekt.PersistenceLayer.DatabaseController;

public class CachedDatabaseObject implements Comparable<CachedDatabaseObject>{

    private long expiresAt;
    private DatabaseObject object;

    public CachedDatabaseObject(DatabaseObject object){
        this.object = object;
        expiresAt = System.currentTimeMillis() + DatabaseController.DEFAULT_CACHE_INVALIDATION_TIME; // Expires after invalidation-time
    }

    public DatabaseObject getObject(){
        return this.object;
    }

    public boolean isExpired(){
        if(object.getState() == DatabaseState.BRAND_NEW) {
            return false;
        }
        return System.currentTimeMillis() > expiresAt;
    }

    @Override
    public int compareTo(CachedDatabaseObject o) {
        return this.object.getId() - o.object.getId();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DatabaseObject){
            if(((DatabaseObject) obj).getId() == this.object.getId() //Hvis de har samme id
                    && obj.getClass().equals(this.object.getClass())){ //OG hvis de har samme class
                return true;                                            //Så er de ens
            }
        }
        return false;
    }
}
