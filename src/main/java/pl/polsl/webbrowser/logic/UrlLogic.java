package pl.polsl.webbrowser.logic;


import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UrlLogic {

    private final Pattern pattern = Pattern.compile("^(https?:\\/\\/)?"+
            "((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.)+[a-z]{2,}|"+
            "((\\d{1,3}\\.){3}\\d{1,3}))"+
            "(\\:\\d+)?(\\/[-a-z\\d%_.~+]*)*"+
            "(\\?[;&a-z\\d%_.~+=-]*)?"+
            "(\\#[-a-z\\d_]*)?$");


    public String transformURL(String urlAddress) {
        if (!(urlAddress.startsWith("https://") || urlAddress.startsWith("http://"))){
            if (urlAddress.startsWith("www.")) return "https://" + urlAddress;
            else return "https://www." + urlAddress;
        }
        return urlAddress;
    }

    public boolean checkIfValidUrl(String phrase){
        Matcher matcher = pattern.matcher(phrase);
        return matcher.matches();
    }

    public String shortenUrl(String urlAddress){
        int indexStart = urlAddress.indexOf("://") + 3;
        int indexEnd = urlAddress.indexOf('/', indexStart);
        String shortUrl = urlAddress.substring(indexStart, indexEnd);
        if (shortUrl.startsWith("www.")) return shortUrl.substring(4);
        else return shortUrl;
    }


}
