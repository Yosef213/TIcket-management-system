/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.project.utils;

import com.mycompany.project.DB.UserDAO;
import java.io.*;

public class ConfigManager {
    public static final String dataFolder = System.getenv("LOCALAPPDATA");
    public static final String APP_FOLDER_NAME = "ticketapp";
    public static final String CONFIG_FILE_NAME = "info.txt";
    public static File appDir = new File(dataFolder, APP_FOLDER_NAME); 
    public static File configFile = new File(appDir, CONFIG_FILE_NAME);
    public  boolean checkConfig(){
        if(appDir.exists()){
            return true;
        }
        else {
            return false;
        }
    }
    public  boolean writeConfig(UserDAO loggedUser){
        boolean write = false;
        if(!this.checkConfig()){
            appDir.mkdirs();
        }
        try (FileWriter fileWriter = new FileWriter(configFile, false);
                BufferedWriter writer = new BufferedWriter(fileWriter)){
            writer.write("username=" + loggedUser.getUsername());
            writer.newLine();
            writer.write("password=" + loggedUser.getPassword());
            writer.newLine();
            return write;
        }
        catch(IOException e){
            
        }
        return write;
    }
public  UserDAO readConfig() {
    if (!configFile.exists()) return null;

    String username = null;
    String password = null;

    try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || !line.contains("=")) continue;

            String[] parts = line.split("=", 2);
            String key = parts[0].trim();
            String value = parts[1].trim();

            if (key.equalsIgnoreCase("username")) username = value;
            else if (key.equalsIgnoreCase("password")) password = value;
        }
    } catch (IOException e) {
        return null;
    }

    if (username == null || password == null) return null;
    return new UserDAO(username, password);
}
public boolean clearConfig() {
    boolean ok = true;

    try {
        // delete the info.txt first
        if (configFile.exists()) {
            ok = configFile.delete();
        }

        // optional: delete the folder if empty
        if (appDir.exists() && appDir.isDirectory()) {
            File[] files = appDir.listFiles();
            if (files == null || files.length == 0) {
                appDir.delete();
            }
        }
    } catch (Exception e) {
        ok = false;
    }

    return ok;
}
}
