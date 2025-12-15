package org.hats.medivaultclientfrontend.controller;

public class ViewController {
    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void nextView() {
        mainController.switchToNextView(this);
    }
}
