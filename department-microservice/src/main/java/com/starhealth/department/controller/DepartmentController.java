package com.starhealth.department.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.starhealth.department.entity.Department;
import com.starhealth.department.service.IDepartmentService;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    @Autowired
    IDepartmentService service;

    // ✅ CREATE
    @PostMapping("/add")
    public Department addDepartment(@RequestBody Department dept) {
        return service.addDepartment(dept);
    }

   
    @GetMapping("/get/{id}")
    public Department getDeptById(@PathVariable Long id) {
        return service.getDeptById(id);
    }

   
    @GetMapping("/all")
    public List<Department> getAllDepartments() {
        return service.getAllDepartments();
    }

  
    @PutMapping("/update/{id}")
    public Department updateDepartment(@PathVariable Long id,
                                       @RequestBody Department dept) {
        return service.updateDepartment(id, dept);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Long id) {
        service.deleteDepartment(id);
        return "Department deleted successfully";
    }
}
