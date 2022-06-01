package com.candle.fileexplorer.viewmodel;

import com.candle.fileexplorer.model.data.FileItem;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

import java.nio.file.attribute.FileTime;

public class FileItemViewModel
{
    //region Private Members

    /**
     * The name of the file/folder/drive in this directory that is exposed to the view.
     */
    private StringProperty fileName;

    /**
     * The type of item contained in this directory that is exposed to the view.
     */
    private ObjectProperty<Image> imagePreview;

    /**
     * The data object to which this view model refers.
     */
    private FileItem fileItem;

    //endregion

    //region Constructor

    /**
     * Initializes the data property values by referring to the model.
     * @param item A reference to the data item
     */
    public FileItemViewModel(FileItem item)
    {
        this.fileItem = item;
        fileName = new SimpleStringProperty();
        imagePreview = new SimpleObjectProperty<>();

        updateFile();
    }

    //endregion

    //region Public Methods

    /**
     * Updates the data by referring to the model.
     */
    public void updateFile()
    {
        fileName.setValue(fileItem.getFileName());

        switch (fileItem.getFileType())
        {
            case File -> imagePreview.setValue(new Image("/com/candle/fileexplorer/images/64/File.png"));
            case Folder -> imagePreview.setValue(new Image("/com/candle/fileexplorer/images/64/Folder.png"));
            case Drive -> imagePreview.setValue(new Image("/com/candle/fileexplorer/images/64/Drive.png"));
        }
    }

    public String getItemDirectory()
    {
        return fileItem.getItemDirectory();
    }

    public String getFileName()
    {
        return fileName.getValue();
    }

    public StringProperty fileNameProperty()
    {
        return fileName;
    }

    public ObjectProperty<Image> imagePreviewProperty()
    {
        return imagePreview;
    }

    public long getFileSize()
    {
        return fileItem.getFileSize();
    }

    public FileTime getLastModifiedTime()
    {
        return fileItem.getLastModifiedTime();
    }

    //endregion
}
