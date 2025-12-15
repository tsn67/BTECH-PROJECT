package org.hats.medivaultclientfrontend.view_3;

import javafx.scene.control.ScrollPane;
import org.hats.medivaultclientfrontend.utils.ErrorExitDialog;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LoadDockerImages extends Task {

    public LoadDockerImages(ScrollPane scrollArea, String taskName, String taskStartingDialog, String completedDialog, Task nextTask) {
        super(scrollArea, taskName, taskStartingDialog, completedDialog, nextTask);
    }


    private void loadDockerImage(Path tarFile, String displayName) throws Exception {
        appendAndScroll("Loading " + displayName + " image...\n");

        ProcessBuilder pb = new ProcessBuilder(
                "docker", "load", "-i", tarFile.toAbsolutePath().toString()
        );

        pb.redirectErrorStream(true);
        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {

            String line;
            while ((line = reader.readLine()) != null) {
                appendAndScroll("  " + line + "\n");
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException(displayName + " image load failed (exit code " + exitCode + ")");
        }

        appendAndScroll(displayName + " image loaded successfully.\n\n");
    }

    @Override
    public void runTask() {
        try {
            final String ipfsTarFileName = "ipfs-client-v-1.tar";
            final String etherTarFileName = "ether-client-v-1.tar";
            appendAndScroll("Preparing Docker image loader...\n");

            String appRoot = System.getProperty("user.dir");
            Path downloadsDir = Paths.get(appRoot, "downloads");

            Path ipfsTar = downloadsDir.resolve(ipfsTarFileName);
            Path etherTar = downloadsDir.resolve(etherTarFileName);

            loadDockerImage(ipfsTar, "IPFS Client");
            loadDockerImage(etherTar, "Ethereum Client");

            appendAndScroll("\nAll Docker images loaded successfully.\n");
            runNextTask();
        } catch (Exception e) {
            ErrorExitDialog.showAndExit("Docker image build failed!","\nDocker image loading failed:\n" + e.getMessage() + "\n");
        }
    }
}
