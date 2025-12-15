package org.hats.medivaultclientfrontend.view_2;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import org.hats.medivaultclientfrontend.animation.ScrollAnimator;


import java.io.IOException;

public abstract class DownloadTask {

    public boolean success = false;
    private DownloadTask nextTask;

    public void setNextTask(DownloadTask nextTask) {
        this.nextTask = nextTask;
    }

    public void runNextTask() {
        if (nextTask != null)
            nextTask.start();
    }


    protected AnchorPane uiPane;
    protected DownloadUiController uiController;
    private final ScrollPane parentScrollPane;
    private int index = 0;

    private final Color successColor = Color.CORNFLOWERBLUE;
    private final Color failureColor = Color.RED;

    DownloadTask(ScrollPane parentScrollPane) {
        this.parentScrollPane = parentScrollPane;

        try {
            var loader = new FXMLLoader(getClass().getResource("/org/hats/medivaultclientfrontend/ImgDownload.fxml"));
            uiPane = loader.load();
            uiController = loader.getController();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Node getUiNode() {
        return this.uiPane;
    }

    public  void initUi(String taskName) {
        uiController.taskName.setText(taskName);
        uiController.indexLabel.setText(String.valueOf(index));
    }

    private void makeCompleteUi() {
        uiController.downloadProgress.setProgress(1);
        uiController.indexCircle.setFill(successColor);
        uiController.statusRect.setFill(successColor);
        uiController.downloadStatus.setText("Download complete!");
    }

    private void makeFailedUi() {
        uiController.downloadProgress.getStyleClass().add("danger");
        uiController.indexCircle.setFill(failureColor);
        uiController.statusRect.setFill(failureColor);
        uiController.downloadStatus.setText("Something went wrong!");
    }

    protected void makeComplete() {
        makeCompleteUi();
        success = true;
    }

    protected void makeFailed() {
        makeFailedUi();
        success = false;
    }

    public void start() {
        ScrollAnimator.smoothScrollToNode(parentScrollPane, uiPane);
        download();
    }

    public abstract void download();

}
