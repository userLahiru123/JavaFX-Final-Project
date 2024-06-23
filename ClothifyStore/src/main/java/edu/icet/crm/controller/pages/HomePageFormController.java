package edu.icet.crm.controller.pages;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class HomePageFormController implements Initializable {
    public Label lblName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblName.setText("Hi, " + LoginPageFormController.employeeName + "..............");
    }
}
