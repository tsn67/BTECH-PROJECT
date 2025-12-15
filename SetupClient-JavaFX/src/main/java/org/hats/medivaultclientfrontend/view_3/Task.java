package org.hats.medivaultclientfrontend.view_3;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.hats.medivaultclientfrontend.animation.ScrollAnimator;


public abstract class Task {
    private final String taskName;
    private final String taskStartingDialog;
    protected final String completedDialog;
    protected final ScrollPane scrollArea;

    private final Task nextTask;

    public Task(ScrollPane scrollArea, String taskName, String taskStartingDialog, String completedDialog, Task nextTask) {
        this.scrollArea = scrollArea;
        this.taskName = taskName;
        this.taskStartingDialog = taskStartingDialog;
        this.completedDialog = completedDialog;
        this.nextTask = nextTask;
    }

    void initUi() {
        Platform.runLater(() -> appendAndScroll(taskName + "\n" + taskStartingDialog + "\n"));
    }

    public void start() {
        if (scrollArea != null)
            initUi();
        runTask();
    }

    protected void runNextTask() {
        if (nextTask != null)
            nextTask.start();
    }

    protected void appendAndScroll(String text) {
        Label label = new Label(text);
        label.setWrapText(false);
        label.setStyle("-fx-font-size: 14px;");
        label.setStyle("-fx-text-fill: lightgreen;");
        ((VBox)scrollArea.getContent()).getChildren().add(label);
        ScrollAnimator.smoothScrollToNode(scrollArea, label);
    }

    public abstract void runTask();
}
