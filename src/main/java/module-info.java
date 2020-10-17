module org.sample {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.sample to javafx.fxml;
    exports org.sample;
}