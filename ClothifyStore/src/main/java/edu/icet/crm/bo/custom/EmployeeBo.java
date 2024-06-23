package edu.icet.crm.bo.custom;

import edu.icet.crm.bo.SuperBo;
import edu.icet.crm.dto.Employee;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface EmployeeBo extends SuperBo {
    boolean saveEmployee(Employee dto);
    ResultSet searchById(String empId) throws SQLException, ClassNotFoundException;
    boolean deleteById(String empId) throws SQLException, ClassNotFoundException;
    boolean updateEmployee(String name, String address, String nic, String email, String password, String empId) throws SQLException, ClassNotFoundException;
}
