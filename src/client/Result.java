/*
 * Student Name: Qixuan Xin
 * Student ID: 1202931
 */

package client;

import java.util.ArrayList;

public class Result {
    private int response;
    private String description;
    private ArrayList<String> containList;

    public Result(int response) {
        this.response = response;
    }

    public Result(int response, String description) {
        this.response = response;
        this.description = description;
    }

    public Result(int response, ArrayList<String> containList) {
        this.response = response;
        this.containList = containList;
    }

    public Result(int response, String description, ArrayList<String> containList) {
        this.response = response;
        this.description = description;
        this.containList = containList;
    }

    public int getResponse() {return response;}

    public String getDescription() {return description;}

    public ArrayList<String> getContainList() {return containList;}
}
