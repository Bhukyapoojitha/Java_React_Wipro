<%@ page import="java.util.List" %>
<%@ page import="com.contactmanager.model.Contact" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>All Contacts</title>

<link rel="stylesheet"
href="css/style.css">

</head>
<body>

<div class="container">

<h2>Saved Contacts</h2>

<%
String msg = request.getParameter("msg");

if(msg != null){
%>

<h3 style="color:green;text-align:center;">
<%= msg %>
</h3>

<%
}
%>

<table>

<tr>
<th>ID</th>
<th>Name</th>
<th>Phone</th>
<th>Email</th>
<th>Actions</th>
</tr>

<%
List<Contact> contacts =
(List<Contact>) request.getAttribute("contacts");

if(contacts != null){

for(Contact c : contacts){
%>

<tr>

<td><%= c.getId() %></td>
<td><%= c.getName() %></td>
<td><%= c.getPhone() %></td>
<td><%= c.getEmail() %></td>

<td>

<a href="editContact?id=<%= c.getId() %>">
Edit
</a>

|

<a href="deleteContact?id=<%= c.getId() %>">
Delete
</a>

</td>

</tr>

<%
}
}
%>

</table>

</div>

</body>
</html>