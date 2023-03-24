package pl.polsl.webbrowser.logic;

import javafx.scene.control.TextField;

import java.io.*;

public class StartupPageLogic {

    private static final String FILE_NAME = "startup.page";
    private static String startupPageUrlAddress = "https://google.com";
    private TextField startupPageTextField;

    public StartupPageLogic(TextField startupPageTextField){
        this.startupPageTextField = startupPageTextField;
    }

    public static String getStartupPageUrlAddress() {
        return startupPageUrlAddress;
    }

    public void setStartupPageUrlAddress(String startupPageUrlAddress) {
        this.startupPageUrlAddress = startupPageUrlAddress;
        startupPageTextField.setText(startupPageUrlAddress);
    }

    public void loadStartupPageUrlAddress() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (
                    var fileReader = new FileReader(file);
                    var bufferedReader = new BufferedReader(fileReader);
            ) {
                String urlAddress = bufferedReader.readLine();
                setStartupPageUrlAddress(urlAddress);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveStartupPageUrlAddress() {
        try (
                var fileWriter = new FileWriter(FILE_NAME);
                var bufferedWriter = new BufferedWriter(fileWriter);
        ) {
            fileWriter.write(getStartupPageUrlAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
