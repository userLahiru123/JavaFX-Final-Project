package edu.icet.crm.bo.custom;

import edu.icet.crm.bo.SuperBo;
import edu.icet.crm.dto.Employee;
import edu.icet.crm.dto.Supplier;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SupplierBo extends SuperBo {
    boolean saveSupplier(Supplier dto);
    ResultSet searchById(String supId) throws SQLException, ClassNotFoundException;
    boolean deleteById(String supId) throws SQLException, ClassNotFoundException;
    boolean updateSupplier(String name, String company, String email, String supId) throws SQLException, ClassNotFoundException;
}
