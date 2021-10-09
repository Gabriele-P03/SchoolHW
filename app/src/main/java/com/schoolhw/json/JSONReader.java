package com.schoolhw.json;

import android.os.Build;
import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * {@link JSONReader} provides json reading
 */

public class JSONReader {

    private FileInputStream fis;

    //This string represents json with \n and space
    private String jsonAsString;

    //JSON file always begins with curly brackets
    private JSONObject mainObject;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public JSONReader(String path) throws IOException {this(new File(path));}
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public JSONReader(File file) throws IOException { this(new FileInputStream(file));}
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public JSONReader(FileInputStream fis) throws IOException {
        this.fis = fis;
        this.readJSON();
    }

    /**
     * Once called, it read json file and save everything inside the mainObject
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void readJSON() throws IOException {

        byte[] bytesRead = new byte[4096];
        this.fis.read(bytesRead);
        this.jsonAsString = new String(bytesRead, StandardCharsets.UTF_8);

        String fileAsString = String.copyValueOf(this.jsonAsString.toCharArray());
        fileAsString = fileAsString
                .substring(1, fileAsString.length()-1)  //Remove first and last curly brackets
                .replaceAll("\n", "")   //Remove \n
                .replaceAll(" ", "")    //Remove space
                .replaceAll("\"", "");  //Remove '"'


        this.mainObject = new JSONObject(fileAsString);
    }


    public void close(){
        try {
            this.fis.close();
        } catch (IOException e) { e.printStackTrace(); }
    }

    public FileInputStream getFis() { return fis; }

    public JSONObject getMainObject() {
        return mainObject;
    }

    public static boolean isValidChar(char c) {return isNotBracket(c) && isNotColon(c) && isNotComma(c);}
    public static boolean isNotColon(char c) {return c != ':';}
    public static boolean isNotComma(char c) {return c != ',';}
    public static boolean isNotBracket(char c){
        return c != '[' && c != ']' && c != '{' && c != '}';
    }
}
