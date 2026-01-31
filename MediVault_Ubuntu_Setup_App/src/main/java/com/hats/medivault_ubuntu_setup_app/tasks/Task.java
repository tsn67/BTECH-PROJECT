package com.hats.medivault_ubuntu_setup_app.tasks;

import java.util.ArrayList;
import java.util.List;

public abstract class Task implements TaskSource {
    private String taskName;
    private Task nextTask = null;
    protected List<TaskObserver> taskObservers = new ArrayList<>();

    public Task(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskName() {
        return taskName;
    }

    @Override
    public void addTaskObserver(TaskObserver taskObserver) {
        taskObservers.add(taskObserver);
    }

    public abstract void start();

    public void setNextTask(Task nextTask) {
        this.nextTask = nextTask;
    }

    public void startNextTask() {
        if (nextTask != null) {
            // update the app with next task
            nextTask.start();
        }
    }
}
