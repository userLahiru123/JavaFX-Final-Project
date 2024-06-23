package edu.icet.crm.dto;

import lombok.*;

import java.io.File;
import java.sql.Blob;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Item {

    private String code;
    private String productName;
    private String category;
    private String size;
    private String qty;
    private String price;
    private String supplierId;
    private File image;

}
