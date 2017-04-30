package com.example.scott.birdbud;

/**
 * Created by Scott on 4/29/2017.
 */

public class Marker {

    String name, xval, yval, date, notes;

    public Marker()
    {

    }

    public Marker(String name, String xval, String yval, String date, String notes)
    {
        this.name = name;
        this.xval = xval;
        this.yval = yval;
        this.date = date;
        this.notes = notes;
    }

    public Marker(String name, Double xval, Double yval, String date, String notes)
    {
        this.name = name;
        this.xval = String.valueOf(xval);
        this.yval = String.valueOf(yval);
        this.date = date;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public String getXval() {
        return xval;
    }

    public String getYval() {
        return yval;
    }

    public String getDate() {
        return date;
    }

    public String getNotes() {
        return notes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setXval(String xval) {
        this.xval = xval;
    }

    public void setYval(String yval) {
        this.yval = yval;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
