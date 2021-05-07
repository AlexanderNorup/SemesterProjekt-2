package dk.sdu.seb05.semesterprojekt.PersistenceLayer.JSONController;

import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IPerson;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProducer;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProgramme;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class JSONProducer implements IProducer {

    private int id;
    private String company;

    public JSONProducer(int id, String company) {
        this.id = id;
        this.company = company;
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", this.id);
        jsonObject.put("company", this.company);

        return jsonObject;
    }

    public static JSONProducer fromJSONObject(JSONObject jsonObject) throws JSONException {
        int id = jsonObject.getInt("id");
        String company = jsonObject.getString("company");
        //  List< > programmeList = jsonObject.getString("programmeList");

        return new JSONProducer(id, company);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JSONProducer){
            return ((JSONProducer) obj).getId() == this.getId();
        }
        return false;
    }

    @Override
    public String toString() {
            return company;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getCompany() {
        return this.company;
    }

    @Override
    public void setCompany(String newCompany) {
        this.company = newCompany;
    }
}
