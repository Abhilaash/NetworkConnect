package com.Velamati.Abhilaash.networkconnect;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Abhilaash on 8/14/2014.
 */
public class Notam {
    private String eventid;
    private String notamnumber;



    private String aclass;
    private String startdate;
    private String enddate;
    private String notamtext;
    private String affectedfeature;
    private String geom;

    public Notam(String Eventid, String Notamnumber, String Class, String Startdate, String Enddate, String Notamtext, String Affectedfeature, String Geom, String url){
        eventid = Eventid;
        notamnumber = Notamnumber;
        aclass = Class;
        startdate = Startdate.substring(4, 6) + "/" + Startdate.substring(2, 4) + "/" + Startdate.substring(0, 2) + Startdate.substring(6, 8) + ":" + Startdate.substring(8);
        enddate = Enddate.substring(4, 6) + "/" + Enddate.substring(2, 4) + "/" + Enddate.substring(0, 2) + Enddate.substring(6, 8) + ":" + Enddate.substring(8);
        notamtext = Notamtext;
        affectedfeature = Affectedfeature;
        geom = Geom;
    }

    public Notam(JSONObject j) throws JSONException{
        eventid = j.getString("eventid");
        notamnumber = j.getString("notamnumber");
        aclass = j.getString("class");
        startdate = j.getString("startdate");
        enddate = j.getString("enddate");
        notamtext = j.getString("notamtext");
        affectedfeature = j.getString("affectedfeature");
        geom = j.getString("geom");
    }

    public String toStringhead(){
        return new String(getNotamnumber() + "\n" + getNotamtext());
    }
    public String toString(){
        return new String(getStartdate() + "\n" + getEnddate() + "\n" + getClass());
    }

    public String getEventid() {
        return "EventID: " + eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public String getNotamnumber() {
        return "Notam Number: " + notamnumber;
    }

    public void setNotamnumber(String notamnumber) {
        this.notamnumber = notamnumber;
    }

    public String getAclass() {
        return "Class: " + aclass;
    }

    public void setAclass(String aclass) {
        this.aclass = aclass;
    }

    public String getStartdate() {
        return "Startdate: " + startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return "End Date: " + enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getNotamtext() {
        return "Notam Text: " + notamtext;
    }

    public void setNotamtext(String notamtext) {
        this.notamtext = notamtext;
    }

    public String getAffectedfeature() {
        return "Affected Feature: " + affectedfeature;
    }

    public void setAffectedfeature(String affectedfeature) {
        this.affectedfeature = affectedfeature;
    }

    public String getGeom() {
        return "Geom: " + geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }
}
