package com.contactmanager.servlet;

import java.io.IOException;

import com.contactmanager.storage.ContactStorage;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/viewContacts")
public class ViewContactsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute(
                "contacts",
                ContactStorage.contacts);

        RequestDispatcher rd =
                request.getRequestDispatcher(
                        "view-contacts.jsp");

        rd.forward(request, response);
    }
}
