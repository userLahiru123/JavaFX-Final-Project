package edu.icet.crm.dao.custom.impl;

import edu.icet.crm.dao.custom.ItemDao;
import edu.icet.crm.db.DBConnection;
import edu.icet.crm.entity.EmployeeEntity;
import edu.icet.crm.util.CrudUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemDaoImpl implements ItemDao {
    @Override
    public boolean save(EmployeeEntity dto) {
        return false;
    }

    @Override
    public boolean deleteById(String itemCode) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM item WHERE code='" + itemCode + "'");
    }

    @Override
    public ResultSet searchById(String itemCode) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("SELECT code,productName,category,size,qty,price,supplierId,image FROM item WHERE code='" + itemCode + "'");
    }

    @Override
    public boolean updateEmployee(String name, String address, String nic, String email, String password, String empId) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean updateItem(String name, String category, String size, String qty, String price, String supplierId, File image, String itemCode) throws SQLException, ClassNotFoundException, FileNotFoundException {

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement psTm = connection.prepareStatement("UPDATE item SET productName=?, category=?, size=?, qty=?, price=?, supplierId=?, image=? WHERE code=?");
        psTm.setString(1, name);
        psTm.setString(2, category);
        psTm.setString(3, size);
        psTm.setString(4, qty);
        psTm.setString(5, price);
        psTm.setString(6, supplierId);

        FileInputStream inputStream = new FileInputStream(image);
        psTm.setBlob(7, inputStream);

        psTm.setString(8, itemCode);
        int i = psTm.executeUpdate();

        return (i >= 1);

    }

    @Override
    public boolean updateItem(String name, String category, String size, String qty, String price, String supplierId, String itemCode) throws SQLException, ClassNotFoundException, FileNotFoundException {

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement psTm = connection.prepareStatement("UPDATE item SET productName=?, category=?, size=?, qty=?, price=?, supplierId=? WHERE code=?");
        psTm.setString(1, name);
        psTm.setString(2, category);
        psTm.setString(3, size);
        psTm.setString(4, qty);
        psTm.setString(5, price);
        psTm.setString(6, supplierId);
        psTm.setString(7, itemCode);
        int i = psTm.executeUpdate();

        return (i >= 1);

    }

    @Override
    public boolean updateSupplier(String name, String company, String email, String supId) throws SQLException, ClassNotFoundException {
        return false;
    }
}
