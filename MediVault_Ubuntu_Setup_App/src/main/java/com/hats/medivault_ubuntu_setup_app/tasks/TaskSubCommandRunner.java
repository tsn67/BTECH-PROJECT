package com.hats.medivault_ubuntu_setup_app.tasks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.function.BiConsumer;

public class TaskSubCommandRunner {

    public static void runCommand(String command, BiConsumer<TaskUpdate, String> commandResultCallback) throws Exception {
        Process process = new ProcessBuilder(
                "bash", "-c", command
        ).redirectErrorStream(true).start();

        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                commandResultCallback.accept(TaskUpdate.TASK_INTERNAL_MESSAGE, line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Command failed: " + command);
        }
    }
}
