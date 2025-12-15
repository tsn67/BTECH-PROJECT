package org.hats.medivaultclientfrontend.view_3;

import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import org.hats.medivaultclientfrontend.utils.ErrorExitDialog;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DownloadFileCheck extends Task {

    private final String ipfsFileName = "ipfs-client-v-1.tar";
    private final String etherFileName = "ether-client-v-1.tar";

    public DownloadFileCheck(ScrollPane scrollArea, String taskName, String taskStartingDialog, String completedDialog, Task nextTask) {
        super(scrollArea, taskName, taskStartingDialog, completedDialog, nextTask);
    }

    @Override
    public void runTask() {

        Thread fileCheckThread = new Thread(() -> {
            try {
                // Optional delay (does NOT block UI)
                Thread.sleep(1000);

                // JavaFX / JVM working directory (application root)
                Path appRoot = Paths.get(System.getProperty("user.dir"));
                Path downloadsDir = appRoot.resolve("downloads");

                Path etherFile = downloadsDir.resolve(etherFileName);
                Path ifpsFile  = downloadsDir.resolve(ipfsFileName);

                boolean downloadsExists = Files.exists(downloadsDir)
                        && Files.isDirectory(downloadsDir);

                boolean etherExists = Files.exists(etherFile);
                boolean ifpsExists  = Files.exists(ifpsFile);

                Platform.runLater(() -> {
                    appendAndScroll("Checking downloaded client files...\n");
                    appendAndScroll("Path: " + downloadsDir.toAbsolutePath() + "\n");

                    if (!downloadsExists) {
                        appendAndScroll("downloads/ directory NOT FOUND\n");
                        return;
                    }

                    appendAndScroll(
                            "ether-client-v-1.tar : " +
                                    (etherExists ? "FOUND\n" : "NOT FOUND\n")
                    );

                    appendAndScroll(
                            "ifps-client-v-1.tar  : " +
                                    (ifpsExists ? "FOUND\n" : "NOT FOUND\n")
                    );

                    if (etherExists && ifpsExists) {
                        appendAndScroll("All required files are present.\n");
                        runNextTask();
                    } else {
                        appendAndScroll("One or more required files are missing.\n");
                        ErrorExitDialog.showAndExit("Docker image files are missing!", "One or more required files are missing.");
                    }
                });

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();

            } catch (Exception e) {
                Platform.runLater(() ->
                        appendAndScroll(
                                "File check failed: " + e.getMessage() + "\n"
                        )
                );
            }

        });

        fileCheckThread.setDaemon(true);
        fileCheckThread.start();
    }
}
