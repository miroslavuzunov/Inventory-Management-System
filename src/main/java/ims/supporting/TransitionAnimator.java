package ims.supporting;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.WritableValue;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class TransitionAnimator {
    private static VBox pane;
    private static WritableValue<Double> writableWidth = new WritableValue<Double>() {
        @Override
        public Double getValue() {
            return pane.getWidth();
        }

        @Override
        public void setValue(Double value) {
            pane.setPrefWidth(value);
        }
    };

    public static void openTransition(VBox paneToBeTransformed){
        pane = paneToBeTransformed;

        Timeline flash = new Timeline(
                new KeyFrame(Duration.seconds(0.20), new KeyValue(writableWidth, 200.00, Interpolator.EASE_IN))
        );
        flash.setCycleCount(1);
        flash.play();
    }

    public static void closeTransition(VBox paneToBeTransformed){
        pane = paneToBeTransformed;

        Timeline flash = new Timeline(
                new KeyFrame(Duration.seconds(0.15), new KeyValue(writableWidth, 60.00, Interpolator.EASE_OUT))
        );
        flash.setCycleCount(1);
        flash.play();
    }
}
