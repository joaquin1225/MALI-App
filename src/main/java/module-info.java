module my.database.maliapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens my.database.maliapp to javafx.fxml;
    exports my.database.maliapp;
}