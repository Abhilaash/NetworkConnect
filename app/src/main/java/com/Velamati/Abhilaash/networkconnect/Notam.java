package com.Velamati.Abhilaash.networkconnect;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Notam {
    private String eventid;
    private String notamnumber;
    private String aclass;
    private String startdate;
    private DateFormat startdateformat;
    private String enddate;
    private DateFormat enddateformat;
    private String notamtext;
    private String affectedfeature;
    private String url;
    private String geom;

    public Notam(JSONObject j) throws JSONException, ParseException {
        if(j.getString("eventid") != null)
            eventid = j.getString("eventid");
        if(j.getString("notamnumber") != null)
            notamnumber = j.getString("notamnumber");
        if(j.getString("class") != null)
            aclass = j.getString("class");
        if(j.getString("startdate") != null) {
            String Startdate = j.getString("startdate").trim();
//            Date start = new Date(-1, -1, -1);
//            start.setYear(Integer.parseInt(Startdate.substring(0, 2)));
//            start.setMonth(Integer.parseInt(Startdate.substring(2, 4)));
//            start.setDate(Integer.parseInt(Startdate.substring(4, 6)));
//            start.setHours(Integer.parseInt(Startdate.substring(6, 8)));
//            start.setMinutes(Integer.parseInt(Startdate.substring(8)));
//            startdate = start.toGMTString();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmm");
            Date start = simpleDateFormat.parse(Startdate);
            startdate = start.toGMTString();
            //startdate = Startdate.substring(2, 4) + "/" + Startdate.substring(4, 6) + "/" + Startdate.substring(0, 2) + "\t" + Startdate.substring(6, 8) + ":" + Startdate.substring(8);
        }
        if(j.getString("enddate") != null) {
            String Enddate = j.getString("enddate").trim();
//            Date end = new Date(-1, -1, -1);
//            end.setYear(Integer.parseInt(Enddate.substring(0, 2)));
//            end.setMonth(Integer.parseInt(Enddate.substring(2, 4)));
//            end.setDate(Integer.parseInt(Enddate.substring(4, 6)));
//            end.setHours(Integer.parseInt(Enddate.substring(6, 8)));
//            end.setMinutes(Integer.parseInt(Enddate.substring(8)));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmm");
            Date end = (Date) simpleDateFormat.parse(Enddate);
            enddate = end.toGMTString();
//         enddate = Enddate.substring(2, 4) + "/" + Enddate.substring(4, 6) + "/" + Enddate.substring(0, 2) + "\t" + Enddate.substring(6, 8) + ":" + Enddate.substring(8);
        }
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

    public String toString()
    {
        return getStartdate() + "\n" + getEnddate() + "\n" + getclass();
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
