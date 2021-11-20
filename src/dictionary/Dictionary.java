/*
 * Student Name: Qixuan Xin
 * Student ID: 1202931
 */

package dictionary;
import java.io.*;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import static constants.StateCodes.*;


public class Dictionary {
    private final String filePath;
    private JSONArray dict;

    public Dictionary(String path) {
        filePath = path;
        updateDict();
    }

    public void updateDict() {
        JSONParser parser = new JSONParser();
        try {
            dict = (JSONArray) parser.parse(new FileReader(filePath));
        } catch (IOException | ParseException e) {
            System.out.println("Cannot read dictionary file");
            e.printStackTrace();
        }
    }

    public ArrayList<String> contain(String word) {
        ArrayList<String> containArray = new ArrayList<>();
        for (Object o : dict) {
            JSONObject jsonWord = (JSONObject) o;
            if (jsonWord.get("word").toString().contains(word)) {
                String tgtWord = jsonWord.get("word").toString();
                containArray.add(tgtWord);
            }
        }
        return containArray;
    }

    public synchronized int add(String word, String desc) {
        if (query(word)[0].equals(CRUD_SUCC + ""))
            return CRUD_FAIL;

        JSONObject jsonWord = new JSONObject();
        jsonWord.put("word", word);
        jsonWord.put("description", desc);
        dict.add(jsonWord);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(dict.toJSONString());
            writer.close();
            updateDict();
        } catch (IOException e) {
            return IO_ERROR;
        }

        return CRUD_SUCC;
    }

    public String[] query(String word) {
        for (Object o : dict) {
            JSONObject jsonWord = (JSONObject) o;
            if (jsonWord.get("word").equals(word)) {
                String desc = (String) jsonWord.get("description");
                return new String[]{CRUD_SUCC+"", desc, dict.indexOf(o)+""};
            }
        }

        return new String[]{CRUD_FAIL+"", ""};
    }

    public synchronized int update(String word, String desc) throws IOException {
        String[] result = query(word);
        if (result[0].equals(CRUD_FAIL + ""))
            return CRUD_FAIL;

        dict.remove(Integer.parseInt(result[2]));
        JSONObject jsonWord = new JSONObject();
        jsonWord.put("word", word);
        jsonWord.put("description", desc);
        dict.add(Integer.parseInt(result[2]), jsonWord);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(dict.toJSONString());
            writer.close();
            updateDict();
        } catch (IOException e) {
            return IO_ERROR;
        }
        return CRUD_SUCC;
    }

    public synchronized int remove(String word) throws IOException {
        String[] result = query(word);
        if (result[0].equals(CRUD_FAIL + ""))
            return CRUD_FAIL;

        dict.remove(Integer.parseInt(result[2]));
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(dict.toJSONString());
            writer.close();
            updateDict();
        } catch (IOException e) {
            return IO_ERROR;
        }
        return CRUD_SUCC;
    }
}
