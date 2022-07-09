package com.candle.fileexplorer.view.helpers;

import com.candle.fileexplorer.core.ViewHandler;
import com.candle.fileexplorer.model.FilesModel;
import com.candle.fileexplorer.model.data.ClipboardMode;
import com.candle.fileexplorer.model.helpers.FileOperations;
import com.candle.fileexplorer.view.FileGridController;
import com.candle.fileexplorer.view.enums.GridSortOrder;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.ArrayList;

/**
 * A helper class that is used by the context menus to perform operations.
 */
public class ContextMenuActions {
    //region Constructors

    //endregion

    //region Public Methods

    /**
     * Opens up the "create new item" dialog window.
     *
     * @param parentDirectory The absolute path to the new file/folder's
     *                        parent directory.
     */
    public void createNewItem(String parentDirectory) {
        if (parentDirectory.equals(""))
            return;

        if (pathCanBeModified(parentDirectory)) {
            try {
                ViewHandler.getInstance().openSubView("NewFile", "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                ViewHandler.getInstance().openSubView("Error", "A file/folder" +
                        " cannot be created in this directory. It may be " +
                        "read-only.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Opens the "rename item" dialog window.
     *
     * @param path The absolute path to the file/folder to rename.
     */
    public void renameItem(String path) {
        if (path.equals(""))
            return;

        if (pathCanBeModified(path)) {
            try {
                ViewHandler.getInstance().openSubView("Rename", path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                ViewHandler.getInstance().openSubView("Error", "The selected " +
                        "file/folder could not be renamed. It may be " +
                        "read-only.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends an item to the trash bin.
     *
     * @param path      The absolute path to the file/folder to trash.
     * @param dataModel A reference to the data model
     */
    public void trashItem(String path, FilesModel dataModel) {
        if (path.equals(""))
            return;

        if (pathCanBeModified(path)) {
            try {
                dataModel.trashItem(path);
            } catch (IllegalStateException e) {
                try {
                    String dependencyError = """
                            The 'trash-cli' dependency could not be found.

                            Since you are using a Linux-based operating system, this package is necessary in order to perform operations involving the Trash Bin.

                            The dependency should be installable via your package manager's repositories. Alternatively, there is a link to the GitHub page in the Help -> About Files -> Components section.
                            """;
                    ViewHandler.getInstance().openSubView("Error",
                            dependencyError);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else {
            try {
                ViewHandler.getInstance().openSubView("Error", "The selected " +
                        "file/folder could not be deleted. It may be " +
                        "read-only.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Adds a file/folder to the clipboard and sets up the data model to
     * paste it somewhere with the "cut" operation.
     *
     * @param path      The absolute path to the file/folder.
     * @param dataModel A reference to the data model.
     */
    public void cutItem(String path, FilesModel dataModel) {
        if (path.equals(""))
            return;

        if (pathCanBeModified(path)) {
            addItemToClipboard(path);
            dataModel.setClipboardMode(ClipboardMode.Cut);
        } else {
            try {
                ViewHandler.getInstance().openSubView("Error", "The selected " +
                        "file/folder could not be cut. It may be read-only.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Adds a file/folder to the clipboard and sets up the data model to
     * paste it somewhere with the "copy" operation.
     *
     * @param path      The absolute path to the file/folder.
     * @param dataModel A reference to the data model.
     */
    public void copyItem(String path, FilesModel dataModel) {
        if (path.equals(""))
            return;

        addItemToClipboard(path);
        dataModel.setClipboardMode(ClipboardMode.Copy);
    }

    /**
     * Adds a file/folder's location to the clipboard.
     *
     * @param path The absolute path to the file/folder.
     */
    public void copyItemLocation(String path) {
        if (path.equals(""))
            return;

        ClipboardContent content = new ClipboardContent();
        content.putString(path);
        Clipboard.getSystemClipboard().setContent(content);
    }

    /**
     * Pastes the item that is currently on the clipboard to the current
     * directory.
     *
     * @param dataModel A reference to the data model.
     */
    public void pasteItem(FilesModel dataModel) {
        ArrayList<File> itemList;
        try {
            itemList =
                    (ArrayList<File>) Clipboard.getSystemClipboard().getFiles();
            if (itemList == null)
                return;
        } catch (ClassCastException e) {
            return;
        }
        if (pathCanBeModified(dataModel.getCurrentDirectory())) {
            try {
                pasteContentsList(itemList, dataModel);
            } catch (IllegalStateException e) {
                try {
                    String dependencyError = "There is a file/folder with an identical name " +
                            "in this directory.";
                    ViewHandler.getInstance().openSubView("Error", dependencyError);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else {
            try {
                ViewHandler.getInstance().openSubView("Error", "The selected " +
                        "file/folder could not be pasted into this directory." +
                        " It may be read-only.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Tells the specified view to update its sort order to the specified value.
     *
     * @param view      The grid view to update.
     * @param sortOrder The new sort order.
     */
    public void sortBy(FileGridController view, GridSortOrder sortOrder) {
        view.setSortOrder(sortOrder);
    }

    /**
     * Opens the specified file/folder in the user's default application.
     *
     * @param path The absolute path to the file/folder.
     */
    public void openItemInDefaultApp(String path) {
        FileOperations.openFileInDefaultApp(path);
    }

    //endregion

    //region Private Helper Methods

    /**
     * Returns whether the given file/folder can be modified.
     *
     * @param path The absolute path to the file/folder.
     */
    private boolean pathCanBeModified(String path) {
        File item = new File(path);
        return item.canWrite();
    }

    /**
     * Adds the file/folder at the specified path to the clipboard
     *
     * @param path The absolute path to the file/folder
     */
    private void addItemToClipboard(String path) {
        File item = new File(path);

        ClipboardContent content = new ClipboardContent();
        ArrayList<File> fileList = new ArrayList<>();
        fileList.add(item);
        content.putFiles(fileList);
        Clipboard.getSystemClipboard().setContent(content);
    }

    /**
     * Tells the data model to paste a specified list of items to the current
     * directory.
     *
     * @param itemList A list of file items
     */
    private void pasteContentsList(ArrayList<File> itemList,
                                   FilesModel dataModel) {
        for (File item : itemList) {
            try {
                dataModel.paste(item.getAbsolutePath());
            } catch (FileSystemException e) {
                try {
                    ViewHandler.getInstance().openSubView("Error",
                            "The file/folder you are trying to paste is " +
                                    "currently in use by another application. " +
                                    "This may result in the copy process completing " +
                                    "incorrectly.\n\n" +
                                    "In order to ensure that the files are pasted properly, " +
                                    "please close the other app and try again.");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    //endregion
}
