package org.hats.medivaultclientfrontend.view_3;

import javafx.scene.control.Button;

public class EndTask extends Task {

    private final Button nextButton;

    public EndTask(Button nextButton) {
        this.nextButton = nextButton;
        super(null, null, null, null, null);
    }

    @Override
    public void runTask() {
        nextButton.setDisable(false);
    }
}
