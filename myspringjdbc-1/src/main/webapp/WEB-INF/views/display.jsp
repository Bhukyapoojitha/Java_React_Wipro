<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<table border="1">
<tr><th>Id</th><th>Name</th></tr>

<c:forEach var="e" items="${list}">
    <tr>
        <td>${e.eid}</td>
        <td>${e.ename}</td>
    </tr>
</c:forEach>

</table>
``