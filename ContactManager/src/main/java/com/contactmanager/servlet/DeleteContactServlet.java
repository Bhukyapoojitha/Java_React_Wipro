package com.contactmanager.servlet;

import java.io.IOException;

import com.contactmanager.model.Contact;
import com.contactmanager.storage.ContactStorage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/deleteContact")
public class DeleteContactServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        int id =
                Integer.parseInt(request.getParameter("id"));

        Contact contactToDelete = null;

        for(Contact c : ContactStorage.contacts){

            if(c.getId() == id){
                contactToDelete = c;
                break;
            }
        }

        ContactStorage.contacts.remove(contactToDelete);

        response.sendRedirect(
                "viewContacts?msg=Contact Deleted");
    }
}