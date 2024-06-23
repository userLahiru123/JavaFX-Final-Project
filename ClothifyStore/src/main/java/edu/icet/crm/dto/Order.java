package edu.icet.crm.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order {

    private String orderId;
    private String employeeId;
    private String customerName;
    private String customerEmail;
    private String customerNic;
    private String date;
    private String time;
    private double discount;
    private double total;
    private List<OrderList> orderList;
}
