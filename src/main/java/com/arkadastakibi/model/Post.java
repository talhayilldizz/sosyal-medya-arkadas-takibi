package com.arkadastakibi.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class Post {
    private int postId;
    private int userId;
    private String postContent;
    private String postDate;
    private ArrayList<Integer> likes;
    private ArrayList<Comment>comments;

    public Post(int postId, int userId,String postContent,String postDate){
        this.postId = postId;
        this.userId = userId;
        this.postContent = postContent;
        this.postDate = postDate;
        this.likes = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public Post(JSONObject post){
        this.postId = post.getInt("postId");
        this.userId = post.getInt("userId");
        this.postContent = post.getString("postContent");
        this.postDate = post.getString("postDate");

        this.likes = new ArrayList<>();
        this.comments = new ArrayList<>();

        JSONArray likesArray = post.getJSONArray("likes");
        for (int i=0;i<likesArray.length();i++){
            likes.add(likesArray.getInt(i));
        }

        JSONArray commentsArray = post.getJSONArray("comments");
        for (int i = 0; i < commentsArray.length(); i++) {
            JSONObject commentJson = commentsArray.getJSONObject(i);
            comments.add(new Comment(commentJson));
        }

    }

    public JSONObject toJSON(){
        JSONObject obj = new JSONObject();
        obj.put("postId", postId);
        obj.put("userId", userId);
        obj.put("postContent", postContent);
        obj.put("postDate", postDate);

        obj.put("likes",new JSONArray(likes));
        obj.put("comments",new JSONArray(comments));

        return obj;
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
