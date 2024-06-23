package edu.icet.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderList {

    private String orderId;
    private String itemCode;
    private String quantity;
    private String unitPrice;
    private String total;

}
