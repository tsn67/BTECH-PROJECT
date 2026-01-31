package com.hats.medivault_ubuntu_setup_app.task;

public interface TaskObserver {
    void notifyObserver(Object source, TaskUpdate taskUpdate, String message);
}
