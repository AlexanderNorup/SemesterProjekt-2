package dk.sdu.seb05.semesterprojekt.PersistenceLayer.JSONController;

import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IDataLayer;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IPerson;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProducer;
import dk.sdu.seb05.semesterprojekt.PersistenceLayer.IProgramme;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import org.json.JSONException;
import org.json.JSONObject;


public class JSONController implements IDataLayer {

    private JSONObject jsonObject;
    private File file;

    public JSONController(){
        file = new File("data.json");
        loadFile();
    }

    private void loadFile(){
        try {
            if (file.exists()) {
                Scanner reader = new Scanner(file);
                StringBuilder stringReader = new StringBuilder();
                while(reader.hasNextLine()){
                    stringReader.append(reader.nextLine()).append("\n");
                }
                reader.close();
                jsonObject = new JSONObject(stringReader.toString());
            } else {
                file.createNewFile();
                jsonObject = new JSONObject();
            }
        }catch(IOException e){
            System.out.println("Der skete en fejl med at indlæse filen: " + e.getMessage());
            jsonObject = new JSONObject();
        }catch(JSONException e){
            System.out.println("Der skete en fejl med at indlæse JSON dataen: " + e.getMessage());
            jsonObject = new JSONObject();
        }
    }

    @Override
    public List<IPerson> getPersons() {
        return null;
    }

    @Override
    public List<IProgramme> getProgrammes() {
        return null;
    }

    @Override
    public List<IProducer> getProducers() {
        return null;
    }

    @Override
    public boolean updateProgramme(IProgramme iProgramme) {
        return false;
    }

    @Override
    public boolean updatePersons(IPerson iPerson) {
        return false;
    }

    @Override
    public boolean updateProducers(IProducer iProducer) {
        return false;
    }

    @Override
    public boolean createProgramme(IProgramme iProgramme) {
        return false;
    }

    @Override
    public boolean createPerson(IPerson iPerson) {
        return false;
    }

    @Override
    public boolean createProducer(IProducer iProducer) {
        return false;
    }

    @Override
    public boolean deleteProgramme(IProgramme iProgramme) {
        return false;
    }

    @Override
    public boolean deletePerson(IPerson iPerson) {
        return false;
    }

    @Override
    public boolean deleteProducer(IProducer iProducer) {
        return false;
    }

    @Override
    public String exportData() {
        return null;
    }

    @Override
    public String exportData(int producerId) {
        return null;
    }

    @Override
    public String getStatistics() {
        return null;
    }

    @Override
    public String getStatistics(int producerId) {
        return null;
    }

    @Override
    public List<IProgramme> searchForProgramme(String query) {
        return null;
    }

    @Override
    public List<IPerson> searchForPerson(String query) {
        return null;
    }

    @Override
    public List<IProducer> searchForProducer(String query) {
        return null;
    }

    @Override
    public List<IProgramme> getLatestProgrammes() {
        return null;
    }

    @Override
    public List<String> getNotifications(int producerId) {
        return null;
    }

    @Override
    public void logMessage(String message) {

    }
}
