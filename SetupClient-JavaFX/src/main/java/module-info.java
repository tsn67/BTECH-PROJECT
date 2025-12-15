module org.hats.medivaultclientfrontend {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires atlantafx.base;
    requires javafx.graphics;
    requires java.desktop;

    opens org.hats.medivaultclientfrontend.view_1 to javafx.fxml;
    opens org.hats.medivaultclientfrontend.view_2 to javafx.fxml;
    opens org.hats.medivaultclientfrontend.view_3 to javafx.fxml;
    opens org.hats.medivaultclientfrontend to javafx.fxml;
    opens org.hats.medivaultclientfrontend.controller to javafx.fxml;
    opens org.hats.medivaultclientfrontend.view_1.dockerutils to javafx.fxml;

    exports org.hats.medivaultclientfrontend;
    exports org.hats.medivaultclientfrontend.controller;
    exports org.hats.medivaultclientfrontend.view_1.EnvChainedTasks;
    exports org.hats.medivaultclientfrontend.view_1.dockerutils;
    exports org.hats.medivaultclientfrontend.utils;
    opens org.hats.medivaultclientfrontend.utils to javafx.fxml;
}