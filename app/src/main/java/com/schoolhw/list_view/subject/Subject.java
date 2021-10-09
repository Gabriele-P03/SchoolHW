package com.schoolhw.list_view.subject;

import com.schoolhw.json.JSONArray;
import com.schoolhw.json.JSONMap;
import com.schoolhw.json.JSONObject;
import com.schoolhw.list_view.days.Days;

public class Subject {


    private String subjectName;
    private Days days[];
    private int color;

    public Subject(String subjectName, Days[] days, int color) {
        this.subjectName = subjectName;
        this.days = days;
        this.color = color;
    }

    public String getSubjectName() {
        return subjectName;
    }
    public Days[] getDays() {
        return days;
    }
    public int getColor() { return color; }

    public String daysAsString(){
        String tmp = "";

        for(Days day : this.days){
            tmp += day.getName() + "  ";
        }

        return tmp;
    }

    private JSONArray daysToJSONArray(){
        JSONArray array = new JSONArray();
        array.setName("days");

        for(Days day : this.days){
            array.getValues().add(String.valueOf(day.getIndex()));
        }

        return array;
    }

    public JSONObject toJSONObject() {
        JSONObject object = new JSONObject();
        object.setName(this.subjectName);
        object.getArrays().add(this.daysToJSONArray());
        object.getMaps().add(new JSONMap("color", String.valueOf(this.color)));

        return object;
    }
}
