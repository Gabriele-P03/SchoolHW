package com.schoolhw;

import android.content.Context;

import java.io.*;
import java.util.ArrayList;

public class Settings {

    private File settingsFile;
    private BufferedReader br;
    private BufferedWriter bw;

    private ArrayList<SettingsMap> settings;

    public Settings(Context context) {
        this.settingsFile = new File(context.getFilesDir(), "settings.conf");
        this.settings = new ArrayList<>();

        this.checkFile();

        try {
            this.br = new BufferedReader(new FileReader(this.settingsFile));
        }catch (IOException e){
            e.printStackTrace();
        }

        this.loadSettings();
    }

    /**
     * Load settings from settings.conf
     */
    public void loadSettings(){
        String line = null;
        try {
            while( (line = this.br.readLine()) != null) {
                String tmp[] = line.split("=");
                tmp[0] = tmp[0].replace("=", "");

                this.settings.add(new SettingsMap(tmp[0], tmp[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class SettingsMap{
        private String setting, value;

        public SettingsMap(String setting, String value) {
            this.setting = setting;
            this.value = value;
        }

        public String getSetting() {
            return setting;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * If the file doesn't exists, it creates and write default settings
     */
    private void checkFile(){

        if(!this.settingsFile.exists()) {
            try {
                this.settingsFile.createNewFile();
                this.settings.add(new SettingsMap("notification", "false"));
                this.saveSettings();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Write all settings contained inside array
     */
    private void saveSettings(){
        try {
            this.bw = new BufferedWriter(new FileWriter(this.settingsFile));

            for(SettingsMap settingsMap : this.settings) {
                this.bw.write(settingsMap.getSetting() + "=" + settingsMap.getValue() + "\n");
            }

            this.bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
