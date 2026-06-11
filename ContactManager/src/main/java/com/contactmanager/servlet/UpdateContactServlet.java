package com.contactmanager.servlet;

import java.io.IOException;

import com.contactmanager.model.Contact;
import com.contactmanager.storage.ContactStorage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/updateContact")
public class UpdateContactServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        int id =
                Integer.parseInt(request.getParameter("id"));

        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");

        for(Contact c : ContactStorage.contacts){

            if(c.getId() == id){

                c.setName(name);
                c.setPhone(phone);
                c.setEmail(email);
            }
        }

        response.sendRedirect(
                "viewContacts?msg=Contact Updated");
    }
}
