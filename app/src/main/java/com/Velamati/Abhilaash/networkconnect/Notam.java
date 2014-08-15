package com.Velamati.Abhilaash.networkconnect;

import org.json.JSONException;
import org.json.JSONObject;

public class Notam {
    private String eventid;
    private String notamnumber;
    private String aclass;
    private String startdate;
    private String enddate;
    private String notamtext;
    private String affectedfeature;
    private String url;
    private String geom;

//    public Notam(String Eventid, String Notamnumber, String Class, String Startdate, String Enddate, String Notamtext, String Affectedfeature, String Url, String Geom){
//        eventid = Eventid;
//        notamnumber = Notamnumber;
//        aclass = Class;
//        startdate = Startdate.substring(4, 6) + "/" + Startdate.substring(2, 4) + "/" + Startdate.substring(0, 2) + Startdate.substring(6, 8) + ":" + Startdate.substring(8);
//        enddate = Enddate.substring(4, 6) + "/" + Enddate.substring(2, 4) + "/" + Enddate.substring(0, 2) + Enddate.substring(6, 8) + ":" + Enddate.substring(8);
//        notamtext = Notamtext;
//        affectedfeature = Affectedfeature;
//        url = Url;
//        geom = Geom;
//    }

    public Notam(JSONObject j) throws JSONException{
        if(j.getString("eventid") != null)
            eventid = j.getString("eventid");
        if(j.getString("notamnumber") != null)
            notamnumber = j.getString("notamnumber");
        if(j.getString("class") != null)
            aclass = j.getString("class");
        if(j.getString("startdate") != null)
            startdate = j.getString("startdate");
        if(j.getString("enddate") != null)
            enddate = j.getString("enddate");
        if(j.getString("notamtext") != null)
            notamtext = j.getString("notamtext");
        if(j.getString("affectedfeature") != null)
            affectedfeature = j.getString("affectedfeature");
        if(j.has("image"))
            if (j.getString("image") != null)
                url = j.getString("image");
            else
                url = "";
        else url = "";
        if(j.getString("geom") != null)
            if(!j.getString("geom").equals(""))
                geom = j.getString("geom");
            else
                geom = "";
        else
            geom = "";
    }

//    public String toStringhead(){
//        return new String(getNotamnumber() + "\n" + getNotamtext());
//    }

    public String toString()
    {
        return getStartdate() + "\n" + getEnddate() + "\n" + getclass();
//        String s = "";
//        if(this.getStartdate() != null && !startdate.equals(""))
//            s += "Start Date: " + startdate;
//
//        if(enddate != null && !enddate.equals(""))
//            s += "End Date: " + enddate;
//
//        if(aclass != null && !aclass.equals(""))
//            s += "Class: " + aclass;
//        return s;
    }

    public String getEventid() {
        if(eventid != null)
            return "EventID: " + eventid;
        return null;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public String getNotamnumber() {
        if(notamnumber != null && !notamnumber.equals(""))
            return "Notam Number: " + notamnumber;
        return null;
    }

    public void setNotamnumber(String notamnumber) {
        this.notamnumber = notamnumber;
    }

    public String getclass() {
        if(aclass != null && !aclass.equals(""))
            return "Class: " + aclass;
        return null;
    }

    public void setclass(String aclass) {
        this.aclass = aclass;
    }

    public String getStartdate() {
        if(startdate != null && !startdate.equals(""))
            return "Start Date: " + startdate;
        return null;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        if(enddate != null && !enddate.equals(""))
            return "End Date: " + enddate;
        return null;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getNotamtext() {
        if(notamtext != null && !notamtext.equals(""))
            return "Notam Text: " + notamtext;
        return notamtext;
    }

    public void setNotamtext(String notamtext) {
        this.notamtext = notamtext;
    }

    public String getAffectedfeature() {
        if(affectedfeature != null && !affectedfeature.equals(""))
            return "Affected Feature: " + affectedfeature;
        return null;
    }

    public void setAffectedfeature(String affectedfeature) {
        this.affectedfeature = affectedfeature;
    }
    public String getUrl(){
        if(url != null && !url.equals(""))
            return url;
        return null;
    }

    public void setUrl(String Url){
        url = Url;
    }

    public String getGeom() {
        if(geom != null && !geom.equals(""))
            return "Geographical Location: " + geom;
        return null;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }

    public int size(){
        int x = 0;
        if(getclass() != null)
            x++;
        if(getStartdate() != null)
            x++;
        if(getEnddate() != null)
            x++;
        if(getGeom() != null)
            x++;
        return x;
    }
}
