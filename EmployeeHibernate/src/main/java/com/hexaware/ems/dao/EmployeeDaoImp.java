package com.hexaware.ems.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.hexaware.ems.entity.Employee;
import com.hexaware.ems.util.HibernateUtil;


@Repository
public class EmployeeDaoImp implements IEmployeeDao {


	@Override
	public int addEmp(Employee emp) {

			
			int count=0;
		
			Session session = HibernateUtil.getSessionFactory().openSession();
			
			Transaction tx = session.beginTransaction();
			
						
			session.save(emp);
			
			tx.commit();
			
			session.close();
			
			count = 1;
	

		return count;

	}

	@Override
	public int updateEmployee(Employee emp) {
		// TODO Auto-generated method stub
		//int count = 0;
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		
		int count = session.createQuery(
				"update Employee e set e.ename = :name, e.salary = :sal where e.eid= :id")
                .setParameter("name", emp.getEname())
                .setParameter("sal", emp.getSalary())
                .setParameter("id", emp.getEid())
                .executeUpdate();
                	

				
				
		//session.update(emp);
		tx.commit();
		session.close();
		
		//count = 1; 
		
		return count;
	}

	@Override
	public int deleteByEid(int eid) {
		// TODO Auto-generated method stub
		//int count = 0;
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		
		int count = session.createQuery(
				"delete from Employee e where e.id = :id")
				.setParameter("id", eid)
				.executeUpdate();
				
		
		//Employee emp = session.get(Employee.class, eid);
		
		//session.delete(emp);
		
		tx.commit();
		session.close();
		
		return count;
	}

	@Override
	public Employee getByEid(int eid) {
		// TODO Auto-generated method stub
		
		
		//Employee emp = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

       Employee emp = session.createQuery(
    		   "from Employee e where e.eid = :id", Employee.class)
    		   .setParameter("id", eid)
    		   .uniqueResult();  

        session.close();

        return emp;
        
	}


	@Override
	public List<Employee> getAllEmployees() {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Employee> list = 
				session.createQuery("from Employee e", Employee.class)
				.getResultList();
		session.close();
        
        return list;
	}


}
