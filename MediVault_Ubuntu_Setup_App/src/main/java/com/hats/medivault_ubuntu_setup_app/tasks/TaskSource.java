package com.hats.medivault_ubuntu_setup_app.tasks;

public interface TaskSource {
    void addTaskObserver(TaskObserver taskObserver);
    void notifyTaskObservers(TaskUpdate taskUpdate, String message);
}
