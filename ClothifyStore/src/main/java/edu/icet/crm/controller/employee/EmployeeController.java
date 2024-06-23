package edu.icet.crm.controller.employee;

import edu.icet.crm.dto.Employee;
import edu.icet.crm.util.CrudUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeController {

    private static EmployeeController instance;
    private EmployeeController (){}

    public static EmployeeController getInstance(){
        if (instance==null){
            return instance=new EmployeeController();
        }
        return instance;
    }

    public ObservableList<Employee> getAllEmployees() {
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT * FROM employee");
            ObservableList<Employee> listTable = FXCollections.observableArrayList();
            while (resultSet.next()) {
                listTable.add(
                        new Employee(
                                resultSet.getString("id"),
                                resultSet.getString("name"),
                                resultSet.getString("address"),
                                resultSet.getString("nic"),
                                resultSet.getString("role"),
                                resultSet.getString("email"),
                                resultSet.getString("password")
                        )
                );
            }
            return listTable;

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
