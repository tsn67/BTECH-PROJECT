package org.hats.medivaultclientfrontend;

import atlantafx.base.theme.CupertinoDark;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MedivaultApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        // set default dark theme
        Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());

        stage.setTitle("Medivault Admin");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
