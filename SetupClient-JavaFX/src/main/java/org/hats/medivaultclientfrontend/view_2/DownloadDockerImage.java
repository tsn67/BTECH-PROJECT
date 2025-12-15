package org.hats.medivaultclientfrontend.view_2;

import javafx.concurrent.Task;
import javafx.scene.control.ScrollPane;
import org.hats.medivaultclientfrontend.utils.ErrorExitDialog;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadDockerImage extends DownloadTask {

   private final String downloadUrl;
   private final String fileName;
    DownloadDockerImage(ScrollPane parentScrollPane, String downloadUrl, String fileName, String imageInfo, int index) {
        super(parentScrollPane);
        this.downloadUrl = downloadUrl;
        this.fileName = fileName;
        setIndex(index);
        initUi(imageInfo);
    }

    @Override
    public void download() {
        downloadLoadImage();
    }

    public void downloadLoadImage() {

        String downloadUrl = this.downloadUrl;

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                updateMessage("Connecting to server...");
                URL url = new URL(downloadUrl + fileName);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int status = connection.getResponseCode();
                if (status != HttpURLConnection.HTTP_OK) {
                    throw new RuntimeException("Server responded with status: " + status);
                }

                int contentLength = connection.getContentLength(); // may be -1
                InputStream inputStream = connection.getInputStream();

                File outputDir = new File("downloads");
                if (!outputDir.exists()) outputDir.mkdirs();

                File outputFile = new File(outputDir, fileName);

                // make a delay
                Thread.sleep(1400);

                try (BufferedInputStream bis = new BufferedInputStream(inputStream);
                     FileOutputStream fos = new FileOutputStream(outputFile)) {

                    byte[] buffer = new byte[8192];
                    long downloaded = 0;
                    int read;

                    if (contentLength > 0) {
                        // CASE 1: Progress can be calculated
                        updateProgress(0, contentLength);
                        while ((read = bis.read(buffer)) != -1) {
                            fos.write(buffer, 0, read);
                            downloaded += read;
                            updateProgress(downloaded, contentLength);
                            updateMessage("Downloaded " + (downloaded * 100 / contentLength) + "%");
                        }
                    } else {
                        // CASE 2: Unknown size -> indeterminate progress
                        updateProgress(-1, -1);
                        while ((read = bis.read(buffer)) != -1) {
                            fos.write(buffer, 0, read);
                            downloaded += read;
                            updateMessage("Downloading... " + (downloaded / 1024) + " KB");
                        }
                    }
                }

                updateMessage("Download complete.");
                return null;
            }
        };

        uiController.downloadProgress.progressProperty().bind(task.progressProperty());
        uiController.downloadStatus.textProperty().bind(task.messageProperty());

        task.setOnSucceeded(e -> {
            uiController.downloadProgress.progressProperty().unbind();
            uiController.downloadStatus.textProperty().unbind();
            uiController.downloadProgress.setProgress(1.0);
            uiController.downloadStatus.setText("Completed!");
            runNextTask();
            makeComplete();
        });

        task.setOnFailed(e -> {
            uiController.downloadProgress.progressProperty().unbind();
            uiController.downloadStatus.textProperty().unbind();
            uiController.downloadStatus.setText("Failed: " + task.getException().getMessage());
            makeFailed();
            //development purposes - disable following statement
            ErrorExitDialog.showAndExit(
                    "Network Connection Error",
                    "The application was unable to connect to the server.\n\n" +
                            "Please check your internet or local network connection.\n" +
                            "The backend service may be temporarily unavailable.\n\n" +
                            "Try again in a few minutes, then restart the application."
            );
        });

        new Thread(task).start();
    }

}
