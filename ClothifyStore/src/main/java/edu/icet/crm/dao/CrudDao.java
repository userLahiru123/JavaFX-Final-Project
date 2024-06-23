package edu.icet.crm.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface CrudDao<T> extends SuperDao{
    boolean save(T dto);
    boolean deleteById(String id) throws SQLException, ClassNotFoundException;
    ResultSet searchById(String id) throws SQLException, ClassNotFoundException;
    boolean updateEmployee(String name, String address, String nic, String email, String password, String empId) throws SQLException, ClassNotFoundException;
    boolean updateSupplier(String name, String company, String email, String supId) throws SQLException, ClassNotFoundException;
    boolean updateItem(String name, String category, String size, String qty, String price, String supplierId, File image, String itemCode) throws SQLException, ClassNotFoundException, FileNotFoundException;
    boolean updateItem(String name, String category, String size, String qty, String price, String supplierId, String itemCode) throws SQLException, ClassNotFoundException, FileNotFoundException;
}
