package com.arkadastakibi.controller;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

// ABSTRACT CLASS: Bu sınıftan nesne üretilemez, sadece miras alınabilir.
public abstract class BaseController {
//-----------------------EKRANLA İLGİLİ FONKSİYONLAR(MESAJLAR, EKRAN DEĞİŞİMİ VB.)-------------------

    // ENCAPSULATION: Bu metoda sadece miras alan sınıflar (protected) erişebilir.
    // OVERLOADING 1: Sadece gidilecek yolu verince çalışır.
    protected void changeScene(Event event, String fxmlPath) {
        changeScene(event, fxmlPath, null); // Diğer metodun çağrılması
    }

    //Generic
    //OVERLOADING 2: Hem yol hem de başlık verince çalışır.
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

            //Controller'ı geri döndürürüz, böylece veri aktarımı yapabiliriz.
            return loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Sayfa geçiş hatası: " + fxmlPath);
            return null;
        }
    }

    //Ortak uyarı mekanizması
    protected void showMessage(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
 //-------------------------TAKİPÇİLERLE İLGİLİ FONKSİYONLAR----------------------------------------
    protected JSONObject findUserByUsername(String username) {
        String filePath = "users.json";
        File file = new File(filePath);

        if (!file.exists()) {
            return null;
        }

        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray usersArray = new JSONArray(content);

            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject user = usersArray.getJSONObject(i);
                if (user.getString("username").equals(username)) {
                    return user;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Kullanıcı bulunamadı
    }

    /**
     * Bir kullanıcının diğerini takip edip etmediğini kontrol eder.
     * @param myUsername Ben (Kontrol eden)
     * @param targetUsername Hedef (Kontrol edilen)
     * @return Takip ediyorsa true, etmiyorsa false
     */
    protected boolean isFollowing(String myUsername, String targetUsername) {
        JSONObject me = findUserByUsername(myUsername);

        if (me != null) {
            // Takip ettiklerimin listesini al (Yoksa boş liste say)
            JSONArray myFollowingList = me.optJSONArray("followingList");
            if (myFollowingList == null) return false;

            // Listeyi tara
            for (int i = 0; i < myFollowingList.length(); i++) {
                if (myFollowingList.getString(i).equals(targetUsername)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * İki kullanıcı arasındaki ortak takipçi sayısını hesaplar.
     * (İkisini de takip eden kişilerin sayısı)
     */
    protected int getMutualFollowersCount(String user1, String user2) {
        JSONObject u1 = findUserByUsername(user1);
        JSONObject u2 = findUserByUsername(user2);

        if (u1 == null || u2 == null) return 0;

        // İkisinin de takipçi listesini al
        JSONArray list1 = u1.optJSONArray("followersList");
        JSONArray list2 = u2.optJSONArray("followersList");

        if (list1 == null || list2 == null) return 0;

        int count = 0;

        // Kesişim kümesini bul (Ortak eleman sayısı)
        for (int i = 0; i < list1.length(); i++) {
            String followerU1 = list1.getString(i);

            for (int j = 0; j < list2.length(); j++) {
                if (followerU1.equals(list2.getString(j))) {
                    count++;
                    break; // Bulduk, diğerine geç
                }
            }
        }
        return count;
    }
}
