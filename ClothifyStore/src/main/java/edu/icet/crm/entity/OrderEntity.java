package edu.icet.crm.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "order")
public class OrderEntity {

    @Id
    private String orderId;
    private String employeeId;
    private String customerName;
    private String customerEmail;
    private String customerNic;
    private LocalDate date;
    private LocalDate time;
    private String discount;
    private String total;

}
