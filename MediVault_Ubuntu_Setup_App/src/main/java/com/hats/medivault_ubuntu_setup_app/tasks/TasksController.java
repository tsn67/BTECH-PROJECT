package com.hats.medivault_ubuntu_setup_app.tasks;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class TasksController implements TaskObserver, Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Task task1 = new TaskDockerInstallation();
        Task task2 = new TaskDockerStarting();

        task1.setNextTask(task2);
        task2.setNextTask(null);

        task1.addTaskObserver(this);
        task2.addTaskObserver(this);

        //only task1 need to be started
        //on success it will start the next task
        task1.start();
    }

    @Override
    public void notifyObserver(Object source, TaskUpdate taskUpdate, String message) {
        System.out.println(taskUpdate.toString());
        System.out.println(message);
    }
}
