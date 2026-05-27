package com.starhealth.department.service;

import java.util.List;

import com.starhealth.department.entity.Department;

public interface IDepartmentService {
	
	public   Department       addDepartment(Department dept);
	
	public  Department  getDeptById(Long id);
	
	List<Department> getAllDepartments();
	

    Department updateDepartment(Long id, Department dept);

    void deleteDepartment(Long id);

	
	

}
