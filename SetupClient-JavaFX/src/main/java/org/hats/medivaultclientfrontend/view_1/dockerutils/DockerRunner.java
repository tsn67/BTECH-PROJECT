package org.hats.medivaultclientfrontend.view_1.dockerutils;

import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;
import org.hats.medivaultclientfrontend.utils.PlatformUrls;
import org.hats.medivaultclientfrontend.view_1.EnvChainedTasks.ChainedTask;

public class DockerRunner extends ChainedTask {

    private final ProgressIndicator runLoader;
    private final Button runButton;
    private final Label statusLabel;
    private final Pane dockerStatusBar;

    public DockerRunner(Label statusLabel, ProgressIndicator runLoader, Button runButton, Pane dockerStatusBar) {
        this.statusLabel = statusLabel;
        this.runLoader = runLoader;
        this.runButton = runButton;
        this.dockerStatusBar = dockerStatusBar;

        runButton.setVisible(false);
        runLoader.setVisible(false);
        statusLabel.setVisible(false);
        dockerStatusBar.setVisible(false);

        runButton.setOnAction(e -> startDocker());
    }

    public void readyForStart() {
        runLoader.setVisible(false);
        statusLabel.getStyleClass().add("warning");
        statusLabel.setText("Docker is not running. please start it!");
        runButton.setVisible(true);
    }


    public void pollUntilDockerRunning() {

        Task<Void> pollTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                String status = "NOT_RUNNING";

                while (status.equals("NOT_RUNNING")) {

                    // Create a FRESH task each time
                    Process infoCheck = Runtime.getRuntime().exec("docker info");
                    int exit = infoCheck.waitFor();

                    if (exit == 0) {
                        status = "RUNNING";
                    }

                    Thread.sleep(1000);
                }

                return null;
            }
        };

        pollTask.setOnSucceeded(e -> {

            runButton.setVisible(false);
            runLoader.setVisible(false);
            statusLabel.getStyleClass().remove("warning");
            statusLabel.setStyle("-fx-text-fill: lightgreen;");
            statusLabel.setText("Docker started successfully!");

            dockerStatusBar.setVisible(false);

            if (nextTask != null) {
                nextTask.run();
            }
        });

        pollTask.setOnFailed(e -> {
            statusLabel.setText("Failed to detect Docker.");
            statusLabel.setStyle("-fx-text-fill: lightcoral;");
        });

        new Thread(pollTask).start();
    }

    public void startDocker() {
        Task<?> task = new Task<>() {
            @Override
            protected String call() throws Exception {
                dockerStatusBar.setVisible(true);
                Runtime.getRuntime().exec(
                        PlatformUrls.windowsDockerStartCommand
                );

                return "";
            }
        };

        new Thread(task).start();
        pollUntilDockerRunning();
    }

    Task<String> dockerRunStatusCheckTask = new Task<>() {
        @Override
        protected String call() throws Exception {
            Process infoCheck = Runtime.getRuntime().exec("docker info");
            int exit = infoCheck.waitFor();
            System.out.println(exit);
            if (exit == 0) {
                return "RUNNING";
            } else {
                return "NOT_RUNNING";
            }
        }
    };

    public void detectDockerStatus() {

        dockerRunStatusCheckTask.setOnSucceeded(e -> {
            String result = dockerRunStatusCheckTask.getValue();
            if (result.equals("RUNNING")) {
                runLoader.setVisible(false);
                runButton.setVisible(false);
                statusLabel.setText("Docker is running!");
                statusLabel.setStyle("-fx-text-fill: lightgreen;");

                if (nextTask != null) {
                    nextTask.run();
                }
            } else {
                readyForStart();
            }
        });

        dockerRunStatusCheckTask.setOnFailed(e -> {
            readyForStart();
        });

        new Thread(dockerRunStatusCheckTask).start();
    }

    @Override
    public void run() {
        runLoader.setVisible(true);
        statusLabel.setVisible(true);
        detectDockerStatus();
    }
}
