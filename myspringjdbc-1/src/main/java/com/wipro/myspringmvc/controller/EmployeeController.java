package com.wipro.myspringmvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.wipro.myspringjdbc.pojo.Employee;
import com.wipro.myspringjdbc.service.IEmployeeService;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private IEmployeeService service;

    
    @GetMapping("/form")
    public String showForm() {
        return "employeeForm";   
    }

    
    @PostMapping("/add")
    public String addEmployee(@ModelAttribute Employee emp, Model model) {

        service.addEmployee(emp);

        model.addAttribute("msg", "Employee Added Successfully");

        return "success";
    }

    
    @GetMapping("/all")
    public String getAllEmployees(Model model) {

        model.addAttribute("list", service.displayAllEmployees());

        return "display";
    }
    
    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "Working ";
    }
}
