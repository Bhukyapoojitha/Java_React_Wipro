package com.starhealth.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.starhealth.user.entity.User;
import com.starhealth.user.repository.UserRepository;
import com.starhealth.user.vo.Department;
import com.starhealth.user.vo.UserDepartmentVO;

@Service
public class UserServiceImp implements IUserServce {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UserRepository repo;

    @Override
    public User addUser(User user) {
        return repo.save(user);
    }

  
    @Override
    public User getUserById(Long userId) {
        return repo.findById(userId).orElse(new User());
    }

    
    @Override
    public List<User> getAllUsers() {
        return repo.findAll();
    }

    
    @Override
    public User updateUser(Long userId, User user) {

        User existing = repo.findById(userId).orElse(null);

        if (existing != null) {
            existing.setUserName(user.getUserName());
            existing.setEmail(user.getEmail());
            existing.setDepartmentId(user.getDepartmentId());
            return repo.save(existing);
        }

        return null;
    }

    
    @Override
    public void deleteUser(Long userId) {
        repo.deleteById(userId);
    }

  
    @Override
    public UserDepartmentVO getUserWithDept(Long userId) {

        User user = getUserById(userId);

        Long deptId = user.getDepartmentId();

        Department department = restTemplate.getForObject(
                "http://localhost:8181/api/department/get/" + deptId,
                Department.class);

        return new UserDepartmentVO(user, department);
    }
}



