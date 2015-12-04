package com.example.brett.esort;

import com.parse.ParseObject;

import java.io.Serializable;
import java.sql.Array;
import java.util.List;

/**
 * Created by Brett on 11/27/2015.
 */
public class User implements Serializable {

    private String id;
    private String firstName;
    private String lastName;
    private String style;
    private List<String> traits;

    public User(ParseObject user)
    {
        id = user.getObjectId();
        firstName = user.getString("firstName");
        lastName = user.getString("lastName");
        traits = user.getList("traits");
        style = user.getString("style");
        if(style == null)
            style = "Not Set";
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

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public List<String> getTraits() {
        return traits;
    }

    public void setTraits(List<String> traits) {
        this.traits = traits;
    }
}
