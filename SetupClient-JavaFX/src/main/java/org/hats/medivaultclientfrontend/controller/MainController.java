package org.hats.medivaultclientfrontend.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import org.hats.medivaultclientfrontend.animation.ViewAnimator;
import org.hats.medivaultclientfrontend.view_1.EnvironmentSetupController;
import org.hats.medivaultclientfrontend.view_2.View2Controller;
import org.hats.medivaultclientfrontend.view_3.View3Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    final String baseFxmlPath = "/org/hats/medivaultclientfrontend/";
    final String envSetupFxml = "View_1.fxml";

    @FXML
    private AnchorPane mainView;

    public void switchToNextView(ViewController object) {
        try {
            if (object instanceof EnvironmentSetupController) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/hats/medivaultclientfrontend/View_2.fxml"));
                AnchorPane newView = loader.load();
                ((ViewController)loader.getController()).setMainController(this);

                ViewAnimator.switchView(mainView, newView);
            } else if (object instanceof View2Controller) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/hats/medivaultclientfrontend/View_3.fxml"));
                AnchorPane newView = loader.load();
                ((ViewController)loader.getController()).setMainController(this);

                ViewAnimator.switchView(mainView, newView);
            } else if (object instanceof View3Controller) {
                System.out.println("Calling view4!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFirstView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(baseFxmlPath + envSetupFxml));
            AnchorPane envSetupAnchorPane = loader.load();
            EnvironmentSetupController envSetupController = loader.getController();
            envSetupController.setMainController(this);

            mainView.getChildren().clear();
            mainView.getChildren().addAll(envSetupAnchorPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadFirstView();
    }

}
