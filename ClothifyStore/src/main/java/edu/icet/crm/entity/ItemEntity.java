package edu.icet.crm.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.*;

import java.sql.Blob;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "item")
public class ItemEntity {

    @Id
    private String code;
    private String productName;
    private String category;
    private String size;
    private String qty;
    private String price;
    private String supplierId;

    @Lob
    private Blob image;

}
