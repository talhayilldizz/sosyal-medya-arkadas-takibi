package com.arkadastakibi.controller;

import com.arkadastakibi.model.Notification;
import com.arkadastakibi.model.User;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
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

        //Sayfa ilk açıldığında postları yükle
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

    //Ana Sayfa (Feed) Yükleme Metodu
    @FXML
    public void showHomeFeed(MouseEvent event) {
        loadHomeFeed();
    }

    private void loadHomeFeed() {
        vboxCenterContent.getChildren().clear();

        VBox shareBox = new VBox(10);
        shareBox.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 15;");
        shareBox.setEffect(new DropShadow(10, Color.rgb(0,0,0,0.1)));

        Label lblThink = new Label("Ne düşünüyorsun?");
        lblThink.setStyle("-fx-font-weight: bold; -fx-text-fill: #6c757d; -fx-font-size: 14px;");

        TextArea txtContent = new TextArea();
        txtContent.setPromptText("Buraya bir şeyler yaz...");
        txtContent.setPrefHeight(80);
        txtContent.setWrapText(true);
        txtContent.setStyle("-fx-background-color: transparent; -fx-border-color: #dee2e6; -fx-border-radius: 5; -fx-background-radius: 5;");

        HBox btnBox = new HBox();
        btnBox.setAlignment(Pos.CENTER_RIGHT);
        Button btnShare = new Button("Paylaş");
        btnShare.setStyle("-fx-background-color: #1e88e5; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-weight: bold; -fx-cursor: hand;");
        btnShare.setPadding(new Insets(8, 20, 8, 20));
        btnBox.getChildren().add(btnShare);

        shareBox.getChildren().addAll(lblThink, txtContent, btnBox);
        vboxCenterContent.getChildren().add(shareBox);

        vboxCenterContent.getChildren().add(createPostView("Mehmet Demir", "2 saat önce", "proje", 12));
        vboxCenterContent.getChildren().add(createPostView("Ayşe Çelik", "5 saat önce", "javafx ödevi bitiyor", 45));
    }

    // Bildirimler Sayfası Metodu
    @FXML
    public void showNotifications(ActionEvent event) {
        vboxCenterContent.getChildren().clear();

        HBox headerBox = new HBox(15);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(0, 0, 20, 0));

        Button btnBack = new Button("← Geri");
        btnBack.setStyle("-fx-background-color: white; -fx-text-fill: #1e88e5; -fx-font-weight: bold; " +
                "-fx-font-size: 13px; -fx-cursor: hand; -fx-border-color: #1e88e5; " +
                "-fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 5 15 5 15;");

        // Geri Dön -> Ana Sayfayı Yükle
        btnBack.setOnAction(e -> loadHomeFeed());

        Label lblTitle = new Label("Bildirimler");
        lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #212529;");

        headerBox.getChildren().addAll(btnBack, lblTitle);
        vboxCenterContent.getChildren().add(headerBox);

        //Bildirim Listesi
        if (this.loggedUser != null && !this.loggedUser.getNotifications().isEmpty()) {

            for (Notification notif : this.loggedUser.getNotifications()) {
                vboxCenterContent.getChildren().add(createNotificationItem(
                        notif.getSenderUsername(),
                        notif.getMessage(),
                        notif.getTime()
                ));
            }

        } else {
            //Hiç bildirim yoksa mesaj göster
            Label lblEmpty = new Label("Henüz yeni bir bildirim yok.");
            lblEmpty.setStyle("-fx-text-fill: #868686; -fx-font-size: 14px; -fx-padding: 20;");
            vboxCenterContent.getChildren().add(lblEmpty);
        }
    }

    private VBox createPostView(String fullname, String time, String content, int likeCount) {
        VBox postBox = new VBox(10);
        postBox.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 20;");
        postBox.setEffect(new DropShadow(10, Color.rgb(0,0,0,0.05)));

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Circle avatar = new Circle(20, Color.web("#e0e0e0"));
        VBox titles = new VBox();
        Label nameLbl = new Label(fullname);
        nameLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #212529; -fx-font-size: 15px;");
        Label timeLbl = new Label(time);
        timeLbl.setStyle("-fx-text-fill: #868686; -fx-font-size: 12px;");
        titles.getChildren().addAll(nameLbl, timeLbl);
        header.getChildren().addAll(avatar, titles);

        Label contentLbl = new Label(content);
        contentLbl.setWrapText(true);
        contentLbl.setStyle("-fx-text-fill: #212529; -fx-font-size: 14px;");

        HBox actions = new HBox(20);
        Button btnLike = new Button("❤️ Beğen (" + likeCount + ")");
        btnLike.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-text-fill: #6c757d;");
        Button btnComment = new Button("💬 Yorum Yap");
        btnComment.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-text-fill: #6c757d;");
        actions.getChildren().addAll(btnLike, btnComment);

        postBox.getChildren().addAll(header, contentLbl, new Separator(), actions);
        return postBox;
    }

    private HBox createNotificationItem(String user, String actionText, String time) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 0);");

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
        nameLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #212529;");
        Label actionLbl = new Label(actionText);
        actionLbl.setStyle("-fx-text-fill: #495057;");
        msgBox.getChildren().addAll(nameLbl, actionLbl);

        Label timeLbl = new Label(time);
        timeLbl.setStyle("-fx-text-fill: #868686; -fx-font-size: 11px;");

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
        row.setStyle("-fx-cursor: hand; -fx-padding: 8; -fx-border-color: #f0f0f0; -fx-border-width: 0 0 1 0;");

        Circle avatar = new Circle(18);
        avatar.setFill(getAvatarPattern(gender));

        VBox nameBox = new VBox();
        nameBox.setAlignment(Pos.CENTER_LEFT);
        Label nameLbl = new Label(name);
        nameLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #212529; -fx-font-size: 13px;");
        Label userLbl = new Label("@" + uName);
        userLbl.setStyle("-fx-text-fill: #868686; -fx-font-size: 11px;");
        nameBox.getChildren().addAll(nameLbl, userLbl);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnGit = new Button("Git >");
        btnGit.setStyle("-fx-background-color: transparent; -fx-text-fill: #1e88e5; -fx-font-weight: bold; -fx-cursor: hand; -fx-font-size: 11px;");
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