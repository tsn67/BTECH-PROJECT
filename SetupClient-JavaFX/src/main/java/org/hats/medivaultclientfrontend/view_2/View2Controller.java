package org.hats.medivaultclientfrontend.view_2;

import atlantafx.base.theme.Styles;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.hats.medivaultclientfrontend.utils.PlatformUrls;
import org.hats.medivaultclientfrontend.controller.ViewController;

import java.net.URL;
import java.util.ResourceBundle;

public class View2Controller extends ViewController implements Initializable {
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button nextButton;

    @FXML
    private VBox scrollVbox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nextButton.getStyleClass().add(Styles.ACCENT);
        nextButton.setDisable(false);

        // test purpose
        nextButton.setDisable(false);

        nextButton.setOnAction(event -> {
            nextView();
        });

        DownloadTask[] downloader = new DownloadTask[3];

        //download ether client
        downloader[0] = new DownloadDockerImage(scrollPane, PlatformUrls.resourceApiUrl + "docker-images/", "ether-client-v-1.tar", "(go-ether) ether-client (custom-v1.0.0)", 0);
        scrollVbox.getChildren().add(downloader[0].getUiNode());

        // download ipfs client
        downloader[1] = new DownloadDockerImage(scrollPane, PlatformUrls.resourceApiUrl + "docker-images/", "ipfs-client-v-1.tar", "(IPFS) ipfs-client (custom-v1.0.0)", 1);
        scrollVbox.getChildren().add(downloader[1].getUiNode());

        // set initial scroll position
        scrollPane.setVvalue(0);
        // next view task
        downloader[downloader.length - 1] = new EndTask(nextButton);

        // chain tasks
        downloader[0].setNextTask(downloader[1]);
        downloader[1].setNextTask(downloader[2]);

        downloader[0].start();
    }
}
