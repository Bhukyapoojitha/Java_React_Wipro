package com.starhealth.department.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starhealth.department.entity.Department;
import com.starhealth.department.repository.DepartmentRepository;
@Service
public class DepartmentServiceImp implements IDepartmentService {

	@Autowired
	DepartmentRepository repo;
	
	@Override
	public Department addDepartment(Department dept) {
		// TODO Auto-generated method stub
		return repo.save(dept);
	}

	@Override
	public Department getDeptById(Long id) {
		// TODO Auto-generated method stub
		return repo.findById(id).orElse(new Department());
	}
	

   @Override
    public List<Department> getAllDepartments() {
        return repo.findAll();
    }
   

@Override
    public Department updateDepartment(Long id, Department dept) {
        Department existing = repo.findById(id).orElse(null);
        if (existing != null) {
        	existing.setDepartmentName(dept.getDepartmentName());
        	existing.setDepartmentCode(dept.getDepartmentCode());
        	existing.setDepartmentAddress(dept.getDepartmentAddress());
            return repo.save(existing);
        }
        return null;
    }

@Override
public void deleteDepartment(Long id) {

    if (!repo.existsById(id)) {
        throw new RuntimeException("Department not found with id: " + id);
    }

    repo.deleteById(id);
}





}
