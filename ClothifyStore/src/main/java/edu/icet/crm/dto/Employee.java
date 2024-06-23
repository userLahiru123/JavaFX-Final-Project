package edu.icet.crm.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Employee {

    private String id;
    private String name;
    private String address;
    private String nic;
    private String role;
    private String email;
    private String password;

}
