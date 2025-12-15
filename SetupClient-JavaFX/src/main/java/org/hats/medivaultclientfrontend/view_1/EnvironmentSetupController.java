package org.hats.medivaultclientfrontend.view_1;

import org.hats.medivaultclientfrontend.utils.PlatformUrls;
import org.hats.medivaultclientfrontend.controller.ViewController;
import org.hats.medivaultclientfrontend.view_1.dockerutils.DockerDetector;
import org.hats.medivaultclientfrontend.view_1.dockerutils.DockerRunner;
import atlantafx.base.theme.Styles;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.hats.medivaultclientfrontend.view_1.EnvChainedTasks.ChainedTask;
import org.hats.medivaultclientfrontend.utils.ThemeController;

import java.net.URL;
import java.util.ResourceBundle;

public class EnvironmentSetupController extends ViewController implements Initializable {
    private static final String dockerInstallUrl = PlatformUrls.dockerDownloadUrl;

    @FXML
    private ChoiceBox<String> themeChoiceButton;

    @FXML
    private ProgressIndicator loader;

    @FXML
    private Label statusLabel;

    @FXML
    private HBox dockerInstallationHbox;

    @FXML
    private ProgressIndicator runLoader;

    @FXML
    private Label runStatusLabel;

    @FXML
    private Button dockerStartButton;

    @FXML
    private Pane dockerStatusBar;

    @FXML
    public Button nextButton;

    @FXML
    private Label dockerInstallLabel;

    private void openGoogle() throws Exception {
        java.awt.Desktop.getDesktop().browse(new java.net.URI(dockerInstallUrl));
    }

    void initDockerInstallLabel() {
        dockerInstallLabel.setOnMouseEntered(e -> {
            dockerInstallLabel.setStyle("-fx-text-fill: #379df5;");
        });

        dockerInstallLabel.setOnMouseExited(e -> {
            dockerInstallLabel.setStyle("-fx-text-fill: #0956dc;");
        });

        dockerInstallLabel.setOnMouseClicked(e -> {
            try {
                openGoogle();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nextButton.setDisable(true);
        //// for test purpose
        //nextButton.setDisable(false);
        nextButton.setOnAction(e -> nextView());
        // test end
        dockerStartButton.getStyleClass().add(Styles.ACCENT);

        new ThemeController(themeChoiceButton).initializeThemeSettings();
        initDockerInstallLabel();

        DockerDetector dockerDetector = new DockerDetector(loader, statusLabel, dockerInstallationHbox);
        DockerRunner dockerRunner = new DockerRunner(runStatusLabel, runLoader, dockerStartButton, dockerStatusBar);
        DockerSetupSuccessTask dockerSetupSuccessTask = new DockerSetupSuccessTask(this);

        dockerDetector.setChainedTask(dockerRunner);
        dockerRunner.setChainedTask(dockerSetupSuccessTask);
        dockerDetector.run();
    }
}

class DockerSetupSuccessTask extends ChainedTask {
    private final EnvironmentSetupController controller;
    DockerSetupSuccessTask(EnvironmentSetupController controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        controller.nextButton.getStyleClass().add(Styles.ACCENT);
        controller.nextButton.setDisable(false);
        controller.nextButton.setOnAction(e -> {
            controller.nextView();
        });
    }
}