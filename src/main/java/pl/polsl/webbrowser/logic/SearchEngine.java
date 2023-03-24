package pl.polsl.webbrowser.logic;

public enum SearchEngine {
    GOOGLE("https://www.google.com/search?q="),
    BING("https://www.bing.com/search?q="),
    YAHOO("https://search.yahoo.com/search?p="),
    DUCK("https://duckduckgo.com/?q=");

    private final String urlAddress;

    private SearchEngine(String urlAddress) {
        this.urlAddress = urlAddress;
    }

    public String getUrlAddress() {
        return urlAddress;
    }
}
