package com.contactmanager.servlet;

import java.io.IOException;

import com.contactmanager.model.Contact;
import com.contactmanager.storage.ContactStorage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/addContact")
public class AddContactServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            String name = request.getParameter("name");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");

            int id = ContactStorage.contacts.size() + 1;

            Contact contact =
                    new Contact(id, name, phone, email);

            ContactStorage.contacts.add(contact);

            response.sendRedirect(
                    "viewContacts?msg=Contact Added Successfully");

        } catch (Exception e) {

            response.sendRedirect(
                    "add-contact.jsp?error=Contact Not Added");
        }
    }
}