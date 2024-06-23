package edu.icet.crm.bo.custom;

import edu.icet.crm.bo.SuperBo;
import edu.icet.crm.dto.Item;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface ItemBo extends SuperBo {
    ResultSet searchById(String itemCode) throws SQLException, ClassNotFoundException;
    boolean deleteById(String empId) throws SQLException, ClassNotFoundException;
    boolean updateItem(String name, String category, String size, String qty, String price, String supplierId, File image, String itemCode) throws SQLException, ClassNotFoundException, FileNotFoundException;
    boolean updateItem(String name, String category, String size, String qty, String price, String supplierId, String itemCode) throws SQLException, ClassNotFoundException, FileNotFoundException;
}
