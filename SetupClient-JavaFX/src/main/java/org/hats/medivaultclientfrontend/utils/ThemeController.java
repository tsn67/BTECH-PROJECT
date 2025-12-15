package org.hats.medivaultclientfrontend.utils;

import atlantafx.base.theme.*;
import javafx.application.Application;
import javafx.scene.control.ChoiceBox;

public class ThemeController {
    final private ChoiceBox<String> themeChoiceButton;

    public ThemeController(ChoiceBox<String> themeChoiceButton) {
        this.themeChoiceButton = themeChoiceButton;
    }

    private void handleThemeSelection(String theme) {
        if (theme.equals("Dark")) {
            Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());
        } else if (theme.equals("Light")) {
            Application.setUserAgentStylesheet(new CupertinoLight().getUserAgentStylesheet());
        }
    }

    public void initializeThemeSettings() {
        themeChoiceButton.getItems().addAll("Dark", "Light");
        themeChoiceButton.getSelectionModel().select(0);

        themeChoiceButton
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        handleThemeSelection(newVal);
                    }
                });
    }
}
