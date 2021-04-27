package dk.sdu.seb05.semesterprojekt.PersistenceLayer.JSONController;

import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IPerson;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IDataLayer;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class JSONPerson implements IPerson {

    private int id;
    private String name;
    private Date birthDate;
    private String description;

    public JSONPerson(int id, String name, Date birthDate, String description) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.description = description;
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        /*
        Vi putter vores JSONPerson ind i et JSONObject, så det kan blive opbevaret i en JSON fil.

        En JSONPerson ser sådanne ud, når han/hun er lavet om til et JSONObject.
        {
          "id": 1,
          "name": "Palle Pallesen",
          "birthdate": 1619128800000,
          "description": "Han boede på en gård."
        }
         */

        jsonObject.put("id", this.id);
        jsonObject.put("name", this.name);
        jsonObject.put("birthdate", this.birthDate.getTime());
        jsonObject.put("description", this.description);

        return jsonObject;
    }

    public static JSONPerson fromJSONObject(JSONObject jsonObject, IDataLayer context) throws JSONException {
        int id = jsonObject.getInt("id");
        String name = jsonObject.getString("name");

        long birthDate_ = jsonObject.getLong("birthdate");
        Date birthDate = new Date(birthDate_);

        String description = jsonObject.getString("description");

        return new JSONPerson(id, name, birthDate, description);
    }

    @Override
    public String toString() {
        LocalDate today = LocalDate.now();
        LocalDate birthDate =  getBirthdate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int age = Period.between(birthDate, today).getYears();
        return name + " ("+age+" år)";
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String newName) {
        this.name = newName;
    }

    @Override
    public Date getBirthdate() {
        return this.birthDate;
    }

    @Override
    public void setBirthDate(Date newBirthdate) {
        this.birthDate = newBirthdate;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(String newDescription) {
        this.description = newDescription;
    }
}
