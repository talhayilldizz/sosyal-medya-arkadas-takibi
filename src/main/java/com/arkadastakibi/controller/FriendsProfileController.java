package com.arkadastakibi.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;


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

    private String targetUsername; // Görüntülenen arkadaşın kullanıcı adı
    private String myUsername;     // Giriş yapmış olan BENİM kullanıcı adım

    // Main Page'den buraya veri gönderirken bu metodu kullanacağız
    public void setArkadasBilgileri(String targetUser, String currentUser) {
        this.targetUsername = targetUser;
        this.myUsername = currentUser;

        arkadasVerileriniYukle();
    }

    private void arkadasVerileriniYukle() {
        // Buradaki mantık UserProfileController ile aynı
        // Sadece ek olarak "Ortak Takipçi" sayısını hesaplayıp lblMutualFollowers'a yazdırabilirsin.
        // Şimdilik temel verileri ve resmi yükleyelim:

        String filePath = "users.json";
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray usersArray = new JSONArray(content);

            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject user = usersArray.getJSONObject(i);

                if (user.getString("username").equals(this.targetUsername)) {
                    lblName.setText(user.getString("firstName") + " " + user.getString("lastName"));
                    lblEmail.setText("@" + user.getString("username"));

                    // Bio
                    if (user.has("bio") && !user.isNull("bio")) lblBio.setText(user.getString("bio"));
                    else lblBio.setText("Biyografi yok.");

                    // Takipçi sayıları
                    lblFollowers.setText(String.valueOf(user.optInt("followers", 0)));
                    lblFollowing.setText(String.valueOf(user.optInt("following", 0)));

                    // ortak takipçi sayısı (şuanlık 0 hesaplamasının yazılması gerek)
                    lblMutualFollowers.setText("0");

                    // resim yükleme
                    String gender = user.optString("gender", "Erkek");
                    String imagePath = gender.equalsIgnoreCase("Kadin") ? "/images/woman.png" : "/images/man.png";

                    if (getClass().getResourceAsStream(imagePath) != null) {
                        imgProfile.setImage(new Image(getClass().getResourceAsStream(imagePath)));
                    }

                    // sosyal medya
                    // checkSocialLink metodu
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void AnaSayfayaGeriDon(ActionEvent event) {
        MainPageController mainCtrl = changeScene(event, "/com.arkadastakibi/main-page.fxml", "Ana Sayfa");

        if(mainCtrl != null) {
            // JSON'dan BENİM (Giriş yapan kişi) bilgilerimi bulup tekrar yüklüyoruz.
            // Çünkü myUsername değişkeninde benim kullanıcı adım kayıtlıydı.

            String filePath = "users.json";
            try {
                String content = new String(Files.readAllBytes(Paths.get(filePath)));
                JSONArray usersArray = new JSONArray(content);

                for (int i = 0; i < usersArray.length(); i++) {
                    JSONObject user = usersArray.getJSONObject(i);
                    if(user.getString("username").equals(this.myUsername)) {

                        String fullName = user.getString("firstName") + " " + user.getString("lastName");
                        String gender = user.optString("gender", "Erkek");

                        mainCtrl.setKullaniciBilgileri(fullName, this.myUsername, gender);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}