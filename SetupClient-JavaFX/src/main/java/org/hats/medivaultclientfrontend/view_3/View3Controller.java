package org.hats.medivaultclientfrontend.view_3;

import atlantafx.base.theme.Styles;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import org.hats.medivaultclientfrontend.controller.ViewController;
import java.net.URL;
import java.util.ResourceBundle;

public class View3Controller extends ViewController implements Initializable {
    @FXML
    private Button nextButton;

    @FXML
    private ScrollPane scrollArea;

    private void initUi() {
        nextButton.getStyleClass().add(Styles.ACCENT);
        nextButton.setDisable(true);
        // hide scroll bars from scrollArea (scrollPane)
        scrollArea.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollArea.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initUi();

        // set onClickTask for the nextButton
        nextButton.setOnAction((_) -> nextView());

        var endTask = new EndTask(nextButton);
        var loadImagesTask = new LoadDockerImages(scrollArea, "docker-img build", "building docker images", "docker images built", endTask);
        var downloadTask = new DownloadFileCheck(scrollArea, "docker-img files check", "check docker-images", "docker images downloaded", loadImagesTask);
        var dockerStatusCheckTask = new DockerRunStatusCheckTask(scrollArea, "docker run status check", "running docker image ls", "docker daemon is running", downloadTask);
        dockerStatusCheckTask.start();
    }
}
