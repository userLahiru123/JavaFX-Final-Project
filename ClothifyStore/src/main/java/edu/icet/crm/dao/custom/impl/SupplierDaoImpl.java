package edu.icet.crm.dao.custom.impl;

import edu.icet.crm.dao.custom.SupplierDao;
import edu.icet.crm.entity.SupplierEntity;
import edu.icet.crm.util.CrudUtil;
import edu.icet.crm.util.HibernateUtil;
import org.hibernate.Session;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SupplierDaoImpl implements SupplierDao {
    @Override
    public boolean save(SupplierEntity entity) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        session.persist(entity);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    @Override
    public boolean deleteById(String supId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM supplier WHERE id='"+supId+"'");
    }

    public ResultSet searchById(String supId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("SELECT name,company,email FROM supplier WHERE id='"+supId+"'");
    }

    @Override
    public boolean updateEmployee(String name, String address, String nic, String email, String password, String empId) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean updateSupplier(String name, String company, String email, String supId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE supplier SET name='"+name+"', company='"+company+"', email='"+email+"' WHERE id='"+supId+"'");
    }

    @Override
    public boolean updateItem(String name, String category, String size, String qty, String price, String supplierId, File image, String itemCode) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean updateItem(String name, String category, String size, String qty, String price, String supplierId, String itemCode) throws SQLException, ClassNotFoundException, FileNotFoundException {
        return false;
    }
}
