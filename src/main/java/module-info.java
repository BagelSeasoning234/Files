module com.candle.fileexplorer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.junit.jupiter.api;

    exports com.candle.fileexplorer;
    opens com.candle.fileexplorer to javafx.graphics;
    opens com.candle.fileexplorer.core to javafx.fxml;
    exports com.candle.fileexplorer.model;
    opens com.candle.fileexplorer.model to javafx.fxml;
    exports com.candle.fileexplorer.model.data;
    opens com.candle.fileexplorer.model.data to javafx.fxml;
    exports com.candle.fileexplorer.viewmodel;
    opens com.candle.fileexplorer.viewmodel to javafx.fxml;
    exports com.candle.fileexplorer.view;
    opens com.candle.fileexplorer.view to javafx.fxml;
    exports com.candle.fileexplorer.view.enums;
    opens com.candle.fileexplorer.view.enums to javafx.fxml;

}