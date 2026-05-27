package com.wipro.rest.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wipro.rest.jpa.entity.Employee;

import jakarta.transaction.Transactional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    Employee findByEname(String ename);

    List<Employee> findBySalaryGreaterThanOrderByEnameAsc(double sal);

    @Query("select e from Employee e order by e.salary")
    public List<Employee> findAllBySorted();

    @Query("select e from Employee e where e.salary between ?1 and ?2")
    public List<Employee> findBySalaryRange(double min, double max);

    @Modifying
    @Transactional
    @Query("update Employee e set e.salary = :sal where e.eid = :id")
    public int updateSalary(@Param("sal") double salary, @Param("id") int eid);

    @Query(value = "select * from employees", nativeQuery = true)
    public List<Employee> getAllBySQL();

    @Query("select sum(e.salary) from Employee e")
    public Double sumOfSalary();
}
