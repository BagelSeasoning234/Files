module FileExplorer.main {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires org.apache.commons.io;
    requires java.desktop;

    exports com.candle.fileexplorer to javafx.graphics;
    opens com.candle.fileexplorer.view to javafx.fxml;
}