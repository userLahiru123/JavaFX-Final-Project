package edu.icet.crm.controller.pages;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class DashboardForEmployeeFormController implements Initializable {
    public Circle circle;
    public Label lblDate;
    public Label lblHome;
    public Label lblProduct;
    public BorderPane borderPane;
    public AnchorPane anchorPane;
    public Label lblAboutUs;
    public Label lblSupplier;
    public Label lblOrder;
    public Label lblReports;
    public JFXButton btnSignOut;

    private List<Label> labelList;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = new Image("/view/assets/profile.jpg");
        circle.setFill(new ImagePattern(image));

        //load date....
        loadDateTime();

        labelList = new ArrayList<>();
        labelList.add(lblHome);
        labelList.add(lblProduct);
        labelList.add(lblOrder);
        labelList.add(lblReports);
        labelList.add(lblAboutUs);
        labelList.add(lblSupplier);

        //load home page.......
        loadPage("home-page-form");
        changeLabelColor(lblHome);
    }

    private void loadDateTime() {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("dd-MMMM-yyyy");
        lblDate.setText(f.format(date));
    }

    public void lblHomeOnAction() {
        loadPage("home-page-form");
//        borderPane.setCenter(anchorPane);
        changeLabelColor(lblHome);
    }

    public void lblProductOnAction() {
        loadPage("products-page-form");
        changeLabelColor(lblProduct);
    }

    public void lblAboutUsOnAction() {
        loadPage("aboutUs-page-form");
        changeLabelColor(lblAboutUs);
    }

    public void lblSupplierOnAction() {
        loadPage("supplier-page-form");
        changeLabelColor(lblSupplier);
    }

    public void lblOrderOnAction() {
        loadPage("order-page-form");
        changeLabelColor(lblOrder);
    }

    public void lblReportsOnAction() {
        loadPage("reports-page-form");
        changeLabelColor(lblReports);
    }

    public void loadPage(String page){
        Parent root;

        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/" + page + ".fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        borderPane.setCenter(root);
    }

    private void changeLabelColor(Label label){
        Color themeColor = Color.web("#FE7C03");

        labelList.forEach(label1 -> {
            if(label1.equals(label)){
                label1.setBackground(new Background(
                        new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                label1.setTextFill(themeColor);
            }else {
                label1.setBackground(new Background(
                        new BackgroundFill(themeColor, CornerRadii.EMPTY, Insets.EMPTY)));
                label1.setTextFill(Color.WHITE);
            }
        });
    }

    public void btnSignOutOnAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sign out..?");
        Optional<ButtonType> alertButton = alert.showAndWait();

        if (alertButton.isPresent() && (alertButton.get() == ButtonType.OK)) {
            try {
                Stage currentStage = (Stage) btnSignOut.getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/login-page-form.fxml"));
                Parent root1 = fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root1));
                stage.show();
                currentStage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
