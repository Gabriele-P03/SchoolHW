package com.schoolhw;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Files {

    public static File getDir(Context context, String dirPath){
        File dir = new File(context.getFilesDir(), dirPath);
        if(!dir.exists()){
            dir.mkdir();
        }
        return dir;
    }

    public static File getFile(Context context, String fileName){
        File file = new File(context.getFilesDir(), fileName);
        if(!file.exists()){
            try {
                if (file.createNewFile()) {
                    Toast.makeText(context.getApplicationContext(), "File has been created inside " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                Toast.makeText(context.getApplicationContext(), "File hasn't been created", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        return file;
    }

    public static BufferedReader getBufferedReader(Context context, String fileName) throws FileNotFoundException {
        return new BufferedReader(new FileReader(getFile(context, fileName)));
    }

    public static BufferedWriter getBufferedWriter(Context context, String fileName) throws IOException{
        return new BufferedWriter(new FileWriter(getFile(context, fileName)));
    }
}