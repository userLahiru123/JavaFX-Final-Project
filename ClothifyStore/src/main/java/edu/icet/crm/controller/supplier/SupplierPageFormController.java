package edu.icet.crm.controller.supplier;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.icet.crm.bo.BoFactory;
import edu.icet.crm.bo.custom.SupplierBo;
import edu.icet.crm.db.DBConnection;
import edu.icet.crm.dto.Supplier;
import edu.icet.crm.dto.tm.SupplierTbl;
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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SupplierPageFormController implements Initializable {
    public JFXButton btnAdd;
    public JFXButton btnClear;
    public JFXButton btnUpdate;
    public JFXButton btnRemove;
    public TextField txtSearch;
    public Label lblErrorMsg;
    public JFXTextField txtName;
    public JFXTextField txtCompany;
    public JFXTextField txtEmail;
    public TableView tblSupplier;
    public TableColumn colSupId;
    public TableColumn colName;
    public TableColumn colCompany;
    public TableColumn colEmail;

    private String supplierId;

    private final SupplierBo supplierBo = BoFactory.getInstance().getBo(BoType.SUPPLIER);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colSupId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCompany.setCellValueFactory(new PropertyValueFactory<>("company"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        loadTable();
        lblErrorMsg.setVisible(false);
        supplierId = generateSupplierId();
        System.out.println(supplierId);
    }

    private void loadTable() {
        ObservableList<SupplierTbl> tbl01 = FXCollections.observableArrayList();
        ObservableList<Supplier> allSuppliers = SupplierController.getInstance().getAllSuppliers();

        allSuppliers.forEach(supplier -> {
            tbl01.add(
                    new SupplierTbl(
                            supplier.getId(),
                            supplier.getName(),
                            supplier.getCompany(),
                            supplier.getEmail()
                    )
            );
        });
        tblSupplier.setItems(tbl01);
    }

    public String generateSupplierId() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM supplier");
            Integer count = 0;
            while (resultSet.next()) {
                count = resultSet.getInt(1);

            }
            if (count == 0) {
                return "S001";
            }
            String lastSupplierId;
            ResultSet resultSet1 = connection.createStatement().executeQuery("SELECT id\n" +
                    "FROM supplier\n" +
                    "ORDER BY id DESC\n" +
                    "LIMIT 1;");
            if (resultSet1.next()) {
                lastSupplierId = resultSet1.getString(1);
                Pattern pattern = Pattern.compile("[A-Za-z](\\d+)");
                Matcher matcher = pattern.matcher(lastSupplierId);
                if (matcher.find()) {
                    int number = Integer.parseInt(matcher.group(1));
                    number++;
                    return String.format("S%03d", number);
                } else {
                    new Alert(Alert.AlertType.WARNING, "Can't find supplier number...").show();
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void btnAddOnAction() {

        String name = txtName.getText();
        String email = txtEmail.getText();
        String company = txtCompany.getText();


        // check all fields........
        if(name.isEmpty() || email.isEmpty() || company.isEmpty()){
            new Alert(Alert.AlertType.WARNING,"Please enter all fields...!").show();
        }else{
            if(isValidated()) {
                Supplier supplier = new Supplier(
                        generateSupplierId(),
                        txtName.getText(),
                        txtCompany.getText(),
                        txtEmail.getText()
                );

                boolean b = supplierBo.saveSupplier(supplier);

                if(b){
                    new Alert(Alert.AlertType.CONFIRMATION,"Supplier Added...!").show();
                    btnClearOnAction();
                    supplierId = generateSupplierId();
                    System.out.println("After place :"+supplierId);
                }else{
                    new Alert(Alert.AlertType.CONFIRMATION,"Supplier Not Added...!").show();
                }

                loadTable();
            }else{
                new Alert(Alert.AlertType.WARNING,"Please enter details correctly...!").show();
            }
        }

    }

    public void btnClearOnAction() {
        txtName.setText("");
        txtEmail.setText("");
        txtSearch.setText("");
        txtCompany.setText("");
    }

    public void btnUpdateOnAction() {
        try {
            updateSupplier();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateSupplier() throws SQLException {
        if(txtName.getText().isEmpty() || txtEmail.getText().isEmpty() || txtCompany.getText().isEmpty() || txtSearch.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Please enter details...").show();
        }else {

            if(isValidated(txtSearch.getText().toUpperCase())){
                Connection connection = null;
                try {
                    connection = DBConnection.getInstance().getConnection();
                    connection.setAutoCommit(false);

                    String supId = txtSearch.getText().toUpperCase();
                    supplierBo.updateSupplier(txtName.getText(),txtCompany.getText(),txtEmail.getText(),supId);

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you confirm..?");
                    Optional<ButtonType> alertButton = alert.showAndWait();

                    if(alertButton.get() == ButtonType.OK){
                        connection.setAutoCommit(true);
                    }else if(alertButton.get() == ButtonType.CANCEL){
                        connection.rollback();
                    }
                    loadTable();

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
                String supId = txtSearch.getText().toUpperCase();
                try {
                    supplierBo.deleteById(supId);
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            loadTable();
            btnClearOnAction();
        }
    }

    public void lblSearchOnAction() {
        String supId = txtSearch.getText().toUpperCase();

        try {
            int size = 0;
            String name = "";
            String company = "";
            String email = "";

            ResultSet resultSet = supplierBo.searchById(supId);
            while (resultSet.next()){
                size++;
                name = resultSet.getString(1);
                company = resultSet.getString(2);
                email = resultSet.getString(3);
            }

            if(size == 1){
                lblErrorMsg.setVisible(false);
                txtName.setText(name);
                txtCompany.setText(company);
                txtEmail.setText(email);
            }else{
                lblErrorMsg.setVisible(true);
                txtName.setText("");
                txtCompany.setText("");
                txtEmail.setText("");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if(txtSearch.getText().isEmpty()){
            lblErrorMsg.setVisible(false);
        }
    }

    private boolean isValidated(){
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_.±]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$");
        Matcher emailMatcher = emailPattern.matcher(txtEmail.getText());

        Pattern namePattern = Pattern.compile("^[a-zA-Z ]*$");
        Matcher nameMatcher = namePattern.matcher(txtName.getText());

        int emailCount=0;
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT COUNT(email) FROM supplier WHERE email='"+txtEmail.getText()+"'");
            while (resultSet.next()){
                emailCount = resultSet.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        if(emailCount != 0){
            new Alert(Alert.AlertType.ERROR,"Same email...").show();
            return false;
        }else if(emailMatcher.find() && nameMatcher.find()){
            return true;
        }

        return false;
    }

    private boolean isValidated(String id){
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_.±]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$");
        Matcher emailMatcher = emailPattern.matcher(txtEmail.getText());

        Pattern namePattern = Pattern.compile("^[a-zA-Z ]*$");
        Matcher nameMatcher = namePattern.matcher(txtName.getText());

        int emailCount=0;
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT COUNT(email) FROM supplier WHERE email='"+txtEmail.getText()+"' AND id!='"+id+"'");
            while (resultSet.next()){
                emailCount = resultSet.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        if(emailCount != 0){
            new Alert(Alert.AlertType.ERROR,"Same email...").show();
            return false;
        }else if(emailMatcher.find() && nameMatcher.find()){
            return true;
        }

        return false;
    }

}
