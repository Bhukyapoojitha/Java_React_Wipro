package com.wipro.myspringjdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.wipro.myspringjdbc.pojo.Employee;

@Repository
public class EmployeeDaoImp implements IEmployeeDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    
    @Override
    public int addEmployee(Employee emp) {
        String sql = "INSERT INTO employee (eid, ename) VALUES (?, ?)";
        return jdbcTemplate.update(sql, emp.getEid(), emp.getEname());
    }

   
    @Override
    public List<Employee> displayAllEmployees() {
        String sql = "SELECT * FROM employee";

        return jdbcTemplate.query(sql, new RowMapper<Employee>() {
            @Override
            public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {

                Employee emp = new Employee();
                emp.setEid(rs.getInt("eid"));
                emp.setEname(rs.getString("ename"));

                return emp;
            }
        });
    }
    @Override
    public int updateEmployee(Employee emp) {
        String sql = "UPDATE employee SET ename=? WHERE eid=?";
        return jdbcTemplate.update(sql, emp.getEname(), emp.getEid());
    }

   
    @Override
    public Employee selectByEid(int eid) {
        String sql = "SELECT * FROM employee WHERE eid=?";

        return jdbcTemplate.queryForObject(sql, new RowMapper<Employee>() {
            @Override
            public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
                Employee emp = new Employee();
                emp.setEid(rs.getInt("eid"));
                emp.setEname(rs.getString("ename"));
                return emp;
            }
        }, eid);
    }

 
    @Override
    public int deleteByEid(int eid) {
        String sql = "DELETE FROM employee WHERE eid=?";
        return jdbcTemplate.update(sql, eid);
    }
}