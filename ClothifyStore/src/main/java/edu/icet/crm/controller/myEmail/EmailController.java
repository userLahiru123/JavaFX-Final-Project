package edu.icet.crm.controller.myEmail;

import edu.icet.crm.util.CrudUtil;
import javafx.scene.control.Alert;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class EmailController {

    private static EmailController instance;
    private EmailController(){}

    public static EmailController getInstance(){
        if(instance == null){
            return instance = new EmailController();
        }
        return instance;
    }

    public void sendMail(String orderId) {

        try {
            String dis = "";
            String totalBillValue = "";
            String receptionMail = "";
            String bill = "Bill Details\n\n"
                    +"Order ID:"+orderId;

            ResultSet resultSet = CrudUtil.execute("SELECT customerEmail,date,time,discount,total FROM orders WHERE orderId='"+orderId+"'");
            while (resultSet.next()){
                receptionMail=resultSet.getString(1);
                bill += "\nDate :"+resultSet.getString(2)
                        +"\nTime :"+resultSet.getString(3);
                dis = resultSet.getString(4);
                totalBillValue = resultSet.getString(5);
            }

            bill += "\nDiscount: Rs."+dis
                    +"\n---------------------------------"
                    +"\nTotal  : Rs."+totalBillValue
                    +"\n----------------------------------";

            Properties properties = new Properties();

            properties.put("mail.smtp.auth","true");
            properties.put("mail.smtp.starttls.enable","true");
            properties.put("mail.smtp.host","smtp.gmail.com");
            properties.put("mail.smtp.port","587");

            String myEmail = "thiwanka631@gmail.com";
            String password = "qdyr lqyb wohz hghf";

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(myEmail,password);
                }
            });

            Message message = prepareMessage(session,myEmail,receptionMail,bill);
            if(message != null){
                new Alert(Alert.AlertType.INFORMATION,"Send email....");
            }
            Transport.send(message);
        } catch (SQLException | ClassNotFoundException | MessagingException e) {
            System.out.println("email send error");
        }

    }

    private Message prepareMessage(Session session, String myMail, String receptionMail, String msg){
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myMail));
            message.setRecipients(Message.RecipientType.TO, new Address[]{
                    new InternetAddress(receptionMail)
            });

            message.setSubject("Clothify Store PVT(LTD)");
            message.setText(msg);
            return message;
        }catch (MessagingException e){
            new Alert(Alert.AlertType.ERROR,"Email sending error...").show();
//            throw new RuntimeException(e);
        }
        return null;
    }
}
