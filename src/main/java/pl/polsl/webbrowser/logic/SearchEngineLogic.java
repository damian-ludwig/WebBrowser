package pl.polsl.webbrowser.logic;

import java.io.*;

public class SearchEngineLogic {

    private final String fileName = "searchEngine.obj";

    public String getSearchEngine() {
        File file = new File(fileName);
        if (file.exists()) {
            try (
                    var fileReader = new FileReader(fileName);
                    var bufferedReader = new BufferedReader(fileReader);
            ) {
                String url = bufferedReader.readLine();
                if (url == null) return SearchEngine.GOOGLE.getUrlAddress();
                else return url;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        else return SearchEngine.GOOGLE.getUrlAddress();
    }

    public void setDefaultSearchEngine(SearchEngine searchEngine) {
        try (
                var fileWriter = new FileWriter(fileName, false);
                var bufferedWriter = new BufferedWriter(fileWriter);
        ) {
            bufferedWriter.write(searchEngine.getUrlAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
