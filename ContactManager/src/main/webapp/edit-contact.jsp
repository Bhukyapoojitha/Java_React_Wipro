<%@ page import="com.contactmanager.model.Contact" %>

<%
Contact c =
(Contact) request.getAttribute("contact");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Edit Contact</title>

<link rel="stylesheet"
href="css/style.css">

</head>
<body>

<div class="container">

<h2>Edit Contact</h2>

<form action="updateContact" method="post">

<input type="hidden"
name="id"
value="<%= c.getId() %>">

<input type="text"
name="name"
value="<%= c.getName() %>"
required>

<input type="text"
name="phone"
value="<%= c.getPhone() %>"
required>

<input type="email"
name="email"
value="<%= c.getEmail() %>"
required>

<button type="submit">
Update Contact
</button>

</form>

</div>

</body>
</html>