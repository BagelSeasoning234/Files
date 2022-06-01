module com.candle.fileexplorer {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.junit.jupiter.api;


    opens com.candle.fileexplorer to javafx.fxml;
    exports com.candle.fileexplorer;
    exports com.candle.fileexplorer.core;
    opens com.candle.fileexplorer.core to javafx.fxml;
    exports com.candle.fileexplorer.view;
    opens com.candle.fileexplorer.view to javafx.fxml;
    exports com.candle.fileexplorer.view.enums;
    opens com.candle.fileexplorer.view.enums to javafx.fxml;
}