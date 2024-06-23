package edu.icet.crm.controller.pages;

import com.jfoenix.controls.JFXRadioButton;
import edu.icet.crm.db.DBConnection;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.Connection;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ReportsPageFormController implements Initializable {
    public JFXRadioButton rbtnInventory;
    public JFXRadioButton rbtnEmployee;
    public JFXRadioButton rbtnSupplier;
    public Button btnGenerate;

    public void btnGenerateOnAction() {
        if (rbtnEmployee.isSelected()) {
            generateReport("employee");
        } else if (rbtnInventory.isSelected()) {
            generateReport("inventory");
        } else {
            generateReport("supplier");
        }
    }

    private void generateReport(String name) {
        try {
            // Establish database connection
            Connection conn = DBConnection.getInstance().getConnection();

            // Compile the JasperReport template
            JasperReport jasperReport = JasperCompileManager.compileReport("C:\\Users\\ASUS\\Desktop\\JAVA FX Final Project\\Project\\ClothifyStore\\src\\main\\resources\\reports\\" + name + ".jrxml");

            // Fill the report with data from the database
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), conn);

            JasperViewer.viewReport(jasperPrint, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        final ToggleGroup group = new ToggleGroup();

        rbtnEmployee.setToggleGroup(group);
        rbtnInventory.setSelected(true);
        rbtnInventory.setToggleGroup(group);
        rbtnSupplier.setToggleGroup(group);
    }

}
