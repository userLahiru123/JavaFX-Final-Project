package edu.icet.crm.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "supplier")
public class SupplierEntity {

    @Id
    private String id;
    private String name;
    private String company;
    private String email;

}
