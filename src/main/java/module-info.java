//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

module com.example.clientside {
    requires javafx.controls;
    requires javafx.fxml;

    exports view;
    exports model;
    exports test;
    exports viewModel;

    opens view to
            javafx.fxml;
    opens model to
            javafx.fxml;
    opens test to
            javafx.fxml;
    opens viewModel to
            javafx.fxml;
}
