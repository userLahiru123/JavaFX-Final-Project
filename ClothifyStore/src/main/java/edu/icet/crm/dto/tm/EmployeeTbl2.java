package edu.icet.crm.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmployeeTbl2 {

    private String id;
    private String name;
    private String email;
    private String password;

}
