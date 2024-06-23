package edu.icet.crm.controller.item;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.icet.crm.bo.BoFactory;
import edu.icet.crm.bo.custom.EmployeeBo;
import edu.icet.crm.bo.custom.ItemBo;
import edu.icet.crm.db.DBConnection;
import edu.icet.crm.dto.Employee;
import edu.icet.crm.util.BoType;
import edu.icet.crm.util.CrudUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductsPageFormController implements Initializable {
    public JFXButton btnAdd;
    public JFXButton btnClear;
    public JFXButton btnUpdate;
    public JFXButton btnRemove;
    public TextField txtSearch;
    public Label lblErrorMsg;
    public JFXComboBox cmbProductName;
    public JFXComboBox cmbCategory;
    public JFXComboBox cmbSize;
    public JFXTextField txtQty;
    public JFXTextField txtPrice;
    public JFXComboBox cmbSupplierId;
    public Label lblSupplierName;
    public Label lblSupplierCompany;
    public Label lblSupplierEmail;
    public Label lblItemCode;
    public ImageView imgItem;

    private File imageFile;
    private final ItemBo itemBo = BoFactory.getInstance().getBo(BoType.ITEM);
    private Image defaultImage = new Image("/view/assets/addImageIcon3.jpg");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadProductNames();
        loadCategory();
        loadSize();
        loadSupplierIds();
        lblItemCode.setText(generateItemCode());
        lblErrorMsg.setVisible(false);

        imgItem.setImage(defaultImage);
    }

    private void loadSupplierIds() {
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT id FROM supplier");
            ObservableList<String> supplierIdList = FXCollections.observableArrayList();

            while (resultSet.next()) {
                supplierIdList.add(resultSet.getString(1));
            }

            cmbSupplierId.setItems(supplierIdList);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadSize() {
        ObservableList<String> itemCods;
        itemCods = ItemController.getInstance().getAllSizes();
        cmbSize.setItems(itemCods);
    }

    private void loadCategory() {
        ObservableList<String> itemCods;
        itemCods = ItemController.getInstance().getAllCategories();
        cmbCategory.setItems(itemCods);
    }

    private void loadProductNames() {

        ObservableList<String> itemCods;
        itemCods = ItemController.getInstance().getAllProductNames();
        cmbProductName.setItems(itemCods);
    }

    public void btnAddOnAction() {
        int i;

        // check all fields........
        if (cmbProductName.getValue() == null || cmbCategory.getValue() == null || cmbSize.getValue() == null || txtPrice.getText().isEmpty() || txtQty.getText().isEmpty() || cmbSupplierId.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Please enter all fields...!").show();
        } else {
            if (isValidated()) {
                String name = (String) cmbProductName.getValue();
                String category = (String) cmbCategory.getValue();
                String size = (String) cmbSize.getValue();
                String qty = txtQty.getText();
                String price = txtPrice.getText();
                String supplierId = (String) cmbSupplierId.getValue();
                try {
                    Connection connection = DBConnection.getInstance().getConnection();
                    PreparedStatement psTm = connection.prepareStatement("INSERT INTO item VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                    psTm.setString(1, lblItemCode.getText());
                    psTm.setString(2, name);
                    psTm.setString(3, category);
                    psTm.setString(4, size);
                    psTm.setString(5, qty);
                    psTm.setString(6, price);
                    psTm.setString(7, supplierId);

                    FileInputStream inputStream = new FileInputStream(imageFile);
                    psTm.setBlob(8, inputStream);
                    i = psTm.executeUpdate();
                } catch (SQLException | ClassNotFoundException | FileNotFoundException e) {
                    throw new RuntimeException(e);
                }

                if (i >= 1) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Item Added...!").show();
                    btnClearOnAction();
                } else {
                    new Alert(Alert.AlertType.CONFIRMATION, "Item Not Added...!").show();
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Please enter details correctly...!").show();
            }
        }
    }

    private boolean isValidated() {
        String qty = txtQty.getText();
        String price = txtPrice.getText();

        try {
            int qtyValue = Integer.parseInt(qty);
            double priceValue = Double.valueOf(price);

            if (qtyValue > 0 && priceValue > 0.0 && imgItem.getImage() != defaultImage) {
                return true;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return false;
    }

    public void btnClearOnAction() {
        txtSearch.setText("");
        cmbProductName.setValue(null);
        cmbSize.setValue(null);
        cmbCategory.setValue(null);
        cmbSupplierId.setValue(null);
        lblSupplierName.setText("");
        lblSupplierEmail.setText("");
        lblSupplierCompany.setText("");
        txtPrice.setText("");
        txtQty.setText("");
        lblItemCode.setText(generateItemCode());
        imgItem.setImage(defaultImage);
    }

    public void btnUpdateOnAction() {

        // check all fields........
        if (cmbProductName.getValue() == null || cmbCategory.getValue() == null || cmbSize.getValue() == null || txtPrice.getText().isEmpty() || txtQty.getText().isEmpty() || cmbSupplierId.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Please enter all fields...!").show();
        } else {
            if (isValidated()) {
                if (imageFile != null) {
                    boolean b;
                    String name = (String) cmbProductName.getValue();
                    String category = (String) cmbCategory.getValue();
                    String size = (String) cmbSize.getValue();
                    String qty = txtQty.getText();
                    String price = txtPrice.getText();
                    String supplierId = (String) cmbSupplierId.getValue();
                    try {
                        b = itemBo.updateItem(name, category, size, qty, price, supplierId, imageFile, lblItemCode.getText());

                    } catch (SQLException | ClassNotFoundException | FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                    if (b) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Item Updated...!").show();
                        btnClearOnAction();
                    } else {
                        new Alert(Alert.AlertType.CONFIRMATION, "Item Not Updated...!").show();
                    }
                } else {
                    boolean b;
                    String name = (String) cmbProductName.getValue();
                    String category = (String) cmbCategory.getValue();
                    String size = (String) cmbSize.getValue();
                    String qty = txtQty.getText();
                    String price = txtPrice.getText();
                    String supplierId = (String) cmbSupplierId.getValue();
                    try {

                        b = itemBo.updateItem(name, category, size, qty, price, supplierId, lblItemCode.getText());

                    } catch (SQLException | ClassNotFoundException | FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                    if (b) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Item Updated...!").show();
                        btnClearOnAction();
                    } else {
                        new Alert(Alert.AlertType.CONFIRMATION, "Item Not Updated...!").show();
                    }
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Please enter details correctly...!").show();
            }
        }
    }

    public void btnDeleteOnAction() {
        if (!txtSearch.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you confirm..?");
            Optional<ButtonType> alertButton = alert.showAndWait();

            if (alertButton.get() == ButtonType.OK) {
                String itemCode = txtSearch.getText().toUpperCase();
                try {
                    itemBo.deleteById(itemCode);
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            btnClearOnAction();
        }
    }

    public void lblSearchOnAction() {

        String itemCode = txtSearch.getText().toUpperCase();

        try {
            int size = 0;
            String code = "";
            String name = "";
            String category = "";
            String productSize = "";
            String qty = "";
            String price = "";
            String supplierId = "";

            ResultSet resultSet = itemBo.searchById(itemCode);
            while (resultSet.next()) {
                size++;
                code = resultSet.getString(1);
                name = resultSet.getString(2);
                category = resultSet.getString(3);
                productSize = resultSet.getString(4);
                qty = resultSet.getString(5);
                price = resultSet.getString(6);
                supplierId = resultSet.getString(7);

                Blob blob = resultSet.getBlob(8);
                InputStream inputStream = blob.getBinaryStream();
                Image image = new Image(inputStream);
                imgItem.setImage(image);
            }

            if (size == 1) {
                lblErrorMsg.setVisible(false);
                lblItemCode.setText(code);
                cmbProductName.setValue(name);
                cmbCategory.setValue(category);
                cmbSize.setValue(productSize);
                txtQty.setText(qty);
                txtPrice.setText(price);
                cmbSupplierId.setValue(supplierId);
            } else {
                lblErrorMsg.setVisible(true);
                lblItemCode.setText(generateItemCode());
                cmbProductName.setValue(null);
                cmbCategory.setValue(null);
                cmbSize.setValue(null);
                txtQty.setText("");
                txtPrice.setText("");
                cmbSupplierId.setValue(null);
                lblSupplierName.setText("");
                lblSupplierEmail.setText("");
                lblSupplierCompany.setText("");
                imgItem.setImage(defaultImage);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (txtSearch.getText().isEmpty()) {
            lblErrorMsg.setVisible(false);
        }

    }

    private void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPEG image", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG image", "*.png"),
                new FileChooser.ExtensionFilter("All images", "*.jpg", "*.png")
        );
        File file = fileChooser.showOpenDialog(imgItem.getScene().getWindow());
        imageFile = file;

        if (file != null) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                Image image = new Image(fileInputStream);
                imgItem.setImage(image);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            Image image = new Image("/view/assets/addImageIcon3.jpg");
            imgItem.setImage(image);
        }
    }


    public void imageItemOnAction() {
        chooseFile();
    }

    private void loadImage() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement psTm = connection.prepareStatement("SELECT image FROM item WHERE code='I002'");
            ResultSet resultSet = psTm.executeQuery();
            while (resultSet.next()) {
                Blob blob = resultSet.getBlob(1);
                InputStream inputStream = blob.getBinaryStream();
                Image image = new Image(inputStream);
                imgItem.setImage(image);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void cmbSupplierIdOnAction() {
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT name,company,email FROM supplier WHERE id='" + (String) cmbSupplierId.getValue() + "'");
            while (resultSet.next()) {
                lblSupplierName.setText(resultSet.getString(1));
                lblSupplierCompany.setText(resultSet.getString(2));
                lblSupplierEmail.setText(resultSet.getString(3));
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateItemCode() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM item");
            Integer count = 0;
            while (resultSet.next()) {
                count = resultSet.getInt(1);

            }
            if (count == 0) {
                return "I001";
            }
            String lastItemCode;
            ResultSet resultSet1 = connection.createStatement().executeQuery("SELECT code\n" +
                    "FROM item\n" +
                    "ORDER BY code DESC\n" +
                    "LIMIT 1;");
            if (resultSet1.next()) {
                lastItemCode = resultSet1.getString(1);
                Pattern pattern = Pattern.compile("[A-Za-z](\\d+)");
                Matcher matcher = pattern.matcher(lastItemCode);
                if (matcher.find()) {
                    int number = Integer.parseInt(matcher.group(1));
                    number++;
                    return String.format("I%03d", number);
                } else {
                    new Alert(Alert.AlertType.WARNING, "Can't find item number...").show();
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
