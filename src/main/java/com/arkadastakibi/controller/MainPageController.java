package com.arkadastakibi.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

public class MainPageController {

    @FXML
    private Label lblAccountName;

    public void setKullaniciBilgileri(String username) {
        lblAccountName.setText(username);
        System.out.println(username + " kullanıcısı ana sayfaya giriş yaptı.");
    }

    public void ProfileGit(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.arkadastakibi/profil-page.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Profil sayfası yüklenemedi! Dosya yolunu kontrol et.");
        }
    }
}