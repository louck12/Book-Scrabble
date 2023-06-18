package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HostViewController {

    @FXML
    private Label welcomeLabel;

    public void setUsername(String username) {
        welcomeLabel.setText("Welcome, " + username + "!");
    }
}
