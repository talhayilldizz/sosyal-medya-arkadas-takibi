package com.arkadastakibi.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DataBase {

    private File file;
    private String FilePath;

    public DataBase(String filePath) {
        this.file = new File(filePath);
        this.FilePath = filePath;
    }

    public JSONArray readData(){
        try {
            String content = new String(Files.readAllBytes(Paths.get(this.FilePath)));
            JSONArray fileArray = new JSONArray(content);
            return fileArray;

        }catch (Exception e) {
            e.printStackTrace();
            return new  JSONArray();
        }

    }

    public int writeData(JSONArray data){
        try (FileWriter writer = new FileWriter(this.file)) {
            writer.write(data.toString(4));
            return 1;
        }catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
