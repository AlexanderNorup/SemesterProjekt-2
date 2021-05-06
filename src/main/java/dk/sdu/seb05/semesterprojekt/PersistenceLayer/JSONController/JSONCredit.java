package dk.sdu.seb05.semesterprojekt.PersistenceLayer.JSONController;

import dk.sdu.seb05.semesterprojekt.PersistenceLayer.FunctionType;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.ICredit;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IPerson;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IDataLayer;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONCredit implements ICredit {

    private int id;
    private IPerson person;
    private FunctionType functionType;

    public JSONCredit(int id, IPerson person, FunctionType functionType) {
        this.id = id;
        this.person = person;
        this.functionType = functionType;
    }

    public static JSONCredit fromJSONObject(JSONObject jsonObject, IDataLayer context) throws JSONException {
        int id = jsonObject.getInt("id");
        int personId = jsonObject.getInt("person");
        IPerson person = context.getPerson(personId);
        String functionType_ = jsonObject.getString("functionType");
        FunctionType functionType;
        //Fordi FunctionType er en enum, så skal den gemmes som en String. Man kan bruge
        //FunctionType.valueOf() til at konvertere en string tilbage til en enum. Men den kaster en IllegalArgumentException hvis det går galt.
        try {
            functionType = FunctionType.valueOf(functionType_);
        } catch (IllegalArgumentException e) {
            functionType = FunctionType.UNKNOWN;
        }
        return new JSONCredit(id, person, functionType);
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        /*
        Således ser et Credit JSONObject ud.
        {
          "id": 1,
          "person": 2,
          "functionType": "ACTOR"
        }
         */
        jsonObject.put("id", this.id);
        jsonObject.put("person", this.person.getId());
        jsonObject.put("functionType", this.functionType.name());

        return jsonObject;
    }

    @Override
    public String toString() {
        return getPerson().toString() + " - " + functionType;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public IPerson getPerson() {
        return this.person;
    }

    @Override
    public void setPerson(IPerson newPerson) {
        this.person = newPerson;
    }

    @Override
    public FunctionType getFunctionType() {
        return this.functionType;
    }

    @Override
    public void setFunctionType(FunctionType newFunction) {
        this.functionType = newFunction;
    }
}
