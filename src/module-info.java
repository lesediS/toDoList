module SedateME {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires java.sql;
    requires java.base;
    requires java.desktop;
    requires java.management;
    requires java.sql.rowset;

    opens com.todolist.controller to javafx.fxml;
    exports com.todolist.controller;

    opens application to javafx.graphics, javafx.fxml;
}
