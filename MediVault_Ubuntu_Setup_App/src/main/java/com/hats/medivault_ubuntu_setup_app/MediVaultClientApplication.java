package com.hats.medivault_ubuntu_setup_app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MediVaultClientApplication extends Application {
    private final String MAIN_FXML_FILE = "Main.fxml";

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(MAIN_FXML_FILE));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle(ResourceStrings.CLIENT_APP_DISPLAY_NAME);
        stage.setWidth(ResourceDimensions.CLIENT_APP_WIDTH);
        stage.setHeight(ResourceDimensions.CLIENT_APP_HEIGHT);
        stage.setResizable(false);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
