package edu.icet.crm.controller.pages;

import edu.icet.crm.db.DBConnection;
import javafx.scene.control.Button;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.Connection;
import java.util.HashMap;

public class SalesPageFormController {
    public Button btnViewReports;

    public void btnViewReportsOnAction() {
        generateReport("sales_charts");
        generateReport("sales_details");
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
}
