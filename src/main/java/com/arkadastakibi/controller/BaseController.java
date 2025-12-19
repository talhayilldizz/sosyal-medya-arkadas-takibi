package com.arkadastakibi.controller;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

// ABSTRACT CLASS: Bu sınıftan nesne üretilemez, sadece miras alınabilir.
public abstract class BaseController {

    // ENCAPSULATION: Bu metoda sadece miras alan sınıflar (protected) erişebilir.
    // OVERLOADING 1: Sadece gidilecek yolu verince çalışır.
    protected void changeScene(Event event, String fxmlPath) {
        changeScene(event, fxmlPath, null); // Diğer metodun çağrılması
    }

    //Generic
    // OVERLOADING 2: Hem yol hem de başlık verince çalışır.
    protected <T> T changeScene(Event event, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));

            if (title != null) {
                stage.setTitle(title);
            }
            stage.show();

            // Controller'ı geri döndürürüz, böylece veri aktarımı yapabiliriz.
            return loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Sayfa geçiş hatası: " + fxmlPath);
            return null;
        }
    }

    // Ortak uyarı mekanizması
    protected void showMessage(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}