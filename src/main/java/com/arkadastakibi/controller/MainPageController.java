package com.arkadastakibi.controller;

import com.arkadastakibi.model.Comment;
import com.arkadastakibi.model.Notification;
import com.arkadastakibi.model.Post;
import com.arkadastakibi.model.User;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.List;

public class MainPageController extends BaseController {

    @FXML private TextField txtSearch;
    @FXML private Label lblMyName;
    @FXML private Label lblMyUsername;
    @FXML private Circle imgAvatar;
    @FXML private VBox vboxFriendsList;
    @FXML private VBox vboxAllUsers;

    @FXML private VBox vboxCenterContent;

    private User loggedUser;

    public void setKullaniciBilgileri(String name, String username, String gender) {
        if(this.app.Users == null){
            return;
        }

        for(User user : this.app.Users){
            if(user.getUsername().equals(username)){
                this.loggedUser = user;
                break;
            }
        }

        if(this.loggedUser == null){
            return;
        }

        lblMyName.setText(this.loggedUser.getFirstName()+ " "+this.loggedUser.getLastName());
        lblMyUsername.setText("@" + username);

        imgAvatar.setFill(getAvatarPattern(this.loggedUser.getGender()));

        loadList();

        // Sayfa ilk açıldığında postları yükle
        loadHomeFeed();
    }

    private void loadList(){
        vboxAllUsers.getChildren().clear();
        vboxFriendsList.getChildren().clear();

        if(this.app.Users == null || this.loggedUser == null){
            return;
        }

        List<Integer> followingUserId = this.loggedUser.getFollowingUser();
        for(User user : this.app.Users){
            if(user.getId() == this.loggedUser.getId()){
                continue;
            }
            vboxAllUsers.getChildren().add(createUserRow(user));
            if(followingUserId.contains(user.getId())){
                vboxFriendsList.getChildren().add(createUserRow(user));
            }
        }
    }

    // Ana Sayfa (Feed) Yükleme Metodu
    @FXML
    public void showHomeFeed(MouseEvent event) {
        loadHomeFeed();
    }

    private void loadHomeFeed() {
        vboxCenterContent.getChildren().clear();

        // Paylaşım Alanı Kutusu
        VBox shareBox = new VBox(10);
        shareBox.getStyleClass().add("card-view");

        Label lblThink = new Label("Ne düşünüyorsun?");
        lblThink.getStyleClass().add("lbl-secondary-header");

        TextArea txtContent = new TextArea();
        txtContent.setPromptText("Buraya bir şeyler yaz...");

        txtContent.setPrefHeight(80);
        txtContent.setWrapText(true);
        txtContent.getStyleClass().add("txt-area-input");

        HBox btnBox = new HBox();
        btnBox.setAlignment(Pos.CENTER_RIGHT);

        Button btnShare = new Button("Paylaş");
        btnShare.getStyleClass().add("btn-primary");
        btnShare.setPadding(new Insets(8, 20, 8, 20));

        btnShare.setOnAction(e->{
            String content=txtContent.getText().trim();

            if(content.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setContentText("Post İçeriği Boş Olamaz!");
                alert.show();
                return;
            }

            createPost(content);
            txtContent.clear();
            loadHomeFeed();
        });

        btnBox.getChildren().add(btnShare);

        shareBox.getChildren().addAll(lblThink, txtContent, btnBox);
        vboxCenterContent.getChildren().add(shareBox);



        // Paylaşılan postları görüntüleyen fonksiyon
        for(Post post : this.app.Posts){
            User postOwner = app.search_to_user(post.getUserId());
            if(postOwner == null){
                continue;
            }

            String userName = postOwner.getUsername();
            String time = post.getPostDate();
            String content = post.getPostContent();

            vboxCenterContent.getChildren().add(
                    createPostView(post, userName, time, content)
            );
        }
    }

    // Post oluşturma fonksiyonu
    private void createPost(String content){
        if(this.loggedUser == null || this.app == null){
            return;
        }

        int newPostId=1;
        if(!app.Posts.isEmpty()){
            newPostId=app.Posts.get(app.Posts.size()-1).getPostId() +1;
        }

        String postDate = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());

        Post newPost = new Post(newPostId, this.loggedUser.getId(), content, postDate);
        app.Posts.add(newPost);
        app.update();
    }

    // Beğeni butonu güncelleme
    private void updateLikeButton(Button btnLike, Post post){
        int likeCount = post.getLikes().size();
        boolean liked = post.getLikes().contains(loggedUser.getId());

        btnLike.setText((liked ? "❤️ Beğenildi " : "Beğen ") + "(" + likeCount + ")");
        btnLike.getStyleClass().clear(); // Önceki sınıfları temizle
        btnLike.getStyleClass().add("btn-action"); // CSS sınıfını ekle
    }

    private boolean toggleLike(Post post){
        int userId = loggedUser.getId();

        if(post.getLikes().contains(userId)){
            post.getLikes().remove(Integer.valueOf(userId)); // beğeniyi geri alma
            app.update();
            return false;
        }
        else {
            post.getLikes().add(userId);
            app.update();
            return true;
        }
    }

    // Bildirimler Sayfası Metodu
    @FXML
    public void showNotifications(ActionEvent event) {
        vboxCenterContent.getChildren().clear();

        HBox headerBox = new HBox(15);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(0, 0, 20, 0));

        Button btnBack = new Button("← Geri");
        btnBack.getStyleClass().add("btn-back");

        // Geri Dön -> Ana Sayfayı Yükle
        btnBack.setOnAction(e -> loadHomeFeed());

        Label lblTitle = new Label("Bildirimler");
        lblTitle.getStyleClass().add("lbl-page-title");

        headerBox.getChildren().addAll(btnBack, lblTitle);
        vboxCenterContent.getChildren().add(headerBox);

        // Bildirim Listesi
        if (this.loggedUser != null && !this.loggedUser.getNotifications().isEmpty()) {
            for (Notification notif : this.loggedUser.getNotifications()) {
                vboxCenterContent.getChildren().add(createNotificationItem(
                        notif.getSenderUsername(),
                        notif.getMessage(),
                        notif.getTime()
                ));
            }
        } else {
            // Hiç bildirim yoksa mesaj göster
            Label lblEmpty = new Label("Henüz yeni bir bildirim yok.");
            lblEmpty.getStyleClass().add("lbl-placeholder");
            vboxCenterContent.getChildren().add(lblEmpty);
        }
    }

    private VBox createPostView(Post post, String username, String time, String content) {
        int postOwnerId = post.getUserId();
        User postOwner = app.search_to_user(postOwnerId);

        VBox postBox = new VBox(10);
        postBox.getStyleClass().add("post-card");

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Circle avatar = new Circle(20, Color.web("#e0e0e0"));
        avatar.setFill(getAvatarPattern(postOwner.getGender()));

        VBox titles = new VBox();
        Label nameLbl = new Label(username);
        nameLbl.getStyleClass().add("lbl-username");

        Label timeLbl = new Label(time);
        timeLbl.getStyleClass().add("lbl-time");

        titles.getChildren().addAll(nameLbl, timeLbl);
        header.getChildren().addAll(avatar, titles);

        Label contentLbl = new Label(content);
        contentLbl.setWrapText(true);
        contentLbl.getStyleClass().add("lbl-post-content");

        HBox actions = new HBox(20);
        Button btnLike = new Button();

        updateLikeButton(btnLike, post);
        btnLike.setOnAction(e -> {
            boolean likedNow = toggleLike(post);
            updateLikeButton(btnLike, post);

            //beğenildiyse bildirim gönderiyor
            if(likedNow){
                sendNotificationToUser(
                        postOwner,
                        loggedUser.getUsername(),
                        "Gönderinizi Beğendi."
                );
            }
        });


        if(!btnLike.getStyleClass().contains("btn-action")) {
            btnLike.getStyleClass().add("btn-action");
        }

        //yorum işlemleri
        Button btnComment = new Button("Yorum");
        btnComment.getStyleClass().add("btn-action");

        actions.getChildren().addAll(btnLike, btnComment);

        VBox commentBox = new VBox(10);
        commentBox.setVisible(false);
        commentBox.setManaged(false);

        VBox commentsList = new VBox(5);

        // Tüm yorumları al
        for(Comment comment : post.getComments()){
            User commentOwner = app.search_to_user(comment.getUserId());
            if(commentOwner == null){
                continue;
            }

            Label lbl = new Label(
                    commentOwner.getUsername() + ": " + comment.getContent()
            );

            lbl.setWrapText(true);
            lbl.getStyleClass().add("lbl-comment-bubble");
            commentsList.getChildren().add(lbl);
        }

        //yeni yorum atma kısmı
        TextArea txtComment = new TextArea();
        txtComment.setPromptText("Yorum Yaz..");
        txtComment.setPrefHeight(60);

        Button btnSendComment = new Button("Gönder");
        btnSendComment.getStyleClass().add("btn-send-comment");

        btnSendComment.setOnAction(e -> {
            String text = txtComment.getText().trim();
            if(text.isEmpty()){
                return;
            }

            String commentTime = new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date());
            int newCommentId = 1;
            if(!app.Comments.isEmpty()){
                newCommentId = app.Comments.get(app.Comments.size()-1).getId()+1;
            }

            Comment comment = new Comment(
                    newCommentId,
                    post.getPostId(),
                    this.loggedUser.getId(),
                    text,
                    commentTime
            );

            app.Comments.add(comment);
            post.getComments().add(comment);
            app.update();

            Label lbl = new Label(this.loggedUser.getUsername() + ": " + text);
            lbl.setWrapText(true);
            lbl.getStyleClass().add("lbl-comment-bubble");
            commentsList.getChildren().add(lbl);

            txtComment.clear();

            if (post.getUserId() != loggedUser.getId()) {
                sendNotificationToUser(
                        postOwner,
                        loggedUser.getUsername(),
                        "Gönderine yorum yaptı"
                );
            }
        });

        btnComment.setOnAction(e -> {
            boolean open = !commentBox.isVisible();
            commentBox.setVisible(open);
            commentBox.setManaged(open);
        });

        commentBox.getChildren().addAll(commentsList, txtComment, btnSendComment);

        postBox.getChildren().addAll(header, contentLbl, new Separator(), actions, commentBox);
        return postBox;
    }

    private HBox createNotificationItem(String user, String actionText, String time) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("notification-card");

        String gender = "Erkek";
        if (this.app.Users != null) {
            for (User u : this.app.Users) {
                if (u.getUsername().equals(user)) {
                    gender = u.getGender();
                    break;
                }
            }
        }

        Circle avatar = new Circle(20);
        ImagePattern pattern = getAvatarPattern(gender);
        if (pattern != null) {
            avatar.setFill(pattern);
        } else {
            avatar.setFill(Color.LIGHTGRAY);
        }

        VBox txtBox = new VBox(2);
        HBox msgBox = new HBox(5);

        Label nameLbl = new Label(user);
        nameLbl.getStyleClass().add("lbl-notif-user");

        Label actionLbl = new Label(actionText);
        actionLbl.getStyleClass().add("lbl-notif-action");

        msgBox.getChildren().addAll(nameLbl, actionLbl);

        Label timeLbl = new Label(time);
        timeLbl.getStyleClass().add("lbl-notif-time");

        txtBox.getChildren().addAll(msgBox, timeLbl);

        row.getChildren().addAll(avatar, txtBox);
        return row;
    }

    private HBox createUserRow(User user) {
        String uName = user.getUsername();
        String name = user.getFirstName() + " " + user.getLastName();
        String gender = user.getGender();

        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setSpacing(10);
        row.getStyleClass().add("user-list-row");

        Circle avatar = new Circle(18);
        avatar.setFill(getAvatarPattern(gender));

        VBox nameBox = new VBox();
        nameBox.setAlignment(Pos.CENTER_LEFT);

        Label nameLbl = new Label(name);
        nameLbl.getStyleClass().add("lbl-list-name");

        Label userLbl = new Label("@" + uName);
        userLbl.getStyleClass().add("lbl-list-username");

        nameBox.getChildren().addAll(nameLbl, userLbl);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnGit = new Button("Git >");
        btnGit.getStyleClass().add("btn-text-link");
        btnGit.setOnAction(event -> navigateToFriendProfile(event, uName));

        row.getChildren().addAll(avatar, nameBox, spacer, btnGit);
        return row;
    }

    private ImagePattern getAvatarPattern(String gender) {
        String imgPath = (gender != null && gender.equalsIgnoreCase("Kadin"))
                ? "/images/woman.png" : "/images/man.png";
        try {
            URL url = getClass().getResource(imgPath);
            if (url != null) {
                return new ImagePattern(new Image(url.toExternalForm()));
            }
        } catch (Exception e) { }
        return null;
    }

    private void navigateToFriendProfile(ActionEvent event, String targetUser) {
        FriendsProfileController friendCtrl = changeScene(event, "/com.arkadastakibi/friends-profile.fxml", "Arkadaş Profili");

        if (friendCtrl != null) {
            friendCtrl.setArkadasBilgileri(targetUser, this.loggedUser.getUsername());
        }
    }

    @FXML
    public void ProfileGit(ActionEvent event) {
        String kAdi = lblMyUsername.getText().replace("@", "");
        UserProfileController profileCtrl = changeScene(event, "/com.arkadastakibi/profil-page.fxml", "Profilim");
        if (profileCtrl != null) {
            profileCtrl.setKullaniciAdi(kAdi);
        }
    }

    public void CikisYap(ActionEvent event) {
        changeScene(event, "/com.arkadastakibi/login.fxml", "Giriş Yap");
    }
}