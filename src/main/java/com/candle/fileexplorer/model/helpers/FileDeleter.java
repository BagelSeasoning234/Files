package com.candle.fileexplorer.model.helpers;

import java.io.*;
import java.nio.file.StandardCopyOption;
import java.time.*;
import java.time.format.DateTimeFormatter;

import static org.apache.commons.io.FileUtils.moveDirectory;
import static org.apache.commons.io.FileUtils.moveFile;

/**
 * The class that is used to move items to the trash.
 */
public class FileDeleter {
    //region Private Static Members

    /**
     * The location of the trash folder on Linux-based devices.
     */
    private static final String linuxTrashLocation = System.getProperty("user.home") + "/.local/share/Trash";

    /**
     * The location of the trashed files/folders on Linux-based devices.
     */
    private static final String linuxTrashFilesLocation = System.getProperty("user.home") + "/.local/share/Trash/files";

    /**
     * The location of the folder that contains information about trashed files/folders.
     */
    private static final String linuxTrashInfoLocation = System.getProperty("user.home") + "/.local/share/Trash/info";

    private static final String windowsTrashLocation = "C:\\$Recycle.Bin";

    private static String timeZone = "America/Denver";

    //endregion

    //region Public Methods

    /**
     * Returns the trash folder directory.
     */
    public static String getTrashDirectory() {
        String OS = System.getProperty("os.name");
        switch (OS) {
            case "Linux" -> {
                return linuxTrashLocation;
            }
            case "Windows" -> {
                return windowsTrashLocation;
            }
        }
        return null;
    }

    /**
     * Sends a given file/folder to the recycle bin.
     * @param path The absolute path of the file/folder to be deleted.
     */
    public static boolean sendItemToTrash(String path) {
        String OS = System.getProperty("os.name");
        if (OS.equals("Linux")) {
                ProcessBuilder builder = new ProcessBuilder();
                if (trashDependencyExists(builder))
                    return runTrashShellCommand(builder, path);
                else {
                    System.out.println("The 'trash-cli' dependency could not be found.");
                    System.out.println("Since you are using a Linux-based operating system, this package is necessary in order to perform operations involving the Trash Bin.");
                    System.out.println("The dependency should be installable via your package manager's repositories, or at 'https://github.com/andreafrancia/trash-cli'.");
                    return false;
                }
        }
        else
            return java.awt.Desktop.getDesktop().moveToTrash(new File(path));
    }

    //endregion

    //region Private Helper Methods

    /**
     * Checks to see if the "trash-put" command is available on Linux-based devices.
     * @param builder The process builder object used to run shell commands.
     * @return Whether the command is available.
     */
    private static boolean trashDependencyExists(ProcessBuilder builder) {
        boolean success = false;

        try {
            builder.command("which", "trash-put");
            Process process = builder.start();

            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            process.waitFor();

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                if (output.indexOf("trash-put not found") == -1) {
                    success = true;
                    break;
                }
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
    private static boolean runTrashShellCommand(ProcessBuilder builder, String path) {
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

    /**
     * Gets the file/folder name and extension (if present) of the given path
     * @param path The path to check.
     */
    private static String getPathNameAndExtension(String path) {
        int startIndex = path.lastIndexOf("/") + 1;
        try {
            return path.substring(startIndex);
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the current time in YYYY MM DD T HH:MM:SS format.
     */
    private static String getLocalTime() {
        ZoneId id = ZoneId.systemDefault();;
        ZonedDateTime time = ZonedDateTime.now(id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss");
        return time.format(formatter);
    }

    //endregion
}
