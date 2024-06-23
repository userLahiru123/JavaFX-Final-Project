package edu.icet.crm.bo.custom.impl;

import edu.icet.crm.bo.custom.ItemBo;
import edu.icet.crm.dao.DaoFactory;
import edu.icet.crm.dao.custom.ItemDao;
import edu.icet.crm.util.DaoType;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemBoImpl implements ItemBo {

    private ItemDao itemDao = DaoFactory.getInstance().getDao(DaoType.ITEM);

    @Override
    public ResultSet searchById(String itemCode) throws ClassNotFoundException, SQLException {
        return itemDao.searchById(itemCode);
    }

    @Override
    public boolean deleteById(String empId) throws SQLException, ClassNotFoundException {
        return itemDao.deleteById(empId);
    }

    @Override
    public boolean updateItem(String name, String category, String size, String qty, String price, String supplierId, File image, String itemCode) throws SQLException, ClassNotFoundException, FileNotFoundException {
        return itemDao.updateItem(name, category, size, qty, price, supplierId, image, itemCode);
    }

    @Override
    public boolean updateItem(String name, String category, String size, String qty, String price, String supplierId, String itemCode) throws SQLException, ClassNotFoundException, FileNotFoundException {
        return itemDao.updateItem(name, category, size, qty, price, supplierId, itemCode);
    }


}
