package pl.polsl.webbrowser.controller;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import pl.polsl.webbrowser.logic.*;



public class MainController {

    @FXML
    private TextField urlTextField;

    @FXML
    private TabPane mainTabPane;

    @FXML
    private RadioMenuItem googleRadioMenuItem;

    @FXML
    private RadioMenuItem bingRadioMenuItem;

    @FXML
    private RadioMenuItem yahooRadioMenuItem;

    @FXML
    private RadioMenuItem duckRadioMenuItem;

    @FXML
    private ListView<HistoryRecord> historyListView;

    @FXML
    private TextField bookmarkTitleTextField;

    @FXML
    private HBox bookmarksHBox;

    @FXML
    private TextField startupPageTextField;



    SearchEngineLogic searchEngineLogic;
    HistoryLogic historyLogic;
    UrlLogic urlLogic;
    BookmarksLogic bookmarksLogic;
    StartupPageLogic startupPageLogic;


    public void initialize() {
        mainTabPane.setTabMaxWidth(100);
        searchEngineLogic = new SearchEngineLogic();
        historyLogic = new HistoryLogic(historyListView);
        urlLogic = new UrlLogic();
        bookmarksLogic = new BookmarksLogic(bookmarksHBox, mainTabPane);
        startupPageLogic = new StartupPageLogic(startupPageTextField);


        startupPageLogic.loadStartupPageUrlAddress();
        setUpNewTabButton();
        setUpTabOnClick();
        selectRadioMenuItem();
        historyLogic.loadHistoryList();
        bookmarksLogic.loadBookmarks();



        mainTabPane.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            oldValue.setOnClosed(y -> {
                Tab tab = (Tab) y.getSource();
                WebView webView = (WebView)tab.getContent();
                webView.getEngine().load(null);
            });
        });


    }

    private void selectRadioMenuItem() {
        if (searchEngineLogic.getSearchEngine().equals(SearchEngine.GOOGLE.getUrlAddress()))
            googleRadioMenuItem.setSelected(true);
        else if (searchEngineLogic.getSearchEngine().equals(SearchEngine.BING.getUrlAddress()))
            bingRadioMenuItem.setSelected(true);
        else if (searchEngineLogic.getSearchEngine().equals(SearchEngine.YAHOO.getUrlAddress()))
            yahooRadioMenuItem.setSelected(true);
        else if (searchEngineLogic.getSearchEngine().equals(SearchEngine.DUCK.getUrlAddress()))
            duckRadioMenuItem.setSelected(true);
    }


    public void urlTextFieldKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            loadUrl(urlTextField.getText());
        }
    }

    public void searchButtonOnClick(ActionEvent actionEvent) {
        loadUrl(urlTextField.getText());
    }

    public void backButtonOnClick(ActionEvent actionEvent) {
        back();
    }

    public void nextButtonOnClick(ActionEvent actionEvent) {
        next();
    }

    public void refreshButtonOnClick(ActionEvent actionEvent) {
        refresh();
    }

    public void newTabMenuItemOnClick(ActionEvent actionEvent) {
        createNewTab();
    }


    public void googleRadioMenuItemOnSelection(ActionEvent actionEvent) {
        searchEngineLogic.setDefaultSearchEngine(SearchEngine.GOOGLE);
    }

    public void bingRadioMenuItemOnSelection(ActionEvent actionEvent) {
        searchEngineLogic.setDefaultSearchEngine(SearchEngine.BING);
    }

    public void yahooRadioMenuItemOnSelection(ActionEvent actionEvent) {
        searchEngineLogic.setDefaultSearchEngine(SearchEngine.YAHOO);
    }

    public void duckRadioMenuItemOnSelection(ActionEvent actionEvent) {
        searchEngineLogic.setDefaultSearchEngine(SearchEngine.DUCK);
    }

    public void zoomOutMenuItemClicked(ActionEvent actionEvent) {
        zoomOut();
    }

    public void zoomInMenuItemClicked(ActionEvent actionEvent) {
        zoomIn();
    }

    public void closeMenuItemOnClick(ActionEvent actionEvent) {
        ((Stage) (mainTabPane.getScene().getWindow())).close();
    }

    public void clearHistoryMenuItemOnClick(ActionEvent actionEvent) {
        historyLogic.clearHistory();
    }


    public void historyListViewOnMouseClicked(MouseEvent mouseEvent) {
        historyListView.getSelectionModel().clearSelection();
    }

    public void bookmarkMenuButtonOnClick(MouseEvent mouseEvent) {
        setBookmarkTitle(bookmarkTitleTextField);
    }


    public void bookmarkAddMenuItemOnClick(ActionEvent actionEvent) {
        bookmarksLogic.createAndAddBookmarkButton(bookmarkTitleTextField.getText(), urlTextField.getText());
    }

    public void exportHistoryMenuItemOnClick(ActionEvent actionEvent) {
        historyLogic.exportHistoryList();
    }

    public void saveStartupPageMenuItemOnClick(ActionEvent actionEvent) {
        startupPageLogic.setStartupPageUrlAddress(startupPageTextField.getText());
    }

    public void loadUrl(String phrase) {
        if (urlLogic.checkIfValidUrl(phrase)) {
            getCurrentEngine().load(urlLogic.transformURL(phrase));
        } else getCurrentEngine().load(searchEngineLogic.getSearchEngine() + phrase);
    }

    public void back() {
        getCurrentEngine().executeScript("history.back()");
    }

    public void next() {
        getCurrentEngine().executeScript("history.forward()");
    }

    public void refresh() {
        getCurrentEngine().executeScript("location.reload()");
    }


    public void setUpNewTabButton() {
        Tab addTab = new Tab("+");
        addTab.setClosable(false);
        mainTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab == addTab) {
                createNewTab();
            }
        });
        mainTabPane.getTabs().add(addTab);
    }

    public void createNewTab() {
        Tab newTab = new Tab("New Tab");
        mainTabPane.getTabs().add(mainTabPane.getTabs().size() - 1, newTab);
        mainTabPane.getSelectionModel().select(mainTabPane.getTabs().size() - 2);

        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();
        //engine.load("https://google.com");

        if (urlLogic.checkIfValidUrl(StartupPageLogic.getStartupPageUrlAddress())) {
            engine.load(urlLogic.transformURL(StartupPageLogic.getStartupPageUrlAddress()));
        }


        newTab.setContent(webView);



        engine.locationProperty().addListener((observableValue, oldValue, newValue) -> {
            urlTextField.setText(newValue);
            engine.titleProperty().addListener((observableTitle, oldTitle, newTitle) -> {
                //System.out.println(newValue);
                //System.out.println("title: " + newTitle);
                newTab.setText(newTitle);
                if (newTitle != null) {
                    historyLogic.addToHistory(newTitle, urlLogic.shortenUrl(newValue), newValue);
                }
            });
        });

        historyListView.getSelectionModel().selectedItemProperty().addListener((observableRecord, oldRecord, newRecord) -> {
            if (newRecord != null) {
                getCurrentEngine().load(newRecord.getUrlAddress());
            }
        });
    }


    public void setUpTabOnClick() {
        mainTabPane.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            WebView webView = (WebView) mainTabPane.getSelectionModel().getSelectedItem().getContent();
            if (webView != null) {
                urlTextField.setText(webView.getEngine().getLocation());
            }
        });
    }

    private WebEngine getCurrentEngine() {
        return ((WebView) mainTabPane.getSelectionModel().getSelectedItem().getContent()).getEngine();
    }

    public void zoomOut() {
        double currentZoom = ((WebView) mainTabPane.getSelectionModel().getSelectedItem().getContent()).getZoom();
        if (currentZoom >= 0.5) {
            ((WebView) mainTabPane.getSelectionModel().getSelectedItem().getContent()).setZoom(currentZoom - 0.25);
        }
    }

    public void zoomIn() {
        double currentZoom = ((WebView) mainTabPane.getSelectionModel().getSelectedItem().getContent()).getZoom();
        ((WebView) mainTabPane.getSelectionModel().getSelectedItem().getContent()).setZoom(currentZoom + 0.25);
    }

    public void setBookmarkTitle(TextField bookmarkTitleTextField){
        bookmarkTitleTextField.setText(getCurrentEngine().titleProperty().get());
    }



}
