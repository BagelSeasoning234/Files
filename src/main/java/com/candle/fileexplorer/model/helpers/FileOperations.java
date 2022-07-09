package com.candle.fileexplorer.model.helpers;

import com.candle.fileexplorer.model.data.FileType;
import javafx.concurrent.Task;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A helper class that contains static methods for various file utilities,
 * such as sanitizing directory paths, opening files, and moving them to the
 * recycle bin.
 */
public class FileOperations {

    //region Private Members

    private static final String linuxTrashLocation = System.getProperty("user" +
            ".home") + "/.local/share/Trash";
    private static final String windowsTrashLocation = "C:/$Recycle.Bin/Recycle Bin";

    //endregion

    //region Public Methods

    /**
     * Returns the name of the given file/folder.
     * This is just the last name in the path's name sequence (after "/").
     *
     * @param path The absolute path to the file/folder.
     */
    public static String getPathName(String path) {
        File temp = new File(path);
        return temp.getName();
    }

    /**
     * Cleans a directory path by adding spaces where necessary and switching
     * ~ for the home directory.
     */
    public static String sanitizePath(String path) {
        path = path.replace("\\040", " ");
        if (System.getProperty("os.name").equals("Linux")) {
            path = path.replace("~", System.getProperty("user.home"));
        }
        path = path.replace("\\", "/");
        return path;
    }

    /**
     * Tries to determine if the item at the given path is a file or a folder.
     * NOTE: This method should only be used when the file/folder currently
     * exists on the user's disk,
     * as otherwise it will automatically return a file.
     *
     * @param path The absolute path to the file/folder.
     * @return The filetype that was determined.
     */
    public static FileType determineType(String path) {
        Path pathObject = Path.of(path);
        if (Files.exists(pathObject))
            return Files.isDirectory(pathObject) ? FileType.Folder : FileType.File;
        else
            return FileType.File;
    }

    /**
     * Opens a given file in the user's default application.
     *
     * @param path The absolute path to the file.
     */
    public static void openFileInDefaultApp(String path) {
        // Opens the file in a new thread to prevent the application from
        // hanging indefinitely.
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

    /**
     * Returns the trash folder directory.
     */
    public static String getTrashDirectory() {
        String OS = System.getProperty("os.name");
        if (OS.equals("Linux")) {
            return linuxTrashLocation;
        } else {
            return windowsTrashLocation;
        }
    }

    /**
     * Sends a given file/folder to the recycle bin.
     *
     * @param path The absolute path of the file/folder to be deleted.
     */
    public static boolean sendItemToTrash(String path) {
        String OS = System.getProperty("os.name");
        if (OS.equals("Linux")) {
            ProcessBuilder builder = new ProcessBuilder();
            if (trashDependencyExists(builder))
                return runTrashShellCommand(builder, path);
            else {
                throw new IllegalStateException();
            }
        } else
            return java.awt.Desktop.getDesktop().moveToTrash(new File(path));
    }

    //endregion

    //region Private Helper Methods

    /**
     * Checks to see if the "trash-put" command is available on Linux-based
     * devices.
     *
     * @param builder The process builder object used to run shell commands.
     * @return Whether the command is available.
     */
    private static boolean trashDependencyExists(ProcessBuilder builder) {
        boolean success = false;

        try {
            builder.command("which", "trash-put");
            Process process = builder.start();

            StringBuilder output = new StringBuilder();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));

            process.waitFor();

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                if (output.indexOf("trash-put not found") == -1) {
                    success = true;
                    break;
                }
                // For some reason, this works if the "if" statement is
                // checked inside the while loop,
                // but not if it's checked after the loop finishes.
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return success;
    }

    /**
     * Runs the "trash-put" command on Linux-based devices.
     *
     * @param builder The process builder object used to run shell commands.
     * @param path    The absolute path of the file/folder to be deleted.
     * @return Whether the operation was performed successfully.
     */
    private static boolean runTrashShellCommand(ProcessBuilder builder,
                                                String path) {
        try {
            builder.command("trash-put", path);
            Process process = builder.start();
            process.waitFor();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    //endregion
}
