/*
 * Student Name: Qixuan Xin
 * Student ID: 1202931
 */

package server;

import static constants.Commands.*;

public class Request {
    private String address;
    private String command;
    private String word;
    private int result;

    public Request (String address, int command, String word, int result) {
        this.address = address;
        switch (command) {
            case QUERY -> this.command = "Query";
            case ADD -> this.command = "Add";
            case UPDATE -> this.command = "Update";
            case DELETE -> this.command = "Delete";
        }
        this.word = word;
        this.result = result;
    }

    public String getAddress() {
        return address;
    }

    public String getCommand() {
        return command;
    }

    public String getWord() {
        return word;
    }

    public int getResult() {
        return result;
    }
}
