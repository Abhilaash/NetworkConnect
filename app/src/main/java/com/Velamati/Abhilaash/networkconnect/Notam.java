package com.Velamati.Abhilaash.networkconnect;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Notam {

    private String eventid;
    private String notamnumber;
    private String aclass;
    private String scenario;
    private String affectedfir;
    private String location;
    private String notamtext;
    private String startdate;
    private String enddate;
    private String issued;
    private String affectedfeature;
    private String url;
    private String geom;

    public Notam(JSONObject j) throws JSONException, ParseException {
        if(j.getString("eventid") != null)
            eventid = j.getString("eventid");
        if(j.getString("notamnumber") != null)
            notamnumber = j.getString("notamnumber");
        if(j.has("classcode")) {
            if (j.getString("classcode") != null)
                aclass = j.getString("classcode");
        }
        if(j.getString("class") != null)
            aclass = j.getString("class");
        if(j.getString("scenario") != null){
            scenario = j.getString("scenario");
        }
        if(j.getString("affectedfir") != null){
            affectedfir = j.getString("affectedfir");
        }
        if(j.getString("location") != null){
            location = j.getString("location");
        }
        if(j.getString("notamtext") != null)
            notamtext = j.getString("notamtext");
        if(j.getString("effectivestart") != null) {
            String Startdate = j.getString("effectivestart").trim();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmm");
            Date start = simpleDateFormat.parse(Startdate);
            startdate = start.toGMTString();
        }
        if(j.getString("effectiveend") != null) {
            String Enddate = j.getString("effectiveend").trim();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmm");
            Date end = simpleDateFormat.parse(Enddate);
            enddate = end.toGMTString();
        }
        if(j.getString("issued") != null){
            String Issued = j.getString("issued").trim();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmm");
            Date issue = simpleDateFormat.parse(Issued);
            issued = issue.toGMTString();
        }
        if(j.getString("affectedfeature") != null)
            affectedfeature = j.getString("affectedfeature");
        if(j.has("image"))
            if (j.getString("image") != null)
                url = j.getString("image");
            else
                url = "";
        else url = "";
        if(!j.getString("geomwkt").equals(""))
            geom = j.getString("geomwkt");
        else
            geom = "";
    }

    public String toString() {
        return getStartdate() + "\n" + getEnddate();
    }

    public String getEventid() {
        if(eventid != null)
            return "EventID: " + eventid;
        return null;
    }

    public String getOnlyEventid(){
        return eventid;
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

    public String getScenario() {
        return "Scenario: " + scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public String getAffectedfir() {
        return "Affected Fir: " + affectedfir;
    }

    public void setAffectedfir(String affectedfir) {
        this.affectedfir = affectedfir;
    }

    public String getLocation() {
        return "Location: " + location;
    }

    public void setLocation(String location) {
        this.location = location;
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
    public String getIssued() {
        if(issued != null && !issued.equals(""))
            return "Issue Date: " + issued;
        return null;
    }
    public void setIssued(String issued){
        this.issued = issued;
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
