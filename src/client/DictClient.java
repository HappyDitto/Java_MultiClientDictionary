/*
 * Student Name: Qixuan Xin
 * Student ID: 1202931
 */

package client;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import static constants.Commands.*;
import static constants.StateCodes.*;

public class DictClient {
    private String address;
    private int port;
    private ClientGUI ui;

    public static void main(String[] args) {
        try {
            String address = args[0];
            int port = Integer.parseInt(args[1]);
            if (port <= 1024 || port >= 49151) {
                System.out.println("Port Number Invalid");
                System.exit(-1);
            }
            DictClient client = new DictClient(address, port);
            client.runGUI();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Parameter Invalid: Did you provide both address and port?");
        } catch (NumberFormatException e) {
            System.out.println("Port Number Invalid");
        } catch (Exception e) {
            System.out.println("Unknown Error");
            e.printStackTrace();
        }
    }

    public DictClient(String address, int port) {
        this.address = address;
        this.port = port;
        ui = new ClientGUI(this);

    }

    public Result parseResult(String message) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonResult = (JSONObject) parser.parse(message);
            int response = Integer.parseInt(jsonResult.get(RESULT).toString());
            String description = "";
            ArrayList<String> containList = new ArrayList<>();
            if (jsonResult.containsKey(DESCRIPTION)) {
                description = jsonResult.get(DESCRIPTION).toString();
            }
            if (jsonResult.containsKey(CONTAINLIST)) {
                if (jsonResult.get(CONTAINLIST) instanceof ArrayList) {
                    containList = (ArrayList<String>) jsonResult.get(CONTAINLIST);
                }
            }
            return new Result(response, description, containList);

        } catch (ParseException e) {
            return new Result(PARSE_ERROR);
        } catch (NullPointerException e) {
            return new Result(SERVER_CONNECTION_FAIL);
        }
    }

    public void runGUI() {
        ui.run();
    }

    public Result execute(String word, String desc, int command) {
        try(Socket socket = new Socket(address, port)) {
            // Output and Input Stream
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            //to send data to the server
            JSONObject data = new JSONObject();
            data.put(COMMAND, command);
            data.put(WORD, word);
            data.put(DESCRIPTION, desc);

            output.writeUTF(data.toJSONString());
            output.flush();

            //to get results from the server
            String message = input.readUTF();
            Result result = parseResult(message);

            //Disconnecting
            input.close();
            output.close();

            return result;
        } catch (IOException | JSONException e) {
            return new Result(SERVER_CONNECTION_FAIL,"");
        }
    }
}
