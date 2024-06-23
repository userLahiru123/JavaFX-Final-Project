package edu.icet.crm.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "employee")
public class EmployeeEntity {

    @Id
    private String id;
    private String name;
    private String address;
    private String nic;
    private String role;
    private String email;
    private String password;

}
