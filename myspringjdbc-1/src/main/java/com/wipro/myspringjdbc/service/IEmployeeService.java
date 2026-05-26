package com.wipro.myspringjdbc.service;

import java.util.List;
import com.wipro.myspringjdbc.pojo.Employee;

public interface IEmployeeService {

    int addEmployee(Employee emp);

    List<Employee> displayAllEmployees();

    int updateEmployee(Employee emp);

    Employee selectByEid(int eid);

    int deleteByEid(int eid);  
}
