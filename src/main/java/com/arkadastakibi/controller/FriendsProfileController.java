package com.arkadastakibi.controller;

import com.arkadastakibi.model.Post;
import com.arkadastakibi.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import org.json.JSONArray;
import java.awt.Desktop;
import java.awt.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class FriendsProfileController extends BaseController {

    @FXML private Button btnBack;
    @FXML private Label lblName;
    @FXML private Label lblEmail;
    @FXML private Label lblBio;
    @FXML private Label lblFollowing;
    @FXML private Label lblFollowers;
    @FXML private Label lblMutualFollowers;

    @FXML private ImageView imgProfile;
    @FXML private Button btnFollow;

    @FXML private Button btnInstagram;
    @FXML private Button btnTwitter;
    @FXML private Button btnTiktok;

    @FXML private HBox boxProfileView;
    @FXML private VBox boxListView;
    @FXML private Label lblListTitle;
    @FXML private VBox vboxListContent;


    @FXML private VBox vboxFriendsPosts;
    @FXML private Label lblFriendsPostTitle;

    private String targetUsername; //Arkadaş
    private String myUsername;     //Ben

    private User targetUserObj; //Hedef Kullanıcı Nesnesi
    private User myUserObj;     //Benim Nesnem



    public void setArkadasBilgileri(String targetUser, String currentUser) {
        this.targetUsername = targetUser;
        this.myUsername = currentUser;
        arkadasVerileriniYukle();
    }

    private void arkadasVerileriniYukle() {
        if(boxListView != null) boxListView.setVisible(false);
        if(boxProfileView != null) boxProfileView.setVisible(true);

        if(this.app == null || this.app.Users == null) return;

        this.targetUserObj = null;
        this.myUserObj = null;

        for (User u : this.app.Users) {
            if (u.getUsername().equals(this.targetUsername)) {
                this.targetUserObj = u;
            }
            if (u.getUsername().equals(this.myUsername)) {
                this.myUserObj = u;
            }
        }

        if (this.targetUserObj == null) return;

        //Verileri Ekrana yaz
        String fullName = targetUserObj.getFirstName() + " " + targetUserObj.getLastName();
        lblName.setText(fullName);
        lblEmail.setText("@" + targetUserObj.getUsername());

        String bio = targetUserObj.getBio();
        lblBio.setText((bio == null || bio.isEmpty()) ? "Biyografi yok." : bio);

        //Veritabanındaki ID listesinin uzunluğunu alıyoruz
        lblFollowers.setText(String.valueOf(targetUserObj.getFollowerUser().size()));
        lblFollowing.setText(String.valueOf(targetUserObj.getFollowingUser().size()));

        //Ortak Takipçi Sayısı
        lblMutualFollowers.setText(String.valueOf(calculateMutualFollowersCount()));

        //Avatar
        String gender = targetUserObj.getGender();
        String imagePath = (gender != null && gender.equalsIgnoreCase("Kadin"))
                ? "/images/woman.png" : "/images/man.png";
        try {
            if (getClass().getResourceAsStream(imagePath) != null) {
                imgProfile.setImage(new Image(getClass().getResourceAsStream(imagePath)));
            }
        } catch (Exception e) { }

        //Sosyal Medya Link Kontrolü
        checkSocialLink(targetUserObj.getInstagramLink(), btnInstagram);
        checkSocialLink(targetUserObj.getTwitterLink(), btnTwitter);
        checkSocialLink(targetUserObj.getTiktokLink(), btnTiktok);

        //Takip Et Butonu Durumu
        updateFollowButtonState();

        friendsPost();
    }


    private void openLinkInBrowser(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void InstagramClicked(){
        openLinkInBrowser(this.targetUserObj.getInstagramLink());
    }

    @FXML
    private void TwitterClicked(){
        openLinkInBrowser(this.targetUserObj.getTwitterLink());
    }

    @FXML
    private void TiktokClicked(){
        openLinkInBrowser(this.targetUserObj.getTiktokLink());
    }

    private void checkSocialLink(String link, Button btn) {
        if (link == null || link.isEmpty()) {
            btn.setDisable(true);
            btn.setOpacity(0.5);
        } else {
            btn.setDisable(false);
            btn.setOpacity(1.0);
        }
    }

    //Ortak Takipçi Sayısı
    private int calculateMutualFollowersCount() {
        if (this.targetUserObj == null || this.myUserObj == null) return 0;

        ArrayList<Integer> targetFollowers = this.targetUserObj.getFollowerUser();
        ArrayList<Integer> myFollowers = this.myUserObj.getFollowerUser();

        int count = 0;
        for (Integer id : targetFollowers) {
            if (myFollowers.contains(id)) {
                count++;
            }
        }
        return count;
    }

    private void updateFollowButtonState() {
        if (this.myUserObj != null && this.targetUserObj != null) {
            //Listede ID var mı kontrolü
            if (this.myUserObj.getFollowingUser().contains(this.targetUserObj.getId())) {
                //zaten takip ediyorsam takibi bırak
                btnFollow.setText("Takibi Bırak");
                btnFollow.setStyle("-fx-background-color: #ffebee; -fx-text-fill: #c62828; -fx-background-radius: 10; -fx-cursor: hand; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, rgba(198,40,40,0.2), 10, 0, 0, 5);");

                //Takibi bırak butonuna basınca toggleFollow çalışacak
                btnFollow.setOnAction(this::toggleFollow);
            } else {
                //Takip etmiyorsam takip et
                btnFollow.setText("Takip Et");
                btnFollow.setStyle("-fx-background-color: #1e88e5; -fx-text-fill: white; -fx-background-radius: 10; -fx-cursor: hand; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, rgba(30,136,229,0.3), 10, 0, 0, 5);");

                //Takip et butonuna basınca toggleFollow çalışacak
                btnFollow.setOnAction(this::toggleFollow);
            }
        }
    }


    @FXML
    public void showFollowersList(MouseEvent event) {
        lblListTitle.setText(targetUserObj.getFirstName() + " kişisinin Takipçileri");
        loadUsersToList("followers");
        switchView(true);
    }

    @FXML
    public void showFollowingList(MouseEvent event) {
        lblListTitle.setText(targetUserObj.getFirstName() + " kişisinin Takip Ettikleri");
        loadUsersToList("following");
        switchView(true);
    }

    @FXML
    public void showMutualFollowersList(MouseEvent event) {
        lblListTitle.setText("Ortak Takipçiler (İkimizi de Takip Edenler)");
        loadUsersToList("mutual");
        switchView(true);
    }

    @FXML
    public void closeListView(ActionEvent event) {
        switchView(false);
    }

    private void switchView(boolean showList) {
        boxListView.setVisible(showList);
        boxProfileView.setVisible(!showList);
    }

    private void loadUsersToList(String type) {
        vboxListContent.getChildren().clear();
        if (this.targetUserObj == null || this.app.Users == null) return;

        List<Integer> idsToLoad = new ArrayList<>();

        if (type.equals("followers")) {
            //Arkadaşın takipçilerinin ID listesi
            idsToLoad = this.targetUserObj.getFollowerUser();
        }
        else if (type.equals("following")) {
            //Arkadaşın takip ettiklerinin ID listesi
            idsToLoad = this.targetUserObj.getFollowingUser();
        }
        else if (type.equals("mutual")) {
            //Ortak takipçileri bulmak için ID'leri hesaplıyoruz
            if (this.myUserObj != null) {
                ArrayList<Integer> targetFollowers = this.targetUserObj.getFollowerUser();
                ArrayList<Integer> myFollowers = this.myUserObj.getFollowerUser();

                for (Integer id : targetFollowers) {
                    if (myFollowers.contains(id)) {
                        idsToLoad.add(id); //Kesişen ID'yi listeye ekle
                    }
                }
            }
        }

        //ID listesindeki her bir ID için gerçek User nesnesini bul ve ekrana bas
        for (Integer id : idsToLoad) {
            User user = findUserById(id);
            if (user != null) {
                vboxListContent.getChildren().add(createUserRow(user));
            }
        }

        if (idsToLoad.isEmpty()) {
            Label lblEmpty = new Label("Burada kimse yok.");
            lblEmpty.setStyle("-fx-text-fill: #868686; -fx-padding: 10;");
            vboxListContent.getChildren().add(lblEmpty);
        }
    }

    private User findUserById(int id) {
        for (User u : this.app.Users) {
            if (u.getId() == id) {
                return u;
            }
        }
        return null;
    }


    private HBox createUserRow(User user) {
        String uName = user.getUsername();
        String fullName = user.getFirstName() + " " + user.getLastName();
        String gender = user.getGender();

        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setSpacing(15);
        row.setStyle("-fx-padding: 10; -fx-border-color: #f0f0f0; -fx-border-width: 0 0 1 0; -fx-background-color: white;");

        //Avatar
        Circle avatar = new Circle(20);
        String imgPath = (gender != null && gender.equalsIgnoreCase("Kadin"))
                ? "/images/woman.png" : "/images/man.png";
        try {
            avatar.setFill(new ImagePattern(new Image(getClass().getResource(imgPath).toExternalForm())));
        } catch (Exception e) { }

        //İsimler
        VBox nameBox = new VBox(2);
        Label lblName = new Label(fullName);
        lblName.setStyle("-fx-font-weight: bold; -fx-text-fill: #212529;");
        Label lblUser = new Label("@" + uName);
        lblUser.setStyle("-fx-text-fill: #868686; -fx-font-size: 12px;");
        nameBox.getChildren().addAll(lblName, lblUser);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        //Git Butonu
        Button btnVisit = new Button("Profili Gör");
        btnVisit.setStyle("-fx-background-color: transparent; -fx-text-fill: #1e88e5; -fx-cursor: hand; -fx-font-weight: bold;");

        //Eğer listedeki kişi şu anki arkadaş ise butonu gizle
        if (uName.equals(this.targetUsername)) {
            btnVisit.setVisible(false);
        }
        //Eğer listedeki kişi BEN isem butonu gizle (Kendi profilime arkadaş üzerinden gitmeyeyim)
        else if (uName.equals(this.myUsername)) {
            btnVisit.setVisible(false);
            Label lblMe = new Label("(Sen)");
            lblMe.setStyle("-fx-text-fill: #868686; -fx-font-style: italic;");
            row.getChildren().add(lblMe);
        }

        btnVisit.setOnAction(e -> {
            //Başka bir arkadaşın profiline geçiş
            setArkadasBilgileri(uName, this.myUsername);
            closeListView(null);
        });

        row.getChildren().addAll(avatar, nameBox, spacer, btnVisit);
        return row;
    }

    @FXML
    void AnaSayfayaGeriDon(ActionEvent event) {
        MainPageController mainCtrl = changeScene(event, "/com.arkadastakibi/main-page.fxml", "Ana Sayfa");

        if(mainCtrl != null && this.myUserObj != null) {
            mainCtrl.setKullaniciBilgileri(
                    this.myUserObj.getFirstName() + " " + this.myUserObj.getLastName(),
                    this.myUserObj.getUsername(),
                    this.myUserObj.getGender()
            );
        }
    }

    @FXML
    void toggleFollow(ActionEvent event) {
        if (this.myUserObj == null || this.targetUserObj == null) return;

        //Takip durumunu kontrol et
        boolean isFollowing = this.myUserObj.getFollowingUser().contains(this.targetUserObj.getId());

        if (isFollowing) {
            //Takibi bırakma işlemi
            this.myUserObj.getFollowingUser().remove(Integer.valueOf(this.targetUserObj.getId()));
            this.targetUserObj.getFollowerUser().remove(Integer.valueOf(this.myUserObj.getId()));

        } else {
            //Takip etme işlemi
            this.myUserObj.getFollowingUser().add(this.targetUserObj.getId());
            this.targetUserObj.getFollowerUser().add(this.myUserObj.getId());

            sendNotificationToUser(
                    this.targetUserObj,            // Kime? (Arkadaşa)
                    this.myUserObj.getUsername(),  // Kimden? (Benden)
                    "seni takip etmeye başladı."   // Mesaj
            );
        }

        updateFollowButtonState();
        updateFollowerCountLabel();

        saveData();
    }
    //Ekrandaki takipçi sayısını anlık güncellemek için
    private void updateFollowerCountLabel() {
        if (targetUserObj != null) {
            lblFollowers.setText(String.valueOf(targetUserObj.getFollowerUser().size()));

            //Eğer ortak takipçi sayısı da değiştiyse güncelle
            lblMutualFollowers.setText(String.valueOf(calculateMutualFollowersCount()));
        }
    }

    //Değişiklikleri kalıcı hale getirmek için JSON'a yazma metodu
    private void saveData() {
        try {
            //Tüm kullanıcı listesini JSON Array formatına çevir
            JSONArray newUsersArray = new JSONArray();
            for (User u : this.app.Users) {
                newUsersArray.put(u.toJSON());
            }

            //DataBase sınıfını kullanarak dosyaya yaz
            com.arkadastakibi.model.DataBase db = new com.arkadastakibi.model.DataBase("users.json");
            db.writeData(newUsersArray);

        } catch (Exception e) {
            System.out.println("Kayıt hatası: " + e.getMessage());
            e.printStackTrace();
        }
    }


    //Kullanıcıların sayfasında kendi postları
    public void friendsPost(){
        vboxFriendsPosts.getChildren().clear();

        if(targetUserObj == null || this.app.Posts==null) return;

        for(Post post : this.app.Posts){
            if(targetUserObj.getId() != post.getUserId()){
                continue;
            }

            lblFriendsPostTitle.setVisible(true);
            VBox postBox = new VBox(10);
            postBox.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 20;");
            postBox.setEffect(new DropShadow(10, Color.rgb(0,0,0,0.05)));

            HBox header = new HBox(10);
            header.setAlignment(Pos.CENTER_LEFT);
            Circle avatar = new Circle(20, Color.web("#e0e0e0"));
            VBox titles = new VBox();
            Label nameLbl = new Label(this.targetUserObj.getUsername());
            nameLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #212529; -fx-font-size: 15px;");
            Label timeLbl = new Label(post.getPostDate().toString());
            timeLbl.setStyle("-fx-text-fill: #868686; -fx-font-size: 12px;");
            titles.getChildren().addAll(nameLbl, timeLbl);
            header.getChildren().addAll(avatar, titles);

            Label contentLbl = new Label(post.getPostContent());
            contentLbl.setWrapText(true);
            contentLbl.setStyle("-fx-text-fill: #212529; -fx-font-size: 14px;");

            postBox.getChildren().addAll(header, contentLbl);

            vboxFriendsPosts.getChildren().add(postBox);
        }

    }
}