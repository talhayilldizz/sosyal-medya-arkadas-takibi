package com.arkadastakibi.model;

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


    private int followerCount; //Takipçi
    private int followingCount; //Takip Edilen

    public User(){
        this.followerCount = 0;
        this.followingCount = 0;
    }

    public User(int id, String firstName, String lastName, String username, String email, String bio){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.bio = bio;
        this.followerCount = 0;
        this.followingCount = 0;
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
    public int getFollowerCount() {
        return followerCount;
    }
    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }
    public int getFollowingCount() {
        return followingCount;
    }
    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }


}
