module my.database.maliapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens my.database.maliapp to javafx.fxml;
    exports my.database.maliapp;
    exports my.database.maliapp.tablas;
    opens my.database.maliapp.tablas to javafx.fxml;
    exports my.database.maliapp.modelos;
    opens my.database.maliapp.modelos to javafx.fxml;
    exports my.database.maliapp.roles.empleado;
    opens my.database.maliapp.roles.empleado to javafx.fxml;
}