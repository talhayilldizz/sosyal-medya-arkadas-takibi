package com.arkadastakibi.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String bio;

    //Diğer sosyal medya hesapları
    private String instagramLink;
    private String twitterLink;
    private String tiktokLink;


    private ArrayList<Integer> followerUser; //Takipçi
    private ArrayList<Integer> followingUser;


    public User(int id, String firstName, String lastName, String username, String email, String bio){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.bio = bio;
        this.followerUser = new ArrayList<Integer>(); //Takipçi
        this.followingUser = new ArrayList<Integer>();
    }

    public User(JSONObject data){
        this.id = data.getInt("id");
        this.firstName = data.getString("firstName");
        this.lastName = data.getString("lastName");
        this.username = data.getString("username");
        this.email = data.getString("email");
        this.bio = data.getString("bio");
        this.instagramLink = data.getString("instagramLink");
        this.twitterLink = data.getString("twitterLink");
        this.tiktokLink = data.getString("tiktokLink");

        JSONArray followerUser_ = data.getJSONArray("followerUser");

        for (int i = 0; i < followerUser_.length(); i++){
            this.followerUser.add(followerUser_.getInt(i));
        }
        JSONArray followingUser_ = data.getJSONArray("followingUser");

        for (int i = 0; i < followerUser_.length(); i++){
            this.followerUser.add(followerUser_.getInt(i));
        }
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


}
