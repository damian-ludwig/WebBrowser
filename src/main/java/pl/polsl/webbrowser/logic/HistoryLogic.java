package pl.polsl.webbrowser.logic;


import javafx.scene.control.ListView;

import java.io.*;


public class HistoryLogic {
    private static ListView<HistoryRecord> historyListView;
    private static final String LIST_FILE_NAME = "history.list";
    private static final String COUNT_FILE_NAME = "history.count";
    private final String exportFileName = "history.csv";

    public HistoryLogic(ListView<HistoryRecord> historyListView) {
        this.historyListView = historyListView;
    }


    public void addToHistory(String title, String shortUrlAddress, String urlAddress) {

        HistoryRecord historyRecord = new HistoryRecord(title, shortUrlAddress, urlAddress);

        if (historyListView.getItems().size() > 1) {
            if (historyListView.getItems().get(historyListView.getItems().size() - 1).toString().contains(historyRecord.getShortTitle()) &&
                    historyListView.getItems().get(historyListView.getItems().size() - 1).toString().contains(historyRecord.getDateTime())) {
                historyListView.getItems().set((historyListView.getItems().size() - 1), historyRecord);
            } else historyListView.getItems().add(historyRecord);
        } else historyListView.getItems().add(historyRecord);

    }

    public void exportHistoryList() {
        try (
                var fileWriter = new FileWriter(exportFileName, false);
                var bufferedWriter = new BufferedWriter(fileWriter);
        ) {
            for (HistoryRecord historyRecord : historyListView.getItems()) {
                bufferedWriter.write(historyRecord.getDateTime() + ";" + historyRecord.getTitle() + ";"
                        + historyRecord.getShortUrlAddress() + ";" + historyRecord.getUrlAddress() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadHistoryList() {
        File file = new File(LIST_FILE_NAME);
        int elements = loadHistoryCount();
        if (elements > 0 && file.exists()) {
            try (
                    var fileInputStream = new FileInputStream(file);//filename
                    var objectInputStream = new ObjectInputStream(fileInputStream);
            ) {
                for (int i = 0; i < elements; i++) {
                    historyListView.getItems().add((HistoryRecord) objectInputStream.readObject());
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveHistoryList() {
        try (
                var fileOutputStream = new FileOutputStream(LIST_FILE_NAME);
                var objectOutputStream = new ObjectOutputStream(fileOutputStream);
        ) {
            for (HistoryRecord historyRecord : historyListView.getItems()) {
                objectOutputStream.writeObject(historyRecord);
            }
            saveHistoryCount();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void clearHistory() {
        historyListView.getItems().clear();
        File file = new File(LIST_FILE_NAME);
        if (file.exists()) {
            try (
                    var fileWriter = new FileWriter(file, false);
                    var bufferedWriter = new BufferedWriter(fileWriter);
            ) {
                bufferedWriter.write("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static void saveHistoryCount() {
        try (
                var fileWriter = new FileWriter(COUNT_FILE_NAME, false);
                var bufferedWriter = new BufferedWriter(fileWriter);
        ) {
            bufferedWriter.write("" + historyListView.getItems().size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int loadHistoryCount() {
        File file = new File(COUNT_FILE_NAME);
        if (file.exists()) {
            try (
                    var fileReader = new FileReader(COUNT_FILE_NAME);
                    var bufferedReader = new BufferedReader(fileReader);
            ) {
                String count = bufferedReader.readLine();
                if (count == null) return 0;
                else {
                    //System.out.println(Integer.valueOf(count));
                    return Integer.valueOf(count);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        } else return 0;
    }


}
