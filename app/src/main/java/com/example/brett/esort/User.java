package com.example.brett.esort;

import com.parse.ParseObject;

import java.io.Serializable;

/**
 * Created by Brett on 11/27/2015.
 */
public class User implements Serializable {

    private String id;
    private String firstName;
    private String lastName;

    public User(ParseObject user)
    {
        id = user.getObjectId();
        firstName = user.getString("firstName");
        lastName = user.getString("lastName");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
