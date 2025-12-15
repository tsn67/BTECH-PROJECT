package org.hats.medivaultclientfrontend.animation;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class ViewAnimator {
    final static int animationDuration = 450;

    public static void switchView(Pane container, Node newView) {

        if (container.getChildren().isEmpty()) {
            container.getChildren().add(newView);
            fadeIn(newView);
            return;
        }

        Node oldView = container.getChildren().get(0);
        fadeOutLeft(oldView, () -> {
            container.getChildren().clear();
            container.getChildren().add(newView);
            fadeInRight(newView);
        });
    }

    private static void fadeOutLeft(Node node, Runnable onFinished) {
        TranslateTransition slide = new TranslateTransition(Duration.millis(animationDuration), node);
        slide.setToX(-80);

        FadeTransition fade = new FadeTransition(Duration.millis(animationDuration), node);
        fade.setToValue(0.0);

        ParallelTransition pt = new ParallelTransition(slide, fade);
        pt.setOnFinished(e -> onFinished.run());
        pt.play();
    }

    private static void fadeInRight(Node node) {
        node.setOpacity(0);
        node.setTranslateX(40);

        TranslateTransition slide = new TranslateTransition(Duration.millis(animationDuration), node);
        slide.setToX(0);

        FadeTransition fade = new FadeTransition(Duration.millis(animationDuration), node);
        fade.setToValue(1.0);

        new ParallelTransition(slide, fade).play();
    }

    private static void fadeIn(Node node) {
        node.setOpacity(0);
        FadeTransition fade = new FadeTransition(Duration.millis(animationDuration), node);
        fade.setToValue(1.0);
        fade.play();
    }
}
