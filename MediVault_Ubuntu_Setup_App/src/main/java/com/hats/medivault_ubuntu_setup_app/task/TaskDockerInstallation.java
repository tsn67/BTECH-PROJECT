package com.hats.medivault_ubuntu_setup_app.task;

import com.hats.medivault_ubuntu_setup_app.ResourceStrings;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.function.BiConsumer;

/**
 * Docker setup task for Ubuntu/Linux (lts22.0.0 or higher)
 * - Checks Docker installation
 * - Installs Docker if missing
 * NOTE:
 * application must be sudo privileges. (TODO: installer must acquire)
 */
public class TaskDockerInstallation extends Task {
    private final BiConsumer<TaskUpdate, String> commandResponseCallBack = this::notifyTaskObservers;

    public TaskDockerInstallation() {
        super(ResourceStrings.TASK_INSTALL_DOCKER);
    }

    public void start() {
        notifyTaskObservers(TaskUpdate.TASK_STARTED, getTaskName());

        try {
            notifyTaskObservers(
                    TaskUpdate.TASK_INTERNAL_WORK,
                    "Checking Docker installation..."
            );

            if (!isDockerInstalled()) {
                notifyTaskObservers(
                        TaskUpdate.TASK_WARNING,
                        "Docker not found. Installing Docker..."
                );
                installDocker();
            } else {
                notifyTaskObservers(
                        TaskUpdate.TASK_INTERNAL_MESSAGE,
                        "Docker installation found"
                );
            }

            String version = getDockerVersion();
            notifyTaskObservers(
                    TaskUpdate.TASK_SUCCESS,
                    "Docker installation: " + version
            );

            // start next Task
            startNextTask();
        } catch (Exception e) {
            notifyTaskObservers(
                    TaskUpdate.TASK_FAILED,
                    "Docker setup failed: " + e.getMessage()
            );
        }
    }

    // helper methods (ubuntu-linux specific)
    private boolean isDockerInstalled() throws Exception {
        Process process = new ProcessBuilder(
                "bash", "-c", "docker --version"
        ).redirectErrorStream(true).start();

        return process.waitFor() == 0;
    }

    private void installDocker() throws Exception {
        TaskSubCommandRunner.runCommand("apt update", commandResponseCallBack);
        TaskSubCommandRunner.runCommand("apt install -y docker.io", commandResponseCallBack);
        TaskSubCommandRunner.runCommand("systemctl enable docker", commandResponseCallBack);
        TaskSubCommandRunner.runCommand("systemctl start docker", commandResponseCallBack);
    }

    private String getDockerVersion() throws Exception {
        Process process = new ProcessBuilder(
                "bash", "-c", "docker --version"
        ).redirectErrorStream(true).start();

        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            return reader.readLine();
        }
    }

    @Override
    public void notifyTaskObservers(TaskUpdate update, String message) {
        taskObservers.forEach((observer) -> {
            observer.notifyObserver(this, update, message);
        });
    }
}
