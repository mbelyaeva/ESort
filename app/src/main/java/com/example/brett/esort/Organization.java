package com.example.brett.esort;

import com.parse.ParseObject;

import java.io.Serializable;

/**
 * Created by Brett on 11/27/2015.
 */
public class Organization implements Serializable {

    private String name;
    private String id;
    private int joinCode;
    private boolean sorted;

    public Organization(ParseObject org)
    {
        name = org.getString("name");
        id = org.getObjectId();
        joinCode = org.getInt("code");
        sorted = org.getBoolean("sorted");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getJoinCode() {
        return joinCode;
    }

    public void setJoinCode(int joinCode) {
        this.joinCode = joinCode;
    }

    public boolean isSorted() {
        return sorted;
    }

    public void setSorted(boolean sorted) {
        this.sorted = sorted;
    }
}
