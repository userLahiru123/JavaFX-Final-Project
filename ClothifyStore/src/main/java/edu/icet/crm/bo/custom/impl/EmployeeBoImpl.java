package edu.icet.crm.bo.custom.impl;

import edu.icet.crm.bo.custom.EmployeeBo;
import edu.icet.crm.dao.DaoFactory;
import edu.icet.crm.dao.custom.EmployeeDao;
import edu.icet.crm.dto.Employee;
import edu.icet.crm.entity.EmployeeEntity;
import edu.icet.crm.util.CrudUtil;
import edu.icet.crm.util.DaoType;
import org.modelmapper.ModelMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeBoImpl implements EmployeeBo {

    private EmployeeDao employeeDao = DaoFactory.getInstance().getDao(DaoType.EMPLOYEE);

    @Override
    public boolean saveEmployee(Employee dto) {
        return employeeDao.save(new ModelMapper().map(dto, EmployeeEntity.class));
    }

    @Override
    public boolean deleteById(String empId) throws SQLException, ClassNotFoundException {
        return employeeDao.deleteById(empId);
    }

    @Override
    public ResultSet searchById(String empId) throws ClassNotFoundException, SQLException {
        return employeeDao.searchById(empId);
    }

    public boolean updateEmployee(String name, String address, String nic, String email, String password, String empId) throws SQLException, ClassNotFoundException {
        return employeeDao.updateEmployee(name,address,nic,email,password,empId);
    }

}
