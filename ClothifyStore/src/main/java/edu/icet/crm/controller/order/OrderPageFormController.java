package edu.icet.crm.controller.order;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.icet.crm.controller.item.ItemController;
import edu.icet.crm.controller.myEmail.EmailController;
import edu.icet.crm.controller.pages.LoginPageFormController;
import edu.icet.crm.db.DBConnection;
import edu.icet.crm.dto.Order;
import edu.icet.crm.dto.OrderList;
import edu.icet.crm.util.CrudUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import org.hibernate.metamodel.mapping.ordering.AliasResolver;

import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderPageFormController implements Initializable {
    public JFXButton btnAddToCart;
    public JFXButton btnClear;
    public JFXButton btnRemove;
    public JFXTextField txtCustomerName;
    public JFXTextField txtCustomerEmail;
    public JFXTextField txtCustomerNic;
    public JFXComboBox cmbItemCode;
    public Label lblSize;
    public Label lblQtyOnHand;
    public JFXTextField txtQty;
    public JFXTextField txtDiscount;
    public Label lblNetTotal;
    public TableView tblCart;
    public TableColumn colItemCode;
    public TableColumn colQty;
    public TableColumn colUnitPrice;
    public TableColumn colTotal;
    public JFXButton btnPlaceOrder;
    public Label lblErrorMsg;
    public TextField lblSearch;
    public Label lblOrderId;
    private ObservableList<OrderList> listForTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        loadItemCodes();
        lblErrorMsg.setVisible(false);
        generateOrderId();
    }

    private void loadItemCodes() {
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT code FROM item");
            ObservableList<String> itemCodeList = FXCollections.observableArrayList();
            while (resultSet.next()) {
                itemCodeList.add(resultSet.getString(1));
            }
            cmbItemCode.setItems(itemCodeList);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    List<OrderList> orderList = new ArrayList<>();

    public void btnAddOnAction() {
        if (isValidate() && cmbItemCode.getValue() != null) {

            double unitPrice = 0.0;
            int qtyOnHand = 0;
            try {
                ResultSet resultSet = CrudUtil.execute("SELECT price,qty FROM item WHERE code='" + cmbItemCode.getValue() + "'");
                while (resultSet.next()) {
                    unitPrice = Double.valueOf(resultSet.getString(1));
                    qtyOnHand = Integer.parseInt(resultSet.getString(2));
                }
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            if (qtyOnHand >= Integer.parseInt(txtQty.getText()) && Integer.parseInt(txtQty.getText()) > 0) {
                double total = Integer.parseInt(txtQty.getText()) * unitPrice;
                lblNetTotal.setText(String.valueOf((Double.valueOf(lblNetTotal.getText()) + total)));
                orderList.add(new OrderList(
                        lblOrderId.getText(),
                        cmbItemCode.getValue().toString(),
                        txtQty.getText(),
                        String.valueOf(unitPrice),
                        String.valueOf(total)
                ));

                loadCartTable();
                cmbItemCode.setValue(null);
                lblSize.setText("");
                lblQtyOnHand.setText("");
                lblSearch.setText(null);
                txtQty.setText(null);
            } else {
                new Alert(Alert.AlertType.WARNING, "Out of stock").show();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please enter values").show();
        }
    }

    private void loadCartTable() {
        listForTable = FXCollections.observableArrayList();

        orderList.forEach(list -> {
            listForTable.add(list);
        });

        tblCart.setItems(listForTable);
    }

    public void btnClearOnAction() {
        cmbItemCode.setValue(null);
        lblSize.setText(null);
        lblQtyOnHand.setText(null);
        lblSearch.setText(null);
        txtCustomerName.setText(null);
        txtCustomerEmail.setText(null);
        txtCustomerNic.setText(null);
        lblNetTotal.setText("0");
        txtDiscount.setText(null);
    }

    public void btnDeleteOnAction() {
        if (!tblCart.getSelectionModel().getSelectedItems().isEmpty()) {
            OrderList item = (OrderList) tblCart.getSelectionModel().getSelectedItem();

            for (int i = 0; i < orderList.size(); i++) {
                if (orderList.get(i).equals(item)) {
                    lblNetTotal.setText(String.valueOf(Double.valueOf(lblNetTotal.getText()) - Double.valueOf(orderList.get(i).getTotal())));

                    orderList.remove(i);
                    break;
                }
            }
            loadCartTable();
        }
    }

    public void lblSearchOnAction() {
        String itemCode = lblSearch.getText().toUpperCase();

        try {
            int size = 0;
            String searchedItem = "";

            ResultSet resultSet = CrudUtil.execute("SELECT code FROM item WHERE code='" + itemCode + "'");
            while (resultSet.next()) {
                size++;
                searchedItem = resultSet.getString(1);
            }

            if (size == 1) {
                lblErrorMsg.setVisible(false);
                cmbItemCode.setValue(searchedItem);
            } else {
                lblErrorMsg.setVisible(true);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (lblSearch.getText().isEmpty()) {
            lblErrorMsg.setVisible(false);
            cmbItemCode.setValue(null);
            lblQtyOnHand.setText(null);
            lblSize.setText(null);
        }
    }

    public void cmbItemCodeOnAction() {

        try {
            ResultSet resultSet = CrudUtil.execute("SELECT size,qty FROM item WHERE code='" + cmbItemCode.getValue() + "'");
            while (resultSet.next()) {
                lblSize.setText(resultSet.getString(1).toUpperCase());
                lblQtyOnHand.setText(resultSet.getString(2));
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (cmbItemCode.getValue() != null) {
            lblSearch.setText(cmbItemCode.getValue().toString());
        }
    }

    private boolean discountValidate(){
        double discount;
        try {
            discount = Double.valueOf(txtDiscount.getText());

            if(discount > 0){
                return true;
            }
            new Alert(Alert.AlertType.ERROR,"Please enter valid discount").show();
            return false;
        }catch (NumberFormatException e){
            new Alert(Alert.AlertType.ERROR,"Please enter valid discount").show();
            return false;
        }
    }

    public void btnPlaceOrderOnAction() {
        if (customerDetailsValidate()) {
            if (!orderList.isEmpty() && discountValidate()) {

                LocalDateTime myDateObj = LocalDateTime.now();
                DateTimeFormatter myFormatObjForTime = DateTimeFormatter.ofPattern("HH:mm:ss");
                DateTimeFormatter myFormatObjForDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String formattedDate = myDateObj.format(myFormatObjForDate);
                String formattedTime = myDateObj.format(myFormatObjForTime);

                double discount = Double.valueOf(txtDiscount.getText());
                try {
                    placeOrder(
                            new Order(
                                    lblOrderId.getText(),
                                    LoginPageFormController.employeeId,
                                    txtCustomerName.getText(),
                                    txtCustomerEmail.getText(),
                                    txtCustomerNic.getText(),
                                    formattedDate,
                                    formattedTime,
                                    (Double.valueOf(lblNetTotal.getText())-discount),
                                    discount,
                                    orderList
                            )
                    );
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

            } else {
                new Alert(Alert.AlertType.ERROR, "Please add items").show();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Please enter valid customer details....").show();
        }
    }

    private boolean isValidate() {
        try {
            Integer.parseInt(txtQty.getText());

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean customerDetailsValidate() {
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_.±]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$");
        Matcher emailMatcher = emailPattern.matcher(txtCustomerEmail.getText());

        Pattern namePattern = Pattern.compile("^[a-zA-Z ]*$");
        Matcher nameMatcher = namePattern.matcher(txtCustomerName.getText());

        Pattern nicPattern = Pattern.compile("^[0-9]");
        Matcher nicMatcher = nicPattern.matcher(txtCustomerNic.getText());

        boolean isNicValidate = false;
        if (txtCustomerNic.getText().length() == 12) {
            isNicValidate = nicMatcher.find();
        } else if (txtCustomerNic.getText().length() == 11) {
            String subNic = txtCustomerNic.getText().substring(0, 10);
            if (txtCustomerNic.getText().endsWith("v") && nicPattern.matcher(subNic).find()) {
                isNicValidate = true;
            }
        }

        if (emailMatcher.find() && nameMatcher.find() && isNicValidate) {
            return true;
        }
        return false;
    }

    private boolean placeOrder(Order order) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement psTm = connection.prepareStatement("INSERT INTO orders VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
            psTm.setString(1, order.getOrderId());
            psTm.setString(2, order.getEmployeeId());
            psTm.setObject(3, order.getCustomerName());
            psTm.setString(4, order.getCustomerEmail());
            psTm.setString(5, order.getCustomerNic());
            psTm.setString(6, order.getDate());
            psTm.setString(7, order.getTime());
            psTm.setDouble(8, order.getDiscount());
            psTm.setDouble(9, order.getTotal());

            boolean isOrderAdd = psTm.execute();

            if (!isOrderAdd) {
                boolean isOrderDetailAdd = OrderDetailsController.getInstance().addOrderDetail(order.getOrderList());
                if (isOrderDetailAdd) {
                    boolean isUpdateStock = ItemController.getInstance().updateStock(order.getOrderList());
                    if (isUpdateStock) {
                        System.out.println("Commit True--------------");
                        connection.setAutoCommit(true);

                        EmailController.getInstance().sendMail(order.getOrderId());
                        new Alert(Alert.AlertType.INFORMATION,"Payment : Rs."+order.getTotal()).show();
                        generateOrderId();
                        orderList.clear();
                        loadCartTable();
                        btnClearOnAction();
                        return true;
                    }
                }
            }
            connection.rollback();
            return false;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("finally");
            connection.setAutoCommit(true);
        }
    }

    public void generateOrderId() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM orders");
            Integer count = 0;
            while (resultSet.next()) {
                count = resultSet.getInt(1);

            }
            if (count == 0) {
                lblOrderId.setText("OR001");
            }
            String lastOrderId = "";
            ResultSet resultSet1 = connection.createStatement().executeQuery("SELECT orderId\n" +
                    "FROM orders\n" +
                    "ORDER BY orderId DESC\n" +
                    "LIMIT 1;");
            if (resultSet1.next()) {
                lastOrderId = resultSet1.getString(1);
                Pattern pattern = Pattern.compile("[A-Za-z](\\d+)");
                Matcher matcher = pattern.matcher(lastOrderId);
                if (matcher.find()) {
                    int number = Integer.parseInt(matcher.group(1));
                    number++;
                    lblOrderId.setText(String.format("OR%03d", number));
                } else {
                    new Alert(Alert.AlertType.WARNING, "hello").show();
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
