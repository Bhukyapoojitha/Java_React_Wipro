package com.contactmanager.servlet;

import java.io.IOException;

import com.contactmanager.model.Contact;
import com.contactmanager.storage.ContactStorage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/editContact")
public class EditContactServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        int id =
                Integer.parseInt(request.getParameter("id"));

        Contact selectedContact = null;

        for(Contact c : ContactStorage.contacts){

            if(c.getId() == id){
                selectedContact = c;
                break;
            }
        }

        request.setAttribute(
                "contact",
                selectedContact);

        request.getRequestDispatcher(
                "edit-contact.jsp")
                .forward(request, response);
    }
}