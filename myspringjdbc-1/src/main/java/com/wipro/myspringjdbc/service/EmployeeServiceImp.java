package com.wipro.myspringjdbc.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import com.wipro.myspringjdbc.dao.IEmployeeDao;
import com.wipro.myspringjdbc.pojo.Employee;

@Service   // ✅ THIS WAS MISSING
public class EmployeeServiceImp implements IEmployeeService {

    @Autowired
    private IEmployeeDao dao;

    @Override
    public int addEmployee(Employee emp) {
        return dao.addEmployee(emp);
    }

    @Override
    public List<Employee> displayAllEmployees() {
        return dao.displayAllEmployees();
    }

    @Override
    public int deleteByEid(int eid) {
        return dao.deleteByEid(eid);
    }

    @Override
    public int updateEmployee(Employee emp) {
        return dao.updateEmployee(emp);
    }

    @Override
    public Employee selectByEid(int eid) {
        return dao.selectByEid(eid);
    }
}