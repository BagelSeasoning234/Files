package com.candle.fileexplorer.viewmodel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class AboutViewModel {
    //region Public Methods

    /**
     * Gets the version of the application.
     */
    public String getFilesVersion() {
        Properties properties = null;
        FileInputStream stream = null;
        String version = null;
        try {
            String location = System.getProperty("user.dir") + "/build" +
                    "/resources/version.properties";
            stream = new FileInputStream(location);
            properties = new Properties();
            properties.load(stream);

            version = properties.getProperty("version");
            stream.close();
        } catch (FileNotFoundException e) {
            System.err.println("The properties file could not be found.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return version;
    }

    /**
     * Gets the version of the Javafx runtime.
     */
    public String getJavaFxVersion() {
        return System.getProperty("javafx.runtime.version");
    }

    public String getJavaVersion() {
        return System.getProperty("java.version");
    }

    //endregion
}
