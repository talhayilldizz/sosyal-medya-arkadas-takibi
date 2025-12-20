package com.arkadastakibi.model;

import java.util.ArrayList;
import java.util.Date;

public class Post {
    private int postId;
    private int userId;
    private String postTitle;
    private String postContent;
    private String postDate;
    private ArrayList<Integer> likes;
    private ArrayList<Comment>comments;

    public Post(int postId, int userId, String postTitle,String postContent,String postDate){
        this.postId = postId;
        this.userId = userId;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postDate = postDate;
        likes = new ArrayList<>();
        comments = new ArrayList<>();
    }

    public int getPostId() {
        return postId;
    }
    public void setPostId(int postId) {
        this.postId = postId;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getPostTitle() {
        return postTitle;
    }
    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }
    public String getPostContent() {
        return postContent;
    }
    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }
    public String getPostDate() {
        return postDate;
    }
    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }
    public ArrayList<Integer> getLikes() {
        return likes;
    }
    public void setLikes(ArrayList<Integer> likes) {
        this.likes = likes;
    }
    public ArrayList<Comment> getComments() {
        return comments;
    }
    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }


}
