package com.gym.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    private Integer employeeAccount;
    private String employeeName;
    private String employeeGender;
    private Integer employeeAge;
    private String entryTime;
    private String staff;
    private String employeeMessage;
}
