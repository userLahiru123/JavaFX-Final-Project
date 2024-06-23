package edu.icet.crm.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmployeeTbl1 {

    private String id;
    private String name;
    private String address;
    private String nic;

}
