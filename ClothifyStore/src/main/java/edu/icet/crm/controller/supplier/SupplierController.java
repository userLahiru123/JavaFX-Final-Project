package edu.icet.crm.controller.supplier;

import edu.icet.crm.dto.Supplier;
import edu.icet.crm.util.CrudUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SupplierController {

    private static SupplierController instance;
    private SupplierController (){}

    public static SupplierController getInstance(){
        if (instance==null){
            return instance=new SupplierController();
        }
        return instance;
    }

    public ObservableList<Supplier> getAllSuppliers() {
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT * FROM supplier");
            ObservableList<Supplier> listTable = FXCollections.observableArrayList();
            while (resultSet.next()) {
                listTable.add(
                        new Supplier(
                                resultSet.getString("id"),
                                resultSet.getString("name"),
                                resultSet.getString("company"),
                                resultSet.getString("email")
                        )
                );
            }
            return listTable;

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
