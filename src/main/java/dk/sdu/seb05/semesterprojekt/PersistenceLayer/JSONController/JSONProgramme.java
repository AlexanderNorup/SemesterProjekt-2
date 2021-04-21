package dk.sdu.seb05.semesterprojekt.PersistenceLayer.JSONController;

import dk.sdu.seb05.semesterprojekt.PersistenceLayer.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class JSONProgramme implements IProgramme {

    private int id;
    private String name;
    private Category category;
    private String channel;
    private Date airedDate;
    private List<IProducer> producers;
    private List<ICredit> credits;

    // ListProducers og ListCredits

    public JSONProgramme (int id, String name, Category category, String channel, Date airedDate, List<IProducer> producers, List<ICredit> credits){
        this.id = id;
        this.name = name;
        this.category = category;
        this.channel = channel;
        this.airedDate = airedDate;
        this.producers = producers;
        this.credits = credits;
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", this.id);
        jsonObject.put("name", this.name);
        jsonObject.put("category", this.category.name());
        jsonObject.put("channel", this.channel);
        jsonObject.put("sendDate", this.airedDate.getTime());

        JSONArray producersArray = new JSONArray();
        for(IProducer producer : producers){
            producersArray.put(producer.getId());
        }

        JSONArray creditsArray = new JSONArray();
        for(ICredit credit : credits){
            creditsArray.put(credit.getId());
        }

        jsonObject.put("producers", producersArray);
        jsonObject.put("credits", creditsArray);

        return jsonObject;
    }

    public static JSONProgramme fromJSONObject(JSONObject jsonObject, IDataLayer context) throws JSONException{
        int id = jsonObject.getInt("id");
        String name = jsonObject.getString("name");

        String category_ = jsonObject.getString("category");
        Category category;
        //Fordi FunctionType er en enum, så skal den gemmes som en String. Man kan bruge
        // FunctionType.valueOf() til at konvertere en string tilbage til en enum. Men den kaster en IllegalArgumentException hvis det går galt.
        try {
            category = Category.valueOf(category_);
        } catch (IllegalArgumentException e) {
            category = Category.UNKNOWN;
        }

        String channel = jsonObject.getString("channel");
        long sendDate_ = jsonObject.getLong("sendDate");
        Date sendDate = new Date(sendDate_);

        JSONArray producerArray = jsonObject.getJSONArray("producers");
        List<IProducer> producers = new ArrayList<>();
        for(int i = 0; i < producerArray.length(); i++){
            int producerId = producerArray.getInt(i);
            IProducer producer = context.getProducer(producerId);
            if(producer == null){
                continue;
            }
            producers.add(producer);
        }

        JSONArray creditArray = jsonObject.getJSONArray("credits");
        List<ICredit> credits = new ArrayList<>();
        for(int i = 0; i < creditArray.length(); i++){
            int creditId = creditArray.getInt(i);
            ICredit credit = context.getCredit(creditId);
            if(credit == null){
                continue;
            }
            credits.add(credit);
        }

        return new JSONProgramme(id, name, category, channel, sendDate, producers, credits);
    }

    @Override
    public String toString() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(airedDate);//cal.get(Calendar.YEAR)
        String date = cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.YEAR);
        return "(" + date + ") " + getName() + " - " + getChannel();
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
    public Category getCategory() {
        return this.category;
    }

    @Override
    public void setCategory(Category newCategory) {
        this.category = newCategory;
    }

    @Override
    public String getChannel() {
        return this.channel;
    }

    @Override
    public void setChannel(String newChannel) {
        this.channel = newChannel;
    }

    @Override
    public Date getAiredDate() {
        return this.airedDate;
    }

    @Override
    public void setAiredDate(Date newDate) {
        this.airedDate = newDate;
    }

    @Override
    public List<ICredit> getCredits() {
        return this.credits;
    }

    @Override
    public void addCredit(ICredit credit) {
        this.credits.add(credit);
    }

    @Override
    public void removeCredit(ICredit credit) {
        this.credits.remove(credit);
    }

    @Override
    public List<IProducer> getProducers() {
        return this.producers;
    }

    @Override
    public void addProducer(IProducer producer) {
        this.producers.add(producer);
    }

    @Override
    public void removeProducer(IProducer producer) {
        this.producers.remove(producer);
    }
}
