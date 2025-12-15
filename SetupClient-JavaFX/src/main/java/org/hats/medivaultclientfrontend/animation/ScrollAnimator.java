package org.hats.medivaultclientfrontend.animation;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.util.Duration;

public class ScrollAnimator {

    public static void smoothScrollToNode(ScrollPane scrollPane, Node node) {
        if (scrollPane == null || node == null) return;
        Platform.runLater(() -> {
            scrollPane.layout();

            Bounds viewportBounds = scrollPane.getViewportBounds();
            Bounds contentBounds = scrollPane.getContent().getBoundsInLocal();

            Bounds nodeBounds = node.getBoundsInLocal();
            Bounds nodeInContent = scrollPane.getContent().sceneToLocal(
                    node.localToScene(nodeBounds)
            );

            double nodeY = nodeInContent.getMinY();
            double viewportHeight = viewportBounds.getHeight();
            double contentHeight = contentBounds.getHeight();

            // Scroll node to TOP of viewport (remove the centering calculation)
            double targetY = nodeY;

            double scrollableHeight = contentHeight - viewportHeight;
            double targetV = scrollableHeight > 0 ? targetY / scrollableHeight : 0;
            targetV = Math.max(0, Math.min(targetV, 1));

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(300),
                            new KeyValue(scrollPane.vvalueProperty(), targetV, Interpolator.EASE_BOTH)
                    )
            );
            timeline.play();
        });
    }
}
