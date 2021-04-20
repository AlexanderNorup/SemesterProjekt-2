package dk.sdu.seb05.semesterprojekt.PersistenceLayer.JSONController;

import dk.sdu.seb05.semesterprojekt.PersistenceLayer.*;

import java.io.*;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JSONController implements IDataLayer {

    private List<IPerson> persons;
    private List<ICredit> credits;
    private List<IProducer> producers;
    private List<IProgramme> programmes;

    private int nextPersonId;
    private int nextCreditId;
    private int nextProducerId;
    private int nextProgrammeId;

    private File file;

    public JSONController() {
        persons = new ArrayList<>();
        credits = new ArrayList<>();
        producers = new ArrayList<>();
        programmes = new ArrayList<>();

        nextPersonId = 0;
        nextCreditId = 0;
        nextProducerId = 0;
        nextProgrammeId = 0;

        file = new File("data.json");
        try {
            JSONObject jsonObject = loadFile();
            populateData(jsonObject);
        } catch (JSONException exception) {
            System.out.println("Der skete en fejl med at indlæse JSON: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    private synchronized JSONObject loadFile() throws JSONException {
        try {
            if (file.exists()) {
                Scanner reader = new Scanner(file);
                StringBuilder stringReader = new StringBuilder();
                while (reader.hasNextLine()) {
                    stringReader.append(reader.nextLine()).append("\n");
                }
                reader.close();
                if (stringReader.toString().trim().isEmpty()) {
                    //Hvis filen vi indlæser er tom, så bare lav et nyt JSONObject.
                    return new JSONObject();
                } else {
                    return new JSONObject(stringReader.toString());
                }
            } else {
                file.createNewFile();
                return new JSONObject();
            }
        } catch (IOException e) {
            System.out.println("Der skete en fejl med at indlæse filen: " + e.getMessage());
        }
        return new JSONObject();
    }

    private synchronized void saveFile() throws JSONException {
        //TODO: Skriv jsonObject til fil.
        //Først fjerner vi alle credits som ikke længere findes.
        cleanCredits();

        JSONObject jsonObject = new JSONObject();

        JSONArray personArray = new JSONArray();
        for (IPerson person : persons) {
            if (person instanceof JSONPerson) {
                JSONPerson jsonPerson = (JSONPerson) person;
                personArray.put(jsonPerson.toJSONObject());
            }
        }
        jsonObject.put("persons", personArray);

        JSONArray creditArray = new JSONArray();
        for (ICredit credit : credits) {
            if (credit instanceof JSONCredit) {
                JSONCredit jsonCredit = (JSONCredit) credit;
                creditArray.put(jsonCredit.toJSONObject());
            }
        }
        jsonObject.put("credits", creditArray);


        JSONArray producerArray = new JSONArray();
        for (IProducer producer : producers) {
            if (producer instanceof JSONProducer) {
                JSONProducer jsonProducer = (JSONProducer) producer;
                producerArray.put(jsonProducer.toJSONObject());
            }
        }
        jsonObject.put("producer", producerArray);

        JSONArray programmeArray = new JSONArray();
        for (IProgramme programme : programmes) {
            if (programme instanceof JSONProgramme) {
                JSONProgramme jsonProgramme = (JSONProgramme) programme;
                producerArray.put(jsonProgramme.toJSONObject());
            }
        }
        jsonObject.put("programmes", programmeArray);

        String finalJson = jsonObject.toString();

        //Now we just write the file
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(file.getPath(), false))) {
            writer.println(finalJson);
            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Filen er blevet gemt!");
    }

    private void populateData(JSONObject jsonObject) throws JSONException {
        //Tager den indlæste JSONData og putter den ind i listerne

        int highestPersonId = -1;
        //Vi starter med at indlæse alle personerne.
        if (jsonObject.has("persons")) {
            JSONArray personsArray = jsonObject.getJSONArray("persons");
            for (int i = 0; i < personsArray.length(); i++) {
                if (personsArray.get(i) instanceof JSONObject) {
                    JSONPerson person = JSONPerson.fromJSONObject((JSONObject) personsArray.get(i), this);
                    if (person.getId() > highestPersonId) {
                        highestPersonId = person.getId();
                    }
                    this.persons.add(person);
                }
            }
        }
        nextPersonId = highestPersonId + 1;

        int highestCreditId = -1;
        //Vi kan nu indlæse alle credits
        if (jsonObject.has("credits")) {
            JSONArray creditsArray = jsonObject.getJSONArray("credits");
            for (int i = 0; i < creditsArray.length(); i++) {
                if (creditsArray.get(i) instanceof JSONObject) {
                    JSONCredit credit = JSONCredit.fromJSONObject((JSONObject) creditsArray.get(i), this);
                    if (credit.getId() > highestCreditId) {
                        highestCreditId = credit.getId();
                    }
                    this.credits.add(credit);
                }
            }
        }
        nextCreditId = highestCreditId + 1;

        int highestProducerId = -1;
        //Vi kan nu indlæse alle producers
        if (jsonObject.has("producers")) {
            JSONArray producerArray = jsonObject.getJSONArray("producers");
            for (int i = 0; i < producerArray.length(); i++) {
                if (producerArray.get(i) instanceof JSONObject) {
                    JSONProducer producer = JSONProducer.fromJSONObject((JSONObject) producerArray.get(i));
                    if (producer.getId() > highestProducerId) {
                        highestProducerId = producer.getId();
                    }
                    this.producers.add(producer);
                }
            }
        }
        nextProducerId = highestProducerId + 1;

        int highestProgrammeId = -1;
        //Så læser vi programmerne ind.
        if (jsonObject.has("programmes")) {
            JSONArray programmeArray = jsonObject.getJSONArray("programmes");
            for (int i = 0; i < programmeArray.length(); i++) {
                if (programmeArray.get(i) instanceof JSONObject) {
                    JSONProgramme programme = JSONProgramme.fromJSONObject((JSONObject) programmeArray.get(i), this);
                    if (programme.getId() > highestProgrammeId) {
                        highestProgrammeId = programme.getId();
                    }
                    this.programmes.add(programme);
                }
            }
        }
        nextProgrammeId = highestProgrammeId + 1;


        // ----
        // NU er alle dataene læst ind. Vi kan vi opresætte referencerne.


        for (IProgramme programme : programmes) {
            for (IProducer producer : programme.getProducers()) {
                producer.addProgramme(programme);
            }
        }

    }

    public static void main(String[] args) {
        IDataLayer dataLayer = new JSONController();
        System.out.println("Alle personer i data systemet:");
        for (IPerson person : dataLayer.getPersons()) {
            System.out.println(" - " + person.getName() + " med beskrivelse: " + person.getDescription());
        }

        /*
        try {
            ((JSONController) dataLayer).saveFile();
        } catch (JSONException e) {
            System.out.println("Det gik lort med at gemme filen: " + e.getMessage());
            e.printStackTrace();
        }/**/
    }

    @Override
    public List<IPerson> getPersons() {
        return this.persons;
    }

    @Override
    public List<IProgramme> getProgrammes() {
        return this.programmes;
    }

    @Override
    public List<IProducer> getProducers() {
        return this.producers;
    }

    public List<ICredit> getCredits() {
        return this.credits;
    }

    @Override
    public IProgramme getProgram(int programId) {
        for (IProgramme programme : programmes) {
            if (programme.getId() == programId) {
                return programme;
            }
        }
        return null;
    }

    @Override
    public IProducer getProducer(int producerId) {
        for (IProducer producer : producers) {
            if (producer.getId() == producerId) {
                return producer;
            }
        }
        return null;
    }

    @Override
    public IPerson getPerson(int personId) {
        for (IPerson person : persons) {
            if (person.getId() == personId) {
                return person;
            }
        }
        return null;
    }

    @Override
    public ICredit getCredit(int creditId) {
        for (ICredit credit : credits) {
            if (credit.getId() == creditId) {
                return credit;
            }
        }
        return null;
    }

    @Override
    public boolean updateProgramme(IProgramme iProgramme) {
        this.deleteProgramme(iProgramme);
        this.programmes.add(iProgramme);
        return true;
    }

    @Override
    public boolean updatePerson(IPerson iPerson) {
        this.deletePerson(iPerson);
        this.persons.add(iPerson);
        return true;
    }

    @Override
    public boolean updateProducer(IProducer iProducer) {
        this.deleteProducer(iProducer);
        this.producers.add(iProducer);
        return true;
    }

    @Override
    public boolean updateCredit(ICredit iCredit) {
        this.deleteCredit(iCredit);
        this.credits.add(iCredit);
        return true;
    }

    @Override
    public int createProgramme(String name, Category category, String channel, Date airedDate, List<ICredit> credits, List<IProducer> producers) {
        IProgramme programme = new JSONProgramme(nextProgrammeId, name, category, channel, airedDate, producers, credits);
        this.programmes.add(programme);
        return nextProgrammeId++;
    }

    @Override
    public int createPerson(String name, Date birthdate, String description) {
        IPerson person = new JSONPerson(nextPersonId, name, birthdate, description);
        this.persons.add(person);
        return nextPersonId++;
    }

    @Override
    public int createProducer(String company, List<IProgramme> programmes) {
        IProducer producer = new JSONProducer(nextProducerId, company, programmes);
        this.producers.add(producer);
        return nextProducerId++;
    }

    @Override
    public int createCredit(IPerson person, FunctionType functionType) {
        ICredit credit = new JSONCredit(nextCreditId, person, functionType);
        this.credits.add(credit);
        return nextCreditId++;
    }

    @Override
    public boolean deleteProgramme(IProgramme iProgramme) {
        IProgramme toRemove = getProgram(iProgramme.getId());
        return programmes.remove(toRemove);
    }

    @Override
    public boolean deletePerson(IPerson iPerson) {
        IPerson toRemove = getPerson(iPerson.getId());
        return persons.remove(toRemove);
    }

    @Override
    public boolean deleteProducer(IProducer iProducer) {
        IProducer toRemove = getProducer(iProducer.getId());
        return producers.remove(toRemove);
    }

    @Override
    public boolean deleteCredit(ICredit iCredit) {
        ICredit toRemove = getCredit(iCredit.getId());
        return credits.remove(toRemove);
    }

    private void cleanCredits() {
        //First remove all credits that reference people that no longer exists.
        List<ICredit> toRemove = new ArrayList<>();
        for (ICredit credit : credits) {
            if (getPerson(credit.getPerson().getId()) == null) {
                //This person has been deleted
                toRemove.add(credit);
                continue;
            }
        }
        this.credits.removeAll(toRemove);

        //The remove all credits which are no longer referenced from programmes.
        List<ICredit> unusedCredits = new ArrayList(credits);
        for (IProgramme programme : programmes) {
            for (ICredit credit : programme.getCredits()) {
                if (credits.contains(credit)) {
                    unusedCredits.remove(credit);
                }
            }
        }
        this.credits.removeAll(unusedCredits);
    }

    @Override
    public String exportData() {
        return "IKKE IMPLEMENTERET ENDNU";
    }

    @Override
    public String exportData(int producerId) {
        return "IKKE IMPLEMENTERET ENDNU";
    }

    @Override
    public String getStatistics() {
        return "IKKE IMPLEMENTERET ENDNU";
    }

    @Override
    public String getStatistics(int producerId) {
        return "IKKE IMPLEMENTERET ENDNU";
    }

    @Override
    public List<IProgramme> searchForProgramme(String query) {
        List<IProgramme> topResults = new ArrayList<>();
        List<IProgramme> programResults = new ArrayList<>();
        List<IProgramme> actorResults = new ArrayList<>();
        List<IProgramme> producerResults = new ArrayList<>();

        for (IProgramme programme : programmes) {
            if (programme.getName().startsWith(query)) {
                topResults.add(programme);
                continue;
            }
            if (programme.getName().contains(query)) {
                programResults.add(programme);
                continue;
            }
            for (ICredit credit : programme.getCredits()) {
                if (credit.getPerson().getName().contains(query)) {
                    actorResults.add(programme);
                    continue;
                }
            }
            for (IProducer producer : programme.getProducers()) {
                if (producer.getCompany().contains(query)) {
                    producerResults.add(programme);
                    continue;
                }
            }
        }

        programResults.addAll(actorResults);
        programResults.addAll(producerResults);
        topResults.addAll(programResults);
        return topResults;
    }

    @Override
    public List<IPerson> searchForPerson(String query) {
        List<IPerson> topResults = new ArrayList<>();
        List<IPerson> personResults = new ArrayList<>();
        List<IPerson> programResults = new ArrayList<>();

        for (IPerson person : persons) {
            if (person.getName().startsWith(query)) {
                topResults.add(person);
                continue;
            }
            if (person.getName().contains(query)) {
                personResults.add(person);
                continue;
            }
            for (IProgramme programme : getProgrammesForPerson(person.getId())) {
                if (programme.getName().contains(query)) {
                    programResults.add(person);
                }
            }
        }

        personResults.addAll(programResults);
        topResults.addAll(personResults);
        return topResults;
    }

    @Override
    public List<IProducer> searchForProducer(String query) {
        List<IProducer> topResults = new ArrayList<>();
        List<IProducer> producerResults = new ArrayList<>();
        List<IProducer> programmeResults = new ArrayList<>();

        for (IProducer producer : producers) {
            if (producer.getCompany().startsWith(query)) {
                topResults.add(producer);
                continue;
            }
            if (producer.getCompany().contains(query)) {
                producerResults.add(producer);
                continue;
            }
            for (IProgramme programme : producer.getProgrammes()) {
                if (programme.getName().contains(query)) {
                    programmeResults.add(producer);
                }
            }
        }

        producerResults.addAll(programmeResults);
        topResults.addAll(producerResults);
        return topResults;
    }

    @Override
    public List<IProgramme> getProgrammesForPerson(int personId) {
        List<IProgramme> toReturn = new ArrayList<>();
        for (IProgramme programme : programmes) {
            for (ICredit credit : programme.getCredits()) {
                if (credit.getPerson().getId() == personId) {
                    toReturn.add(programme);
                    break;
                }
            }
        }

        return toReturn;
    }

    @Override
    public List<ICredit> getCreditsForPerson(int personId) {
        List<ICredit> toReturn = new ArrayList<>();
        for (ICredit credit : credits) {
            if (credit.getPerson().getId() == personId) {
                toReturn.add(credit);
            }
        }
        return toReturn;
    }

    @Override
    public List<IProgramme> getLatestProgrammes() {
        List<IProgramme> latestProgrammes = new ArrayList<>(programmes);

        Collections.sort(latestProgrammes, new ProgrammeDateComparator());

        return latestProgrammes;
    }

    @Override
    public List<String> getNotifications(int producerId) {
        return new ArrayList<String>();
    }

    @Override
    public void logMessage(String message) {
        System.out.println("[Temp-Log] " + message);
    }
}