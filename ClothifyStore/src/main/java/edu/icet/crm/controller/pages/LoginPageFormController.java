package edu.icet.crm.controller.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import edu.icet.crm.util.CrudUtil;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginPageFormController implements Initializable {
    public JFXPasswordField txtPassword;
    public JFXTextField txtUserName;
    public JFXButton btnLogin;
    public Label lblErrorMsg;
    public static String employeeId;
    public static String employeeName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblErrorMsg.setVisible(false);
    }

    public void btnLoginOnAction() {
        if (!txtUserName.getText().isEmpty() && !txtPassword.getText().isEmpty()) {
            if (isValidated()) {
                try {
                    int size = 0;
                    String password = "";
                    String role = "";
                    ResultSet resultSet = CrudUtil.execute("SELECT id,password,role,name FROM employee WHERE email='" + txtUserName.getText() + "'");
                    while (resultSet.next()) {
                        size++;
                        employeeId = resultSet.getString(1);
                        password = resultSet.getString(2);
                        role = resultSet.getString(3);
                        employeeName = resultSet.getString(4);
                    }

                    if (size == 1) {
                        if (decryptPassword(password).equals(txtPassword.getText())) {
                            if (role.equals("admin")) {
                                try {
                                    Stage currentStage = (Stage) btnLogin.getScene().getWindow();
                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/dashboard-form.fxml"));
                                    Parent root1 = fxmlLoader.load();
                                    Stage stage = new Stage();
                                    stage.setScene(new Scene(root1));
                                    stage.show();
                                    currentStage.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    Stage currentStage = (Stage) btnLogin.getScene().getWindow();
                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/dashboard-for-employee-page-form.fxml"));
                                    Parent root1 = fxmlLoader.load();
                                    Stage stage = new Stage();
                                    stage.setScene(new Scene(root1));
                                    stage.show();
                                    currentStage.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            lblErrorMsg.setVisible(true);
                        }
                    } else {
                        lblErrorMsg.setVisible(true);
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else {
                lblErrorMsg.setVisible(true);
            }
        }
    }

    private boolean isValidated() {
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_.±]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$");
        Matcher emailMatcher = emailPattern.matcher(txtUserName.getText());

        return emailMatcher.find();
    }

    private String decryptPassword(String encodedValue) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] bytes = decoder.decode(encodedValue);

        return new String(bytes);
    }

    public void txtUserNameOnAction() {
        lblErrorMsg.setVisible(false);
    }

    public void txtPasswordOnAction() {
        lblErrorMsg.setVisible(false);
    }
}
