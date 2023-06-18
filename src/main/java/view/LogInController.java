package view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Host;
import model.Model;
import viewModel.ViewModelGuest;
import viewModel.ViewModelHost;

import java.io.IOException;

public class LogInController {

    @FXML
    private TextField nameTextField;

    @FXML
    private ChoiceBox<String> roleChoiceBox;

    @FXML
    private Button loginButton;

    @FXML
    private VBox vbox;


    public void initialize() {
        roleChoiceBox.setItems(FXCollections.observableArrayList("Host", "Guest"));
    }

    @FXML
    private void playButtonClicked() {
        String username = nameTextField.getText();
        String role = roleChoiceBox.getValue();

        if (role != null && !role.isEmpty()) {
            if (role.equals("Host")) {
                Model m = new Model(new Host(8080));
                ViewModelHost vm = new ViewModelHost(m);

                try{
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("host-view.fxml"));
                    Parent welcomePageRoot = fxmlLoader.load();
                    HostViewController welcomeController = fxmlLoader.getController();
                    welcomeController.setUsername(username);

                    Scene welcomePageScene = new Scene(welcomePageRoot);
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    stage.setScene(welcomePageScene);
                } catch (IOException e){
                    System.out.println("Cannot move host to his designated page");
                }

            } else if (role.equals("Guest")) {
                try{
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
                    BorderPane welcomePageRoot = fxmlLoader.load();

                    ViewModelGuest vm = new ViewModelGuest();
                    WindowController wc = fxmlLoader.getController();
                    wc.setMyUserName(username);
                    wc.setViewModel(vm);

                    Scene welcomePageScene = new Scene(welcomePageRoot);
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    stage.setScene(welcomePageScene);

                }catch(IOException e){
                    System.out.println("Cannot move guest to his designated page");
                }
            }
        }
    }
}
