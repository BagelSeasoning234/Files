package com.candle.fileexplorer.model.helpers;

import javafx.concurrent.Task;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * A helper class that is used to open files with the user's default applications.
 */
public class FileOpener {
    //region Public Methods

    /**
     * Opens a given file in the user's default application.
     * @param path The absolute path to the file.
     */
    public static void openFileInDefaultApp(String path) {
        // Opens the file in a new thread to prevent the application from hanging indefinitely.
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try {
                    Desktop.getDesktop().open(new File(path));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    //endregion

    //region Private Helper Methods

    //endregion
}
