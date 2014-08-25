package com.Velamati.Abhilaash.networkconnect;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by Abhilaash on 8/21/2014.
 */
public class Airport {

    String arptseq;
    String arptid;
    String arptname;
    String wkt;
    String point;

    public Airport(JSONObject j) throws JSONException, ParseException {
        if(j.getString("arpt_seq") != null)
            arptseq = j.getString("arpt_seq");
        if(j.getString("arpt_id") != null)
            arptid = j.getString("arpt_id");
        if (j.getString("arpt_name") != null)
            arptname = j.getString("arpt_name");
        if(j.getString("wkt") != null)
            wkt = j.getString("wkt");
    }
    public String toString(){
        return getArptid() + ":" + getArptname();
    }
    public String getArptseq() {
        return arptseq;
    }

    public void setArptseq(String arptseq) {
        this.arptseq = arptseq;
    }

    public String getArptid() {
        return arptid;
    }

    public void setArptid(String arptid) {
        this.arptid = arptid;
    }

    public String getArptname() {
        return arptname;
    }

    public void setArptname(String arptname) {
        this.arptname = arptname;
    }

    public String getWkt() {
        return wkt;
    }

    public void setWkt(String wkt) {
        this.wkt = wkt;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

}