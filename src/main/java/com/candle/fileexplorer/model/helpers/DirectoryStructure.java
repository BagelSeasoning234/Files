package com.candle.fileexplorer.model.helpers;

import com.candle.fileexplorer.model.data.FileItem;
import com.candle.fileexplorer.model.data.DefaultFileItem;
import com.candle.fileexplorer.model.data.FileType;

import java.io.*;
import java.util.ArrayList;

/**
 * A helper class that gets information about file directories.
 */
public class DirectoryStructure {
    //region Public Methods

    /**
     * Returns the name of the home user folder.
     */
    public static String getHomeDirectoryName() {
        File homefolder = new File(System.getProperty("user.home"));
        return homefolder.getName();
    }

    /**
     * Returns a list of the mounted drives.
     */
    public static ArrayList<FileItem> getDrives() {
        String OS = System.getProperty("os.name");
        if (OS.equals("Linux"))
            return getLinuxDrives();
        else {
            ArrayList<FileItem> list = new ArrayList<>();
            for (File file : File.listRoots())
                list.add(new DefaultFileItem(FileType.Drive, file.getPath()));
            return list;
        }
    }

    /**
     * Get the top level contents of a given directory.
     */
    public static ArrayList<FileItem> getDirectoryContents(String path, boolean showHiddenItems) {
        File currentDirectory = new File(FileOperations.sanitizePath(path));
        if (!currentDirectory.isDirectory())
            return new ArrayList<>();

        ArrayList<FileItem> result = new ArrayList<>();
        File[] contents = currentDirectory.listFiles();
        if (contents != null && contents.length > 0) {
            FileType type;
            for (File subFile : contents) {
                // Unless we're showing hidden files, skip to the next iteration if found.
                if (!showHiddenItems) {
                    if (subFile.isHidden())
                        continue;
                }
                type = (subFile.isDirectory() ? FileType.Folder : FileType.File);
                result.add(new DefaultFileItem(type, subFile.getPath()));
            }
        }
        return result;
    }

    //endregion

    //region Private Methods

    private static ArrayList<FileItem> getLinuxDrives() {
        // Unfortunately, listRoots() doesn't work on linux and only returns /.
        // Instead, I have to go through /proc/mounts and look for /dev/sd* drives
        String mountedDrives = "/proc/mounts";
        String textToFind = "/dev/";

        try {
            // Define the file readers
            FileReader fileReader = new FileReader(mountedDrives);
            BufferedReader reader = new BufferedReader(fileReader);
            // A string to store the lines
            String line;
            ArrayList<String> driveStrings = new ArrayList<>();
            ArrayList<FileItem> files = new ArrayList<>();

            while (true) {
                try {
                    // Loop until we've gone through all the lines.
                    line = reader.readLine();
                    if (line == null)
                        break;

                    int index = line.indexOf(textToFind);
                    if (index != -1) {
                        // Knowing that this is a /dev/... line, use spacers to get the beginning and end of the name.
                        int startIndex = line.indexOf(" ") + 1;
                        int endIndex = line.indexOf(" ", startIndex);

                        String driveName = line.substring(startIndex, endIndex);
                        String sanitizedVersion = validateDrive(driveName);
                        if (sanitizedVersion != null)
                            driveStrings.add(sanitizedVersion);
                    }

                } catch (IOException e) {
                    System.out.println("Error reading file line: " + e.getMessage());
                }
            }

            for (String drive : driveStrings) {
                files.add(new DefaultFileItem(FileType.Drive, drive));
            }
            return files;
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Checks to see if a given string returns a valid drive name by cutting out boot partitions.
     */
    private static String validateDrive(String drive) {
        String[] badDriveNames = {"boot", "timeshift", "shm", "pts", "mqueue", "hugepages"};
        for (String badName : badDriveNames)
            if (drive.contains(badName))
                return null;

        // If everything worked, sanitize the string and return it.
        return FileOperations.sanitizePath(drive);
    }


    //endregion
}
