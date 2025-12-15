package org.hats.medivaultclientfrontend.view_2;

import javafx.scene.control.Button;

public class EndTask extends DownloadTask {
    private final Button nextButton;

    public EndTask(Button nextButton) {
        super(null);
        this.nextButton = nextButton;
    }

    @Override
    public void download() {
        nextButton.setDisable(false);
        // nothing to download (end task) just enable the button
    }
}
