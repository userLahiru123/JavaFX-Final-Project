package edu.icet.crm.dao.custom.impl;

import edu.icet.crm.dao.custom.EmployeeDao;
import edu.icet.crm.entity.EmployeeEntity;
import edu.icet.crm.util.CrudUtil;
import edu.icet.crm.util.HibernateUtil;
import org.hibernate.Session;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeDaoImpl implements EmployeeDao {
    @Override
    public boolean save(EmployeeEntity entity) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        session.persist(entity);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    @Override
    public boolean deleteById(String empId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM employee WHERE id='"+empId+"'");
    }

    public ResultSet searchById(String empId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("SELECT name,address,email,password,nic FROM employee WHERE id='"+empId+"'");
    }

    public boolean updateEmployee(String name, String address, String nic, String email, String password, String empId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE employee SET name='"+name+"', address='"+address+"', nic='"+nic+"', email='"+email+"', password='"+password+"' WHERE id='"+empId+"'");
    }

    @Override
    public boolean updateSupplier(String name, String company, String email, String supId) throws SQLException, ClassNotFoundException {
        return false;
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
