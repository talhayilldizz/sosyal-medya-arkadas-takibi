package com.arkadastakibi.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

public class MainPageController {

    @FXML
    private TextField txtSearch;

    @FXML
    private Label lblMyName;

    @FXML
    private Label lblMyUsername;

    @FXML
    private Circle imgAvatar;

    public void setKullaniciBilgileri(String name, String username) {
        lblMyName.setText(name);
        lblMyUsername.setText(username);
    }

    @FXML
    public void ProfileGit(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.arkadastakibi/profil-page.fxml"));
            Parent root = loader.load();

            UserProfileController profileController = loader.getController();

            //Şu anki ana sayfada yazan kullanıcı adını al
            String mevcutKullaniciAdi = lblMyUsername.getText();

            if (mevcutKullaniciAdi != null && mevcutKullaniciAdi.startsWith("@")) {
                mevcutKullaniciAdi = mevcutKullaniciAdi.substring(1); // @ işaretini atar
            } else if (mevcutKullaniciAdi == null) {
                mevcutKullaniciAdi = "hata";
            }

            //Veriyi profil sayfasına gönder
            profileController.setKullaniciAdi(mevcutKullaniciAdi);

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Hata: Profil sayfası yüklenemedi! Dosya yolu veya ismi hatalı olabilir.");
        }
    }

    public void CikisYap(ActionEvent event) {
        try {
            FXMLLoader loader= new FXMLLoader(getClass().getResource("/com.arkadastakibi/login.fxml"));
            Parent root=loader.load();
            Stage stage=(Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene=new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Çıkış Yapılamadı!");
        }
    }
}