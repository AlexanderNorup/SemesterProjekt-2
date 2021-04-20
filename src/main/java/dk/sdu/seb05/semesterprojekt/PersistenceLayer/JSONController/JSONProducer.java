package dk.sdu.seb05.semesterprojekt.PersistenceLayer.JSONController;

import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IPerson;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProducer;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProgramme;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONProducer implements IProducer {

    private int id;
    private String company;
    private List<IProgramme> programmeList;

    public JSONProducer(int id, String company, List<IProgramme> programmeList) {
        this.id = id;
        this.company = company;
        this.programmeList = programmeList;
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

        return new JSONProducer(id, company, new ArrayList<IProgramme>());
    }

    //TODO: ID

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

    @Override
    public List<IProgramme> getProgrammes() {
        return this.programmeList;
    }
}
