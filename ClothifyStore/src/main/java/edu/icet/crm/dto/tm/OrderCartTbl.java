package edu.icet.crm.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderCartTbl {

    private String itemCode;
    private String quantity;
    private String unitPrice;
    private String total;
}
