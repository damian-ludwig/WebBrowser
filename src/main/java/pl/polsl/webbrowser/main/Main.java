package pl.polsl.webbrowser.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pl.polsl.webbrowser.logic.BookmarksLogic;
import pl.polsl.webbrowser.logic.HistoryLogic;
import pl.polsl.webbrowser.logic.StartupPageLogic;


public class Main extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        VBox mainPane = FXMLLoader.load(getClass().getResource("/fxml/mainPane.fxml"));
        Scene scene = new Scene(mainPane);
        stage.setScene(scene);
        stage.setTitle("Przeglądarka internetowa");
        stage.show();
    }

    @Override
    public void stop(){
        HistoryLogic.saveHistoryList();
        BookmarksLogic.saveBookmarks();
        StartupPageLogic.saveStartupPageUrlAddress();
    }
}
