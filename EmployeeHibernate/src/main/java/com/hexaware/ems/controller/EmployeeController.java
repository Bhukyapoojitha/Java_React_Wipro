package com.hexaware.ems.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hexaware.ems.entity.Employee;
import com.hexaware.ems.service.IEmployeeService;

@Component
public class EmployeeController {

    @Autowired
    private IEmployeeService service;

    public int addEmployee(Employee emp) {
        return service.addEmp(emp);
    }

    public int updateEmployee(Employee emp) {
        return service.updateEmployee(emp);
    }

    public int deleteEmployee(int eid) {
        return service.deleteByEid(eid);
    }

    public Employee getEmployeeById(int eid) {
        return service.getByEid(eid);
    }

    public java.util.List<Employee> getAllEmployees() {
        return service.getAllEmployees();
    };
    
}