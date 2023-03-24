module WebBrowser {
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.web;

    exports pl.polsl.webbrowser.main to javafx.graphics;
    opens pl.polsl.webbrowser.controller to javafx.fxml;
}