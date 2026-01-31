package com.hats.medivault_ubuntu_setup_app.task;

import com.hats.medivault_ubuntu_setup_app.ResourceStrings;

public class TaskContainerStart extends Task {
    public TaskContainerStart() {
        super(ResourceStrings.TASK_DOCKER_CONTAINER_START);
    }

    @Override
    public void start() {
        // TODO:
        // verify image exists
        // verify checksum

        //stop all existing containers
        notifyTaskObservers(TaskUpdate.TASK_INTERNAL_WORK, "Stopping existing containers (if any)");
        notifyTaskObservers(TaskUpdate.TASK_WARNING, "All the docker containers will be stopped!");
        clearExistingContainers();

        // start each container
        try {
            startContainer(ResourceStrings.IPFS_DOCKER_IMAGE_NAME, "medivault_ipfs");
            startContainer(ResourceStrings.GETH_DOCKER_IMAGE_NAME, "medivault_geth");
        } catch (Exception e) {
            notifyTaskObservers(
                    TaskUpdate.TASK_FAILED,
                    "failed to start container:" + e.getMessage()
            );
            return;
        }

        notifyTaskObservers(TaskUpdate.TASK_SUCCESS, "containers started successfully");
        notifyTaskObservers(TaskUpdate.TASK_WARNING, "please don't close any bash-terminals or the setup client!");
        notifyTaskObservers(TaskUpdate.TASK_WARNING, "regular backups of the container data folder recommended");
        // show warnings
    }

    @Override
    public void notifyTaskObservers(TaskUpdate taskUpdate, String message) {
        taskObservers.forEach(taskObserver -> {
           taskObserver.notifyObserver(this, taskUpdate, message);
        });
    }

    private void startContainer(String image, String containerName) throws Exception {
        TaskSubCommandRunner.runCommand("docker run -d --restart unless-stopped --name "
                + containerName
                + " "
                + image, this::notifyTaskObservers
        );

        new ProcessBuilder(
                "gnome-terminal",
                "--",
                "docker", "exec", "-it",
                containerName,
                "bash"
        ).start();
    }

    private void clearExistingContainers() {
        // run the commands twice
        // container deletion require confirmation
        TaskSubCommandRunner.runCommandFailureOkay(
                "sh -c \"docker stop $(docker ps -q)\"",
                this::notifyTaskObservers
        );
        TaskSubCommandRunner.runCommandFailureOkay("docker container prune -f", this::notifyTaskObservers);

        TaskSubCommandRunner.runCommandFailureOkay(
                "sh -c \"docker stop $(docker ps -q)\"",
                this::notifyTaskObservers
        );
        TaskSubCommandRunner.runCommandFailureOkay("docker container prune -f", this::notifyTaskObservers);
    }
}
