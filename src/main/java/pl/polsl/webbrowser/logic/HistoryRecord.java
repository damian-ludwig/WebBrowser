package pl.polsl.webbrowser.logic;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoryRecord implements Serializable {
    private String title;
    private String shortUrlAddress;
    private String urlAddress;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private String dateTime;
    private String shortTitle;

    public HistoryRecord(String title, String shortUrlAddress, String urlAddress) {
        this.title = title;
        this.shortUrlAddress = shortUrlAddress;
        this.urlAddress = urlAddress;
        Date date = new Date();
        dateTime = dateFormat.format(date);
        shortTitle = title.substring(0, Math.min(title.length(), 20));
    }

    public String getUrlAddress(){
        return urlAddress;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public String getTitle() {
        return title;
    }

    public String getShortUrlAddress() {
        return shortUrlAddress;
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    @Override
    public String toString() {
        String shortTitleFormatted = String.format("%-20s", shortTitle);
        String shortUrlAddressFormatted = String.format("%-20s", shortUrlAddress);

        return (shortTitleFormatted + " - " + shortUrlAddressFormatted + " - " + dateTime);
    }
}
