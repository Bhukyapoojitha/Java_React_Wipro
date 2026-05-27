package com.starhealth.user.service;

import java.util.List;

import com.starhealth.user.entity.User;
import com.starhealth.user.vo.UserDepartmentVO;

public interface IUserServce {

    User addUser(User user);

    User getUserById(Long userId);

    UserDepartmentVO getUserWithDept(Long userId);

    List<User> getAllUsers();

    User updateUser(Long userId, User user);

    void deleteUser(Long userId);
}
