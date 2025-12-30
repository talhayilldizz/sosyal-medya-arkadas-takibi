package com.arkadastakibi.model;

import org.json.JSONObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Notification {
    private String senderUsername; // Bildirimi gönderen
    private String message;        // Mesaj
    private String time;           // Zaman
    private boolean firstSeen;

    //Yeni bildirim oluştururken kullanılır
    public Notification(String senderUsername, String message) {
        this.senderUsername = senderUsername;
        this.message = message;
        //Şu anki saati alıp formatlıyoruz (Gün/Ay Saat:Dakika)
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM HH:mm");
        this.time = dtf.format(LocalDateTime.now());
    }

    //JSON'dan okurken kullanılır
    public Notification(JSONObject obj) {
        this.senderUsername = obj.getString("senderUsername");
        this.message = obj.getString("message");
        this.time = obj.optString("time", "");
        this.firstSeen = obj.optBoolean("firstSeen", false);
    }


    //Kaydederken kullanılır
    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("senderUsername", senderUsername);
        obj.put("message", message);
        obj.put("time", time);
        obj.put("firstSeen", firstSeen);
        return obj;
    }

    public String getSenderUsername() { return senderUsername; }
    public String getMessage() { return message; }
    public String getTime() { return time; }
    public boolean getFirstSeen() {return firstSeen; }
    public void setFirstSeen(boolean firstSeen) { this.firstSeen = firstSeen; }
}