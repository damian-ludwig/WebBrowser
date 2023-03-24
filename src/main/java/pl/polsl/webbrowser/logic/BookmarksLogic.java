package pl.polsl.webbrowser.logic;

import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.*;

public class BookmarksLogic{
    private static final String LIST_FILE_NAME = "bookmarks.list";
    private static HBox bookmarksHBox;
    private TabPane mainTabPane;

    public BookmarksLogic(HBox bookmarksHBox, TabPane mainTabPane){
        this.bookmarksHBox = bookmarksHBox;
        this.mainTabPane = mainTabPane;
    }

    private WebEngine getCurrentEngine(){
        return ((WebView) mainTabPane.getSelectionModel().getSelectedItem().getContent()).getEngine();
    }
    public void createAndAddBookmarkButton(String title, String urlAddress){
        if(title != null && urlAddress != null) {
            Button button = new Button();
            button.setText(title);
            button.setAccessibleText(urlAddress);
            button.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    getCurrentEngine().load(urlAddress);
                } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    deleteBookmarkButton(button);
                }
            });
            bookmarksHBox.getChildren().add(button);
        }
    }


    public void deleteBookmarkButton(Button button){
        bookmarksHBox.getChildren().remove(button);
    }

    public void loadBookmarks() {
        File file = new File(LIST_FILE_NAME);

        if (file.exists()) {
            try (
                    var fileReader = new FileReader(file);
                    var bufferedReader = new BufferedReader(fileReader);
            ) {
                String nextLine = null;
                boolean end = true;
                String title = null;
                String url = null;
                while ((nextLine = bufferedReader.readLine()) != null) {
                    if (end){
                        title = nextLine;
                        end = false;
                    }else{
                        url = nextLine;
                        createAndAddBookmarkButton(title,url);
                        end = true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveBookmarks() {
        try (
                var fileWriter = new FileWriter(LIST_FILE_NAME);
                var bufferedWriter = new BufferedWriter(fileWriter);
        ) {
            for (int i = 0; i < bookmarksHBox.getChildren().size(); i++) {
                Button button = (Button) bookmarksHBox.getChildren().get(i);
                fileWriter.write(button.getText() + "\n" + button.getAccessibleText() +"\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
