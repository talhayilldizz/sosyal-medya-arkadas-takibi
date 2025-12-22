package com.arkadastakibi.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UserProfileController extends BaseController {

    @FXML private Button btnBack;
    @FXML private Label lblName;
    @FXML private Label lblEmail;
    @FXML private Label lblBio;
    @FXML private Label lblFollowing;
    @FXML private Label lblFollowers;
    @FXML private ImageView imgProfile;

    @FXML private Button btnEditProfile;
    @FXML private Button btnDeleteAccount;
    @FXML private Button btnInstagram;
    @FXML private Button btnTwitter;
    @FXML private Button btnTiktok;

    private String currentUsername; // Şu an profili görüntülenen kişi

    public void setKullaniciAdi(String username) {
        this.currentUsername = username;
        kullaniciVerileriniYukle();
    }

    private void kullaniciVerileriniYukle() {
        String filePath = "users.json";
        File file = new File(filePath);

        if (!file.exists()) return;

        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray usersArray = new JSONArray(content);

            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject user = usersArray.getJSONObject(i);

                if (user.getString("username").equals(this.currentUsername)) {
                    String fullName = user.getString("firstName") + " " + user.getString("lastName");
                    lblName.setText(fullName);
                    lblEmail.setText("@" + user.getString("username"));
                    if (user.has("bio") && !user.isNull("bio")) {
                        lblBio.setText(user.getString("bio"));
                    } else {
                        lblBio.setText("Henüz bir biyografi eklenmemiş.");
                    }

                    int followers = user.has("followers") ? user.getInt("followers") : 0;
                    int following = user.has("following") ? user.getInt("following") : 0;

                    lblFollowers.setText(String.valueOf(followers));
                    lblFollowing.setText(String.valueOf(following));

                    checkSocialLink(user, "instagram", btnInstagram);
                    checkSocialLink(user, "twitter", btnTwitter);
                    checkSocialLink(user, "tiktok", btnTiktok);

                    String gender = "Erkek";

                    if (user.has("gender")) {
                        gender = user.getString("gender");
                    }

                    String imagePath = "";
                    if (gender.equalsIgnoreCase("Kadin")) {
                        imagePath = "/images/woman.png";
                    } else {
                        imagePath = "/images/man.png";
                    }

                    //Resmi yükle
                    try {
                        if (getClass().getResourceAsStream(imagePath) != null) {
                            Image image = new Image(getClass().getResourceAsStream(imagePath));
                            imgProfile.setImage(image);
                        } else {
                            System.out.println("Resim bulunamadı: " + imagePath);
                        }
                    } catch (Exception e) {
                        System.out.println("Resim yüklenirken hata oluştu.");
                    }

                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Profil verileri okunurken hata oluştu.");
        }
    }

    private void checkSocialLink(JSONObject user, String key, Button btn) {
        if (!user.has(key) || user.isNull(key) || user.getString(key).isEmpty()) {
            btn.setDisable(true);
            btn.setOpacity(0.5);
        } else {
            btn.setDisable(false);
            btn.setOpacity(1.0);
        }
    }

    @FXML
    void AnaSayfayaGeriDon(ActionEvent event) {
        MainPageController mainCtrl = changeScene(event, "/com.arkadastakibi/main-page.fxml", "Ana Sayfa");

        // Controller başarıyla geldiyse verileri içine yüklüyoruz
        if (mainCtrl != null) {
            String aranacakKullanici = (this.currentUsername != null) ? this.currentUsername.trim() : "";


            String bulunanAdSoyad = "Misafir Kullanıcı";
            String bulunanCinsiyet = "Erkek";


            String dosyaYolu = "users.json";
            File file = new File(dosyaYolu);

            if (file.exists() && !aranacakKullanici.isEmpty()) {
                try {
                    String content = new String(Files.readAllBytes(Paths.get(dosyaYolu)));
                    JSONArray usersArray = new JSONArray(content);

                    for (int i = 0; i < usersArray.length(); i++) {
                        JSONObject user = usersArray.getJSONObject(i);

                        if (user.getString("username").equals(aranacakKullanici)) {
                            bulunanAdSoyad = user.getString("firstName") + " " + user.getString("lastName");


                            if (user.has("gender")) {
                                bulunanCinsiyet = user.getString("gender");
                            }
                            break;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Dosya okuma hatası: " + e.getMessage());
                }
            }

            // Verileri Ana Sayfa Controller'ına gönderiyoruz
            mainCtrl.setKullaniciBilgileri(bulunanAdSoyad, aranacakKullanici, bulunanCinsiyet);
        }
    }
}