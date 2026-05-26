package com.wipro.spring.annotations.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("e1")
@Scope("prototype")
public class Employee {

    private int eid;
    private String ename;

    @Autowired
    private Address address;

    @Autowired
    private Thread t1;   

    public Employee() {
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public Address getAddress() {
        return address;
    }

    public Thread getT1() {
        return t1;
    }

    @Override
    public String toString() {
        return "Employee [eid=" + eid + ", ename=" + ename + ", address=" + address + "]";
    }
}