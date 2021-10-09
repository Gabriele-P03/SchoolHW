package com.schoolhw.json;

import java.util.ArrayList;

/**
 * An object in a json file is represented by curly brackets
 */

public class JSONObject {

     //to a key name not available for objects/arrays declared inside themselves, it can be null
    private String name;

    private int length = 0;

    private ArrayList<JSONObject> objects = new ArrayList<>();
    private ArrayList<JSONMap> maps = new ArrayList<>();
    private ArrayList<JSONArray> arrays = new ArrayList<>();

    public JSONObject(){

    }

    public JSONObject(String objectAsString){
        this("", objectAsString);
    }


    public JSONObject(String name, String objectAsString){

        this.name = name;
        String tmp = "";

        for(int index = 0; index < objectAsString.length(); index++){

            char c = objectAsString.charAt(index);

            //Lenght takes count of commas, brackets and colons, too
            this.length++;

            if(c == '}')
                break;

            if(JSONReader.isValidChar(c)){
                tmp += c;
            }else {

                if (c == ':') {

                    String nextChar = this.getNextChar(objectAsString, index);
                    if(nextChar != "" && nextChar != null) {

                        if (nextChar.equals("{")) {
                            JSONObject object = new JSONObject(tmp, objectAsString.substring(index+1));
                            this.objects.add(object);
                            this.length += object.getLength();
                            index += object.getLength();
                        } else if (nextChar.equals("[")) {
                            JSONArray array = new JSONArray(tmp, objectAsString.substring(index+1));
                            this.arrays.add(array);
                            this.length += array.getLength();
                            index += array.getLength();
                        } else if (nextChar.equals("}")) {
                            break;
                        } else if (nextChar.equals("]")) {
                            continue;
                        }else if(nextChar.equals(',')) {
                            continue;
                        }else{
                            JSONMap map = new JSONMap(tmp, objectAsString.substring(index+1));
                            this.maps.add(map);
                            length += map.getValue().length();
                            index += map.getValue().length();
                        }
                    }
                }
                tmp = "";
            }
        }
    }

    private String getNextChar(String string, int index){
        return string.length() > index + 1? String.valueOf(string.charAt(index+1)) : null;
    }



    public void addObject(String objectAsString){ this.objects.add(new JSONObject(objectAsString)); }
    public void addArray(String arrayAsString){ this.arrays.add(new JSONArray(arrayAsString)); }
    public void addMap(String mapAsString){ this.maps.add(new JSONMap(mapAsString)); }

    public JSONObject getObjectByName(String name){

        for(JSONObject object : this.objects){
            if(object.name.equals(name))
                return object;
        }

        return null;
    }
    public JSONArray getArrayByName(String name){
        for(JSONArray array : this.arrays){
            if(array.getName().equals(name))
                return array;
        }

        return null;
    }

    public JSONMap getMapByName(String name){
        for(JSONMap map : this.maps){
            if(map.getKey().equals(name))
                return map;
        }

        return null;
    }

    public ArrayList<JSONObject> getObjects() { return objects; }
    public ArrayList<JSONMap> getMaps() { return maps; }
    public ArrayList<JSONArray> getArrays() { return arrays; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name;    }

    /**
     *
     * @return how many chars this object is composed of
     */
    public int getLength(){
        return this.length;
    }

    @Override
    public String toString() {
        String asString = "";

        if(this.name != null && this.name != ""){
            asString += "\"" + this.name + "\": ";
        }

        asString += "{\n";

        for(int i = 0; i < this.objects.size(); i++){
            asString += this.objects.get(i).toString();

            if( i+1 < this.objects.size() || !this.arrays.isEmpty() || !this.maps.isEmpty()){
                asString += ",";
            }

            asString += "\n";
        }


        for(int i = 0; i < this.arrays.size(); i++){
            asString += this.arrays.get(i).toString();

            if( i+1 < this.arrays.size() || !this.maps.isEmpty()){
                asString += ",";
            }

            asString += "\n";
        }


        for(int i = 0; i < this.maps.size(); i++){
            asString += this.maps.get(i).toString();

            if( i+1 < this.maps.size()){
                asString += ",";
            }

            asString += "\n";
        }

        asString += "\n}";

        return asString;
    }
}
