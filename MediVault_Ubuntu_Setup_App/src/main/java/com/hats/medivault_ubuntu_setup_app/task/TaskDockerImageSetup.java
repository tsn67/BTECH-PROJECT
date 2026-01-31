package com.hats.medivault_ubuntu_setup_app.task;

import com.hats.medivault_ubuntu_setup_app.ResourceStrings;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TaskDockerImageSetup extends Task {
    private static final String DOWNLOAD_OUT_DIR_PATH = "./data/images";
    private static final int STREAMLINE_BUFFER_SIZE = 8192;

    public TaskDockerImageSetup() {
        super(ResourceStrings.TASK_DOCKER_IMAGE_SETUP);
    }

    @Override
    public void start() {
        notifyTaskObservers(TaskUpdate.TASK_STARTED, getTaskName());

        try {
            // download docker images (.tar) files

            final String ipfs_image_url =
                    ResourceStrings.RESOURCE_API_URL + "/"
                            + ResourceStrings.IPFS_DOCKER_IMAGE_NAME;

            final String geth_image_url =
                    ResourceStrings.RESOURCE_API_URL + "/"
                            + ResourceStrings.GETH_DOCKER_IMAGE_NAME;

            notifyTaskObservers(TaskUpdate.TASK_INTERNAL_WORK, "downloading " + ResourceStrings.IPFS_DOCKER_IMAGE_NAME);
            downloadDockerImage(ipfs_image_url, ResourceStrings.IPFS_DOCKER_IMAGE_NAME);
            notifyTaskObservers(TaskUpdate.TASK_INTERNAL_WORK, "downloading " + ResourceStrings.GETH_DOCKER_IMAGE_NAME);
            downloadDockerImage(geth_image_url, ResourceStrings.GETH_DOCKER_IMAGE_NAME);

            notifyTaskObservers(TaskUpdate.TASK_SUCCESS, "download complete");

        } catch (IOException e) {
            notifyTaskObservers(
                    TaskUpdate.TASK_FAILED,
                    "Couldn't download docker image: " + e.getMessage()
            );
            return;
        }

        // check whether docker is still running
        notifyTaskObservers(TaskUpdate.TASK_INTERNAL_MESSAGE, "docker running status verification");
        try {
            if (!isDockerRunning()) {
                throw new RuntimeException("Docker daemon not running! Please restart installer");
            }
        } catch (Exception e) {
            notifyTaskObservers(
                    TaskUpdate.TASK_FAILED,
                    "Couldn't download docker image: " + e.getMessage()
            );
            return;
        }
        notifyTaskObservers(TaskUpdate.TASK_SUCCESS, "docker engine running");

        // load docker images (into docker storage)
        try {
            Path ipfsImagePath = Paths.get(DOWNLOAD_OUT_DIR_PATH + "/" + ResourceStrings.IPFS_DOCKER_IMAGE_NAME);
            Path gethImagePath = Paths.get(DOWNLOAD_OUT_DIR_PATH + "/" + ResourceStrings.GETH_DOCKER_IMAGE_NAME);

            loadDockerImage(ipfsImagePath);
            loadDockerImage(gethImagePath);
        } catch (Exception e) {
            notifyTaskObservers(
                    TaskUpdate.TASK_FAILED,
                    "failed to load docker images:" + e.getMessage()
            );
            return;
        }
        notifyTaskObservers(TaskUpdate.TASK_INTERNAL_MESSAGE, "docker images load complete");

        notifyTaskObservers(TaskUpdate.TASK_SUCCESS, "docker image setup completed");

        startNextTask();
    }

    private boolean isDockerRunning() throws Exception {
        Process process = new ProcessBuilder(
                "bash", "-c", "systemctl is-active --quiet docker"
        ).start();

        return process.waitFor() == 0;
    }

    @Override
    public void notifyTaskObservers(TaskUpdate taskUpdate, String message) {
        taskObservers.forEach(taskObserver -> {
            taskObserver.notifyObserver(this, taskUpdate, message);
        });
    }

    private void downloadDockerImage(String url, String imageName) throws IOException {

        HttpURLConnection conn =
                (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestProperty("Accept-Encoding", "identity"); // IMPORTANT
        conn.connect();

        long totalBytes = conn.getContentLengthLong();

        if (totalBytes <= 0) {
            notifyTaskObservers(TaskUpdate.TASK_WARNING, "Unknown file size, progress unavailable");
        }

        long downloadedBytes = 0;
        int lastPrintedPercent = -1;

        try (InputStream in = conn.getInputStream();
             FileOutputStream out =
                     new FileOutputStream(DOWNLOAD_OUT_DIR_PATH + "/" + imageName)) {

            byte[] buffer = new byte[STREAMLINE_BUFFER_SIZE];
            int len;

            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
                downloadedBytes += len;

                if (totalBytes > 0) {
                    int percent = (int) ((downloadedBytes * 100) / totalBytes);

                    if (percent != lastPrintedPercent) {
                        lastPrintedPercent = percent;
                        notifyTaskObservers(TaskUpdate.TASK_INTERNAL_MESSAGE, "progress " + percent + "%");
                    }
                }
            }
        }

        notifyTaskObservers(TaskUpdate.TASK_INTERNAL_MESSAGE, "Download completed: 100%");
    }

    private void loadDockerImage(Path tarPath) throws Exception {
        //TODO: sudo or pkexec (both) can be removed later
        ProcessBuilder pb = new ProcessBuilder(
                "pkexec", "docker", "load", "-i", tarPath.toAbsolutePath().toString()
        );

        pb.redirectErrorStream(true);
        Process process = pb.start();

        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                notifyTaskObservers(TaskUpdate.TASK_INTERNAL_MESSAGE, "[docker-load]" + line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("docker load failed: " + tarPath);
        }
    }
}

