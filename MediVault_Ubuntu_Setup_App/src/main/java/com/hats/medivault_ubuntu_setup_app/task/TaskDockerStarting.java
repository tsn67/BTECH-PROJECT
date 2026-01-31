package com.hats.medivault_ubuntu_setup_app.task;

import com.hats.medivault_ubuntu_setup_app.ResourceStrings;

import java.util.function.BiConsumer;

public class TaskDockerStarting extends Task {
    private final BiConsumer<TaskUpdate, String> commandResponseCallBack = this::notifyTaskObservers;

    public TaskDockerStarting() {
        super(ResourceStrings.TASK_START_DOCKER);
    }

    @Override
    public void start() {
        try {
            notifyTaskObservers(
                    TaskUpdate.TASK_INTERNAL_WORK,
                    "Checking Docker service status..."
            );

            if (!isDockerRunning()) {
                notifyTaskObservers(
                        TaskUpdate.TASK_INTERNAL_MESSAGE,
                        "Docker service not running. Starting Docker..."
                );
                startDockerService();
            }

            notifyTaskObservers(TaskUpdate.TASK_SUCCESS, "Docker engine started");
            startNextTask();
        } catch (Exception e) {
            notifyTaskObservers(
                    TaskUpdate.TASK_FAILED,
                    "Couldn't start docker: " + e.getMessage()
            );
        }
    }

    @Override
    public void notifyTaskObservers(TaskUpdate taskUpdate, String message) {
        taskObservers.forEach(observer -> observer.notifyObserver(this, taskUpdate, message));
    }

    private void startDockerService() throws Exception {
        // change this to systemctl start..., before creating installer
        // installed app will have sudo privileges
        TaskSubCommandRunner.runCommand("systemctl start docker", commandResponseCallBack);
    }

    private boolean isDockerRunning() throws Exception {
        Process process = new ProcessBuilder(
                "bash", "-c", "systemctl is-active --quiet docker"
        ).start();

        return process.waitFor() == 0;
    }

}
