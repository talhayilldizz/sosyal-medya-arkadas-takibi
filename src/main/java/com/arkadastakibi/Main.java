package com.arkadastakibi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try {
            var url = getClass().getResource("/com.arkadastakibi/login.fxml");
            FXMLLoader loader = new FXMLLoader(url);

            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Sosyal Medya Arkadaş Takibi");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}