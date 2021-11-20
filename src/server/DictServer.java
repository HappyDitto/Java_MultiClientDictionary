/*
 * Student Name: Qixuan Xin
 * Student ID: 1202931
 */

package server;

import dictionary.Dictionary;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ServerSocketFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static constants.Commands.*;
import static constants.StateCodes.*;

public class DictServer {
    private static Dictionary dictionary;
    private static int port;
    private static int startFlag = 1;
    private static ArrayList<Request> requests;
    private static ServerGUI ui;

    public static void main(String[] args) {
        try {
            int port = Integer.parseInt(args[0]);
            String path = args[1];
            if (port <= 1024 || port >= 49151) {
                System.out.println("Port Number Invalid");
                System.exit(-1);
            }
            if (!new File(path).exists()) {
                System.out.println("Cannot find dict.json");
                System.exit(-1);
            }
            DictServer server = new DictServer(path, port);
            ui.run();
            server.run();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Parameter Invalid: Did you provide both address and port?");
        } catch (NumberFormatException e) {
            System.out.println("Port Number Invalid");
        } catch (Exception e) {
            System.out.println("Unknown Error");
            e.printStackTrace();
        }
    }

    public DictServer(String path, int portNum) {
        port = portNum;
        dictionary = new Dictionary(path);
        ui = new ServerGUI(this);
        requests = new ArrayList<>();
    }

    public void setStartFlag(int flag) {
        startFlag = flag;
    }

    public void run() {

        ServerSocketFactory factory = ServerSocketFactory.getDefault();

        try(ServerSocket server = factory.createServerSocket(port))
        {
            // Wait for connections.
            while (true) {
                Socket request = server.accept();
                // Start a new thread for a connection
                Thread t = new Thread(() -> handleRequest(request));
                t.start();
            }
        }
        catch (IOException e)
        {
            System.out.println("Error on creating server socket");
            e.printStackTrace();
        }
        catch (Exception e) {
            System.out.println("Unknown Error");
            e.printStackTrace();
        }
    }

    public static JSONObject parseRequest(String message) {
        JSONParser parser = new JSONParser();
        try {
            return (JSONObject) parser.parse(message);
        } catch (ParseException e) {
            System.out.println("Cannot parse request");
        }
        return null;
    }

    private static void handleRequest(Socket client)
    {
        try(Socket clientSocket = client)
        {
            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());

            if (startFlag == 0) {
                JSONObject response = new JSONObject();
                response.put(RESULT,SERVER_CONNECTION_FAIL);
                output.writeUTF(response.toJSONString());
                output.flush();

                input.close();
                output.close();
                return;
            }

            String message = input.readUTF();
            JSONObject request = parseRequest(message);
            if (request == null) {
                JSONObject response = new JSONObject();
                response.put(RESULT,SERVER_CONNECTION_FAIL);
                output.writeUTF(response.toJSONString());
                output.flush();

                input.close();
                output.close();
                return;
            }

            int command = Integer.parseInt(request.get(COMMAND).toString());
            String word = request.get(WORD).toString().toLowerCase();
            String description = request.get(DESCRIPTION).toString();

            JSONObject response = new JSONObject();
            switch (command) {
                case QUERY -> {
                    String[] queryResult = dictionary.query(word);
                    if (queryResult[0].equals(CRUD_FAIL + "")) {
                        response.put(RESULT, WORD_NOT_FOUND);
                    } else {
                        response.put(RESULT, SUCC);
                        response.put(DESCRIPTION, queryResult[1]);
                    }
                }
                case ADD -> {
                    int addResult = dictionary.add(word, description);
                    if (addResult == CRUD_FAIL) {
                        response.put(RESULT, WORD_ALREADY_EXIST);
                    } else if (addResult == IO_ERROR) {
                        response.put(RESULT, IO_ERROR);
                    } else {
                        response.put(RESULT, SUCC);
                    }
                }
                case UPDATE -> {
                    int updateResult = dictionary.update(word, description);
                    if (updateResult == CRUD_FAIL) {
                        response.put(RESULT, WORD_NOT_FOUND);
                    } else if (updateResult == IO_ERROR) {
                        response.put(RESULT, IO_ERROR);
                    } else {
                        response.put(RESULT, SUCC);
                        response.put(DESCRIPTION, description);
                    }
                }
                case DELETE -> {
                    int deleteResult = dictionary.remove(word);
                    if (deleteResult == CRUD_FAIL) {
                        response.put(RESULT, WORD_NOT_FOUND);
                    } else if (deleteResult == IO_ERROR) {
                        response.put(RESULT, IO_ERROR);
                    } else {
                        response.put(RESULT, SUCC);
                    }
                }
                case CONTAIN -> {
                    ArrayList<String> result = dictionary.contain(word);
                    response.put(RESULT, SUCC);
                    response.put(CONTAINLIST, result);
                }
            }

            if (command != CONTAIN) {
                InetSocketAddress socketAddress = (InetSocketAddress) client.getRemoteSocketAddress();
                String address = socketAddress.getAddress().toString().substring(1);
                Request cell = new Request(address, command, word, Integer.parseInt(response.get(RESULT).toString()));
                requests.add(cell);
                ui.updateList(requests.toArray(Request[]::new));
            }

            output.writeUTF(response.toJSONString());
            output.flush();

            input.close();
            output.close();
        }
        catch (IOException e)
        {
            System.out.println("Error on read/write buffer");
            e.printStackTrace();
        }
    }
}
