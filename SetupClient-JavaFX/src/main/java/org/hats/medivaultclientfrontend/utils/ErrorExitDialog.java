package org.hats.medivaultclientfrontend.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ErrorExitDialog {

    public static void showAndExit(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
            alert.setTitle(title);
            alert.setHeaderText(null);

            alert.showAndWait();

            Platform.exit();
            System.exit(0);
        });
    }
}
