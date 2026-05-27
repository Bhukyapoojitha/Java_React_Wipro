package com.hexaware.ems.presentation;

import java.util.List;
import java.util.Scanner;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.hexaware.ems.config.AppConfig;
import com.hexaware.ems.entity.Employee;
import com.hexaware.ems.exceptions.EmployeeNotFoundException;
import com.hexaware.ems.service.IEmployeeService;

public class Client {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

       
        IEmployeeService service = context.getBean(IEmployeeService.class);

        boolean flag = true;

        while (flag) {

            System.out.println("*****Welcome to EMS*****");
            System.out.println("1.ADD EMPLOYEE");
            System.out.println("2.SHOW ALL EMPLOYEES");
            System.out.println("3.UPDATE EMPLOYEE");
            System.out.println("4. DELETE EMPLOYEE");
            System.out.println("5. SHOW EMPLOYEE BY EID");
            System.out.println("0. EXIT  / LOGOUT");

            int choice = scanner.nextInt();

            switch (choice) {

            case 1:

                Employee emp = readEmpData();

                int count = service.addEmp(emp);

                if (count > 0) {
                    System.out.println("Employee added successfully..");
                } else {
                    System.err.println("Employee Add Failed...");
                }

                break;

            case 2:

                List<Employee> list = service.getAllEmployees();

                for (Employee e : list) {
                    System.out.println(e);
                }

                break;

            case 3:

                System.out.println("Enter Employee Id to Update");
                int updateId = scanner.nextInt();

                try {
                    Employee existingEmp = service.getByEid(updateId);

                    System.out.println("Enter New Name");
                    existingEmp.setEname(scanner.next());

                    System.out.println("Enter New Salary");
                    existingEmp.setSalary(scanner.nextDouble());

                    int updateCount = service.updateEmployee(existingEmp);

                    if (updateCount > 0) {
                        System.out.println("Employee updated successfully..");
                    } else {
                        System.err.println("Employee update failed...");
                    }

                } catch (EmployeeNotFoundException e) {
                    System.err.println(e.getMessage());
                }

                break;

            case 4:

                System.out.println("Enter Employee Id to Delete");
                int deleteId = scanner.nextInt();

                int deleteCount = service.deleteByEid(deleteId);

                if (deleteCount > 0) {
                    System.out.println("Employee deleted successfully..");
                } else {
                    System.err.println("Employee not found...");
                }

                break;

            case 5:

                System.out.println("Enter Eid to Search Employee");

                int eid = scanner.nextInt();

                try {
                    Employee employee = service.getByEid(eid);
                    System.out.println(employee);
                } catch (EmployeeNotFoundException e) {
                    System.err.println(e.getMessage());
                }

                break;

            case 0:

                flag = false;
                System.out.println("Thank you , logout successfully..");
                break;

            default:
                System.err.println("Invalid option");
                break;
            }

        }
    }

    public static Employee readEmpData() {

        Employee emp = new Employee();

        System.out.println("Enter Eid");
        emp.setEid(scanner.nextInt());

        System.out.println("Enter Ename");
        emp.setEname(scanner.next());

        System.out.println("Enter Salary");
        emp.setSalary(scanner.nextDouble());

        return emp;
    }
}