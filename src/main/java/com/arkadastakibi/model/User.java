package com.arkadastakibi.model;

import org.json.JSONArray;
import org.json.JSONObject;
import com.arkadastakibi.model.Notification;

import java.util.ArrayList;
import java.util.Arrays;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String gender;
    private String bio;

    //Diğer sosyal medya hesapları
    private String instagramLink;
    private String twitterLink;
    private String tiktokLink;


    private ArrayList<Integer> followerUser; //Takipçi
    private ArrayList<Integer> followingUser;
    private ArrayList<Notification> notifications;


    public User(int id, String firstName, String lastName, String username,String gender,String password, String email){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.gender=gender;
        this.password=password;
        this.bio = "";
        this.instagramLink = "";
        this.twitterLink = "";
        this.tiktokLink = "";
        this.followerUser = new ArrayList<Integer>(); //Takipçi
        this.followingUser = new ArrayList<Integer>();
        this.notifications = new ArrayList<>();
    }

    public User(JSONObject data){
        this.id = data.getInt("id");
        this.username = data.getString("username");

        this.firstName = data.optString("firstName", "");
        this.lastName = data.optString("lastName", "");
        this.email = data.optString("email", "");
        this.password = data.optString("password", "");
        this.gender = data.optString("gender", "Belirtilmemiş");
        this.bio = data.optString("bio", "");

        this.instagramLink = data.optString("instagram","");
        this.twitterLink = data.optString("twitter","");
        this.tiktokLink = data.optString("tiktok","");

        this.followerUser = new ArrayList<>();
        this.followingUser = new ArrayList<>();
        this.notifications = new ArrayList<>();

        JSONArray followerUser_ = data.getJSONArray("followerUser");
        for (int i = 0; i < followerUser_.length(); i++){
            this.followerUser.add(followerUser_.getInt(i));
        }

        JSONArray followingUser_ = data.getJSONArray("followingUser");
        for (int i = 0; i < followingUser_.length(); i++){
            this.followingUser.add(followingUser_.getInt(i));
        }

        if (data.has("notifications")) {
            JSONArray notifsArray = data.getJSONArray("notifications");
            for (int i = 0; i < notifsArray.length(); i++) {
                this.notifications.add(new Notification(notifsArray.getJSONObject(i)));
            }
        }
    }

    public JSONObject toJSON(){
        JSONObject user = new JSONObject();
        user.put("id", id);
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.put("username", username);
        user.put("email", email);
        user.put("bio", bio);
        user.put("password", password);
        user.put("gender", gender);
        user.put("instagramLink", instagramLink);
        user.put("twitterLink", twitterLink);
        user.put("tiktokLink", tiktokLink);

        user.put("followerUser",new JSONArray(followerUser));
        user.put("followingUser",new JSONArray(followingUser));

        JSONArray notifsArray = new JSONArray();
        for(Notification n : this.notifications) {
            notifsArray.put(n.toJSON());
        }
        user.put("notifications", notifsArray);

        return user;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getBio() {
        return bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }
    public String getInstagramLink() {
        return instagramLink;
    }
    public void setInstagramLink(String instagramLink) {
        this.instagramLink = instagramLink;
    }
    public String getTwitterLink() {
        return twitterLink;
    }
    public void setTwitterLink(String twitterLink) {
        this.twitterLink = twitterLink;
    }
    public String getTiktokLink() {
        return tiktokLink;
    }
    public void setTiktokLink(String tiktokLink) {
        this.tiktokLink = tiktokLink;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public ArrayList<Integer> getFollowerUser() {
        return followerUser;
    }
    public ArrayList<Integer> getFollowingUser() {
        return followingUser;
    }
    public ArrayList<Notification> getNotifications() {return notifications;}
}
