package com.wipro.spring.annotations.beans;

import org.springframework.stereotype.Component;

@Component("a1")
public class Address {

    private String city = "Hyderabad";

    public Address() {
    }

    public Address(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Address [city=" + city + "]";
    }
}