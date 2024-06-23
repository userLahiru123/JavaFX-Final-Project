package edu.icet.crm.controller.item;

import edu.icet.crm.dto.OrderList;
import edu.icet.crm.dto.Supplier;
import edu.icet.crm.util.CrudUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemController {

    private static ItemController instance;
    private ItemController (){}

    public static ItemController getInstance(){
        if (instance==null){
            return instance=new ItemController();
        }
        return instance;
    }

    public ObservableList<String> getAllProductNames() {
        ObservableList<String> productNames = FXCollections.observableArrayList();
        productNames.add("t-shirts");
        productNames.add("jens");
        productNames.add("shorts");
        return productNames;
    }

    public ObservableList<String> getAllSizes() {
        ObservableList<String> sizes = FXCollections.observableArrayList();
        sizes.add("xs");
        sizes.add("s");
        sizes.add("m");
        sizes.add("l");
        sizes.add("xl");
        sizes.add("xxl");
        return sizes;
    }

    public ObservableList<String> getAllCategories() {
        ObservableList<String> categories = FXCollections.observableArrayList();
        categories.add("ladies");
        categories.add("gents");
        categories.add("kids");
        return categories;
    }

    public boolean updateStock(List<OrderList> orderDetailList) {
        boolean isUpdate = false;
        for (OrderList orderDetail : orderDetailList) {
            isUpdate = updateStock(orderDetail);
        }
        if (isUpdate){
            return true;
        }
        return false;
    }

    public boolean updateStock(OrderList orderDetail) {

        try {
            int oldQty = 0;
            ResultSet resultSet = CrudUtil.execute("SELECT qty FROM item WHERE code='"+orderDetail.getItemCode()+"'");
            while (resultSet.next()){
                oldQty = Integer.parseInt(resultSet.getString(1));
            }
            int newQty = oldQty - Integer.parseInt(orderDetail.getQuantity());
            Object isUpdate = CrudUtil.execute("UPDATE item SET qty='"+newQty+"' WHERE code='"+orderDetail.getItemCode()+"'");
            return (Boolean) isUpdate;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
