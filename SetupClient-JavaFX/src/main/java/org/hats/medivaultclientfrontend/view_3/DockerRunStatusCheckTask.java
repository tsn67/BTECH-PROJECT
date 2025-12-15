package org.hats.medivaultclientfrontend.view_3;

import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import org.hats.medivaultclientfrontend.utils.ErrorExitDialog;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DockerRunStatusCheckTask extends Task {

    public DockerRunStatusCheckTask(ScrollPane scrollArea, String taskName, String taskStartingDialog, String completedDialog, Task nextTask) {
        super(scrollArea, taskName, taskStartingDialog, completedDialog, nextTask);
    }

    @Override
    public void runTask() {
        Thread dockerCheckThread = new Thread(() -> {
            try {
                // Delay inside worker thread
                Thread.sleep(2000); // 2 seconds

                ProcessBuilder processBuilder =
                        new ProcessBuilder("docker", "info");

                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();

                StringBuilder output = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()))) {

                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.append(line).append("\n");
                    }
                }

                int exitCode = process.waitFor();

                Platform.runLater(() -> {
                    appendAndScroll(output.toString());

                    if (exitCode == 0) {
                        appendAndScroll("Docker is running.\n");
                        appendAndScroll(completedDialog + "\n");
                        runNextTask();
                    } else {
                        appendAndScroll("Docker is NOT running.\n");
                        ErrorExitDialog.showAndExit("Docker status check error", "Docker is not running.\n Please check your Docker staus and try again.");
                    }
                });

            } catch (Exception e) {
                Platform.runLater(() ->
                        appendAndScroll(
                                "Docker check failed: " + e.getMessage() + "\n"
                        )
                );
            }
        });

        dockerCheckThread.setDaemon(true);
        dockerCheckThread.start();
    }
}
