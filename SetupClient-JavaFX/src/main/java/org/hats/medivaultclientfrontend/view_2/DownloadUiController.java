package org.hats.medivaultclientfrontend.view_2;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class DownloadUiController {

    @FXML
    public AnchorPane containerPane;

    @FXML
    public ProgressBar downloadProgress;

    @FXML
    public Label downloadStatus;

    @FXML
    public Circle indexCircle;

    @FXML
    public Label indexLabel;

    @FXML
    public Rectangle statusRect;

    @FXML
    public Label taskName;
}
