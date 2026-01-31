module com.hats.medivault_ubuntu_setup_app {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.hats.medivault_ubuntu_setup_app to javafx.fxml;
    exports com.hats.medivault_ubuntu_setup_app;
    exports com.hats.medivault_ubuntu_setup_app.task;
    opens com.hats.medivault_ubuntu_setup_app.task to javafx.fxml;
}