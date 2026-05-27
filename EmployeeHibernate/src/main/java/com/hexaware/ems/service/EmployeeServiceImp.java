package com.hexaware.ems.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hexaware.ems.dao.IEmployeeDao;
import com.hexaware.ems.entity.Employee;
import com.hexaware.ems.exceptions.EmployeeNotFoundException;

@Service
public class EmployeeServiceImp implements IEmployeeService {

    @Autowired   
    private IEmployeeDao dao;

    @Override
    public int addEmp(Employee emp) {
        return dao.addEmp(emp);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return dao.getAllEmployees();
    }

    @Override
    public int updateEmployee(Employee emp) {
        return dao.updateEmployee(emp);
    }

    @Override
    public int deleteByEid(int eid) {
        return dao.deleteByEid(eid);
    }

    @Override
    public Employee getByEid(int eid) {

        Employee emp = dao.getByEid(eid);

        if (emp == null) {
            throw new EmployeeNotFoundException("Employee Not Found Eid " + eid);
        }

        return emp;
    }

    
    public static boolean validateEmp(Employee emp) {

        return emp.getEid() > 99 &&
               emp.getEname().length() > 4 &&
               emp.getSalary() > 10000;
    }
}