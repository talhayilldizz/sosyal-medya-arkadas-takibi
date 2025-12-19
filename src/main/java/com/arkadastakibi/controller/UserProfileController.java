package com.arkadastakibi.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UserProfileController {

    @FXML private Button btnBack;
    @FXML private Label lblName;
    @FXML private Label lblEmail;
    @FXML private Label lblBio;
    @FXML private Label lblFollowing;
    @FXML private Label lblFollowers;

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
            btn.setDisable(true); //Link yoksa tıklanamaz yapar
            btn.setOpacity(0.5);  //Rengini soldurur
        } else {
            btn.setDisable(false);
            btn.setOpacity(1.0);
            //Tıklanınca tarayıcıda açma kodu
        }
    }

    @FXML
    void AnaSayfayaGeriDon(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.arkadastakibi/main-page.fxml"));
            Parent root = loader.load();
            MainPageController mainController = loader.getController();

            String aranacakKullanici = (this.currentUsername != null) ? this.currentUsername.trim() : "";
            String dosyaYolu = "users.json";
            File file = new File(dosyaYolu);

            //Varsayılan
            String bulunanAdSoyad = "Misafir Kullanıcı";

            if (file.exists() && !aranacakKullanici.isEmpty()) {
                try {
                    String content = new String(Files.readAllBytes(Paths.get(dosyaYolu)));
                    JSONArray usersArray = new JSONArray(content);

                    for (int i = 0; i < usersArray.length(); i++) {
                        JSONObject user = usersArray.getJSONObject(i);

                        //jsondaki kullanıcı adını alıyoruz
                        String jsonUsername = user.getString("username");

                        //eşleşme Kontrolü
                        if (jsonUsername.equals(aranacakKullanici)) {
                            bulunanAdSoyad = user.getString("firstName") + " " + user.getString("lastName");
                            break;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Dosya okuma hatası: " + e.getMessage());
                }
            } else {
                System.out.println("UYARI: users.json bulunamadı veya kullanıcı adı boş!");
            }

            mainController.setKullaniciBilgileri(bulunanAdSoyad, aranacakKullanici);

            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Sayfa geçiş hatası!");
        }
    }
}