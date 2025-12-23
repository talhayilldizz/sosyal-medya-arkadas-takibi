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
import com.arkadastakibi.model.App;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class BaseController {
//-----------------------EKRANLA İLGİLİ FONKSİYONLAR(MESAJLAR, EKRAN DEĞİŞİMİ VB.)-------------------

    protected App app; // - Ortak uygulama nesnesi

    public void setApp(App app) {
        this.app = app;
    }
    // ENCAPSULATION:Bu metoda sadece miras alan sınıflar (protected) erişebilir.
    // OVERLOADING 1:Sadece gidilecek yolu verince çalışır.
    protected void changeScene(Event event, String fxmlPath) {
        changeScene(event, fxmlPath, null);
    }

    //Generic
    //OVERLOADING 2: Hem yol hem de başlık verince çalışır.
    protected <T> T changeScene(Event event, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            T controller = (T) loader.getController();

            if (controller instanceof BaseController) {
                ((BaseController) controller).setApp(this.app);
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));

            if (title != null) {
                stage.setTitle(title);
            }
            stage.show();

            return controller;

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
        return null; //Kullanıcı bulunamadı
    }

    protected boolean isFollowing(String myUsername, String targetUsername) {
        JSONObject me = findUserByUsername(myUsername);

        if (me != null) {
            //Takip ettiklerimin listesini al (Yoksa boş liste say)
            JSONArray myFollowingList = me.optJSONArray("followingList");
            if (myFollowingList == null) return false;

            //Listeyi tara
            for (int i = 0; i < myFollowingList.length(); i++) {
                if (myFollowingList.getString(i).equals(targetUsername)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected int getMutualFollowersCount(String user1, String user2) {
        JSONObject u1 = findUserByUsername(user1);
        JSONObject u2 = findUserByUsername(user2);

        if (u1 == null || u2 == null) return 0;

        //İkisinin de takipçi listesini al
        JSONArray list1 = u1.optJSONArray("followersList");
        JSONArray list2 = u2.optJSONArray("followersList");

        if (list1 == null || list2 == null) return 0;

        int count = 0;

        //Kesişim kümesini bul
        for (int i = 0; i < list1.length(); i++) {
            String followerU1 = list1.getString(i);

            for (int j = 0; j < list2.length(); j++) {
                if (followerU1.equals(list2.getString(j))) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    //App sınıfındaki update() metodunu çağırır.
    protected void saveAllData() {
        if (this.app != null) {
            this.app.update(); // SENİN APP SINIFINDAKİ METODU KULLANIYORUZ
        }
    }

    //BİLDİRİM GÖNDERME METODU
    protected void sendNotificationToUser(com.arkadastakibi.model.User targetUser, String senderName, String message) {
        if (targetUser == null) return;

        //Yeni bildirim oluştur
        com.arkadastakibi.model.Notification notif = new com.arkadastakibi.model.Notification(senderName, message);

        //Hedef kişinin listesinin en başına ekle
        targetUser.getNotifications().add(0, notif);

        saveAllData();
    }
}
