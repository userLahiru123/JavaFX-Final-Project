package edu.icet.crm.controller.employee;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.icet.crm.bo.BoFactory;
import edu.icet.crm.bo.custom.EmployeeBo;
import edu.icet.crm.db.DBConnection;
import edu.icet.crm.dto.Employee;
import edu.icet.crm.dto.tm.EmployeeTbl1;
import edu.icet.crm.dto.tm.EmployeeTbl2;
import edu.icet.crm.util.BoType;
import edu.icet.crm.util.CrudUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StaffPageController implements Initializable {
    public TextField txtSearch;
    public JFXTextField txtName;
    public JFXTextField txtAddress;
    public JFXTextField txtEmail;
    public JFXTextField txtNic;
    public JFXButton btnAdd;
    public JFXButton btnClear;
    public JFXButton btnUpdate;
    public JFXButton btnRemove;
    public JFXTextField txtPassword;

    private final EmployeeBo employeeBo = BoFactory.getInstance().getBo(BoType.EMPLOYEE);
    public TableView tblEmployee1;
    public TableColumn colName;
    public TableColumn colEmpId1;
    public TableColumn colAddress;
    public TableColumn colNic;
    public TableView tblEmployee2;
    public TableColumn colEmpId2;
    public TableColumn colName2;
    public TableColumn colEmail;
    public TableColumn colPassword;
    public Label lblErrorMsg;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colEmpId1.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colEmpId2.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName2.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));

        loadTable1();
        loadTable2();
        lblErrorMsg.setVisible(false);

    }

    private void loadTable1() {
        ObservableList<EmployeeTbl1> tbl01 = FXCollections.observableArrayList();
        ObservableList<Employee> allEmployees = EmployeeController.getInstance().getAllEmployees();

        allEmployees.forEach(employee -> {
            tbl01.add(
                    new EmployeeTbl1(
                            employee.getId(),
                            employee.getName(),
                            employee.getAddress(),
                            employee.getNic()
                    )
            );
        });
        tblEmployee1.setItems(tbl01);
    }

    private void loadTable2() {
        ObservableList<EmployeeTbl2> tbl02 = FXCollections.observableArrayList();
        ObservableList<Employee> allEmployees = EmployeeController.getInstance().getAllEmployees();

        allEmployees.forEach(employee -> {
            tbl02.add(
                    new EmployeeTbl2(
                            employee.getId(),
                            employee.getName(),
                            employee.getEmail(),
                            decryptPassword(employee.getPassword())
                    )
            );
        });
        tblEmployee2.setItems(tbl02);
    }

    public void btnAddOnAction() {

        String name = txtName.getText();
        String address = txtAddress.getText();
        String email = txtEmail.getText();
        String nic = txtNic.getText();
        String password = txtPassword.getText();

        // check all fields........
        if(name.isEmpty() || address.isEmpty() || email.isEmpty() || nic.isEmpty() || password.isEmpty()){
            new Alert(Alert.AlertType.WARNING,"Please enter all fields...!").show();
        }else{
            if(isValidated()) {
                Employee employee = new Employee(
                        generateEmployeeId(),
                        txtName.getText(),
                        txtAddress.getText(),
                        txtNic.getText(),
                        "staff",
                        txtEmail.getText(),
                        encryptPassword(txtPassword.getText())
                );

                boolean b = employeeBo.saveEmployee(employee);

                if(b){
                    new Alert(Alert.AlertType.CONFIRMATION,"Employee Added...!").show();
                    btnClearOnAction();
                }else{
                    new Alert(Alert.AlertType.CONFIRMATION,"Employee Not Added...!").show();
                }

                loadTable1();
                loadTable2();
            }else{
                new Alert(Alert.AlertType.WARNING,"Please enter details correctly...!").show();
            }
        }

    }

    public String generateEmployeeId() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM employee");
            Integer count = 0;
            while (resultSet.next()) {
                count = resultSet.getInt(1);

            }
            if (count == 0) {
                return "E001";
            }
            String lastEmployeeId;
            ResultSet resultSet1 = connection.createStatement().executeQuery("SELECT id\n" +
                    "FROM employee\n" +
                    "ORDER BY id DESC\n" +
                    "LIMIT 1;");
            if (resultSet1.next()) {
                lastEmployeeId = resultSet1.getString(1);
                Pattern pattern = Pattern.compile("[A-Za-z](\\d+)");
                Matcher matcher = pattern.matcher(lastEmployeeId);
                if (matcher.find()) {
                    int number = Integer.parseInt(matcher.group(1));
                    number++;
                    return String.format("E%03d", number);
                } else {
                    new Alert(Alert.AlertType.WARNING, "Can't find employee number...").show();
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void btnClearOnAction() {
        txtName.setText("");
        txtAddress.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        txtNic.setText("");
        txtSearch.setText("");
    }


    public void lblSearchOnAction() {

        String empId = txtSearch.getText().toUpperCase();

        try {
            int size = 0;
            String name = "";
            String address = "";
            String email = "";
            String password = "";
            String nic = "";

            ResultSet resultSet = employeeBo.searchById(empId);
            while (resultSet.next()){
                size++;
                name = resultSet.getString(1);
                address = resultSet.getString(2);
                email = resultSet.getString(3);
                password = resultSet.getString(4);
                nic = resultSet.getString(5);
            }

            if(size == 1){
                lblErrorMsg.setVisible(false);
                txtName.setText(name);
                txtAddress.setText(address);
                txtEmail.setText(email);
                txtPassword.setText(decryptPassword(password));
                txtNic.setText(nic);
            }else{
                lblErrorMsg.setVisible(true);
                txtName.setText("");
                txtAddress.setText("");
                txtEmail.setText("");
                txtPassword.setText("");
                txtNic.setText("");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if(txtSearch.getText().isEmpty()){
            lblErrorMsg.setVisible(false);
        }
    }

    public void btnUpdateOnAction() {
        
        try {
            updateEmployee();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void updateEmployee() throws SQLException {
        if(txtName.getText().isEmpty() || txtAddress.getText().isEmpty() || txtEmail.getText().isEmpty() || txtPassword.getText().isEmpty() || txtNic.getText().isEmpty() || txtSearch.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Please enter details...").show();
        }else {

            if(isValidated(txtSearch.getText().toUpperCase())){
                Connection connection = null;
                try {
                    connection = DBConnection.getInstance().getConnection();
                    connection.setAutoCommit(false);

                    String empId = txtSearch.getText().toUpperCase();
                    employeeBo.updateEmployee(txtName.getText(),txtAddress.getText(),txtNic.getText(),txtEmail.getText(),encryptPassword(txtPassword.getText()),empId);

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you confirm..?");
                    Optional<ButtonType> alertButton = alert.showAndWait();

                    if(alertButton.get() == ButtonType.OK){
                        connection.setAutoCommit(true);
                    }else if(alertButton.get() == ButtonType.CANCEL){
                        connection.rollback();
                    }
                    loadTable1();
                    loadTable2();

                } catch (ClassNotFoundException | SQLException e) {
                    throw new RuntimeException(e);
                }finally {
                    connection.setAutoCommit(true);
                }
            }

        }
    }

    public void btnDeleteOnAction() {
        if(!txtSearch.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you confirm..?");
            Optional<ButtonType> alertButton = alert.showAndWait();

            if(alertButton.get() == ButtonType.OK){
                String empId = txtSearch.getText().toUpperCase();
                try {
                    employeeBo.deleteById(empId);
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            loadTable1();
            loadTable2();
            btnClearOnAction();
        }
    }

    private boolean isValidated(){
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_.±]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$");
        Matcher emailMatcher = emailPattern.matcher(txtEmail.getText());

        Pattern namePattern = Pattern.compile("^[a-zA-Z ]*$");
        Matcher nameMatcher = namePattern.matcher(txtName.getText());

        Pattern nicPattern = Pattern.compile("^[0-9]");
        Matcher nicMatcher = nicPattern.matcher(txtNic.getText());

        boolean isNicValidate = false;
        if(txtNic.getText().length() == 12){
            isNicValidate = nicMatcher.find();
        }else if(txtNic.getText().length() == 11){
            String subNic = txtNic.getText().substring(0, 10);
            if(txtNic.getText().endsWith("v") && nicPattern.matcher(subNic).find()) {
                isNicValidate = true;
            }
        }

        int emailCount=0;
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT COUNT(email) FROM employee WHERE email='"+txtEmail.getText()+"'");
            while (resultSet.next()){
                emailCount = resultSet.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        int nicCount=0;
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT COUNT(nic) FROM employee WHERE email='"+txtNic.getText()+"'");
            while (resultSet.next()){
                nicCount = resultSet.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if(emailCount != 0 || nicCount != 0){
            new Alert(Alert.AlertType.ERROR,"Same email or nic...").show();
            return false;
        }else if(emailMatcher.find() && nameMatcher.find() && isNicValidate){
            return true;
        }

        return false;
    }

    private boolean isValidated(String id){
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_.±]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$");
        Matcher emailMatcher = emailPattern.matcher(txtEmail.getText());

        Pattern namePattern = Pattern.compile("^[a-zA-Z ]*$");
        Matcher nameMatcher = namePattern.matcher(txtName.getText());

        Pattern nicPattern = Pattern.compile("^[0-9]");
        Matcher nicMatcher = nicPattern.matcher(txtNic.getText());

        boolean isNicValidate = false;
        if(txtNic.getText().length() == 12){
            isNicValidate = nicMatcher.find();
        }else if(txtNic.getText().length() == 11){
            String subNic = txtNic.getText().substring(0, 10);
            if(txtNic.getText().endsWith("v") && nicPattern.matcher(subNic).find()) {
                isNicValidate = true;
            }
        }

        int emailCount=0;
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT COUNT(email) FROM employee WHERE email='"+txtEmail.getText()+"' AND id!='"+id+"'");
            while (resultSet.next()){
                emailCount = resultSet.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        int nicCount=0;
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT COUNT(nic) FROM employee WHERE email='"+txtNic.getText()+"' AND id!='"+id+"'");
            while (resultSet.next()){
                nicCount = resultSet.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if(emailCount != 0 || nicCount != 0){
            new Alert(Alert.AlertType.ERROR,"Same email or nic...").show();
            return false;
        }else if(emailMatcher.find() && nameMatcher.find() && isNicValidate){
            return true;
        }

        return false;
    }

    private String encryptPassword(String password){
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(password.getBytes());

    }

    private String decryptPassword(String encodedValue) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] bytes = decoder.decode(encodedValue);

        return new String(bytes);
    }
}
