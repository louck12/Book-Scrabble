module com.example.clientside {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.clientside to javafx.fxml;
    exports com.example.clientside;
}