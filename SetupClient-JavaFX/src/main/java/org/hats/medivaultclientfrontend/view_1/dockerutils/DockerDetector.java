package org.hats.medivaultclientfrontend.view_1.dockerutils;

import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import org.hats.medivaultclientfrontend.view_1.EnvChainedTasks.ChainedTask;
import org.kordamp.ikonli.javafx.FontIcon;

public class DockerDetector extends ChainedTask {
    private final ProgressIndicator loader;
    private final Label statusLabel;
    private final HBox dockerInstallationHbox;
    private final String dockerInstallationCheckCommand = "docker --version";
    private final Button recheckButton = new Button("check again");;

    private static final int START_DELAY = 0;

    private void recheckDockerInstallation() {
        recheckButton.setVisible(false);
        statusLabel.getStyleClass().remove("danger");
        statusLabel.setText("Checking Docker installation...");
        loader.setVisible(true);

        // restart this task (run again)
        run();
    }

    public DockerDetector(ProgressIndicator loader, Label statusLabel, HBox dockerInstallationHbox) {
        this.loader = loader;
        this.statusLabel = statusLabel;
        this.dockerInstallationHbox = dockerInstallationHbox;

        recheckButton.setVisible(false);
        recheckButton.setOnAction(e -> recheckDockerInstallation());
        dockerInstallationHbox.getChildren().add(recheckButton);
    }

    public void checkDockerInstallation() {
        Task<String> task = new Task<>() {

            @Override
            protected String call() throws Exception {
                Thread.sleep(START_DELAY);
                Process versionCheck = Runtime.getRuntime().exec(dockerInstallationCheckCommand);

                if (versionCheck.waitFor() != 0) {
                    return "NOT_INSTALLED";
                }

                return "INSTALLED";
            }
        };

        task.setOnSucceeded(e -> {
            loader.setVisible(false);   // Stop animation

            String result = task.getValue();

            switch (result) {
                case "NOT_INSTALLED":
                    statusLabel.setText("Docker not installed.");
                    statusLabel.getStyleClass().add("danger");
                    recheckButton.setVisible(true);
                    break;

                case "INSTALLED":
                    var fontIcon = new FontIcon();
                    fontIcon.setIconSize(20);

                    dockerInstallationHbox.getChildren().add(fontIcon);
                    statusLabel.setText("Docker installation verified.");
                    statusLabel.setStyle("-fx-text-fill: lightgreen;");
                    if (nextTask != null) {
                        nextTask.run();
                    }
                    break;
            }
        });

        task.setOnFailed(e -> {
            loader.setVisible(false);
            statusLabel.setText("Error checking Docker.");
            statusLabel.setStyle("-fx-text-fill: lightcoral;");
            recheckButton.setVisible(true);
        });

        new Thread(task).start();
    }

    @Override
    public void run() {
        checkDockerInstallation();
    }
}
