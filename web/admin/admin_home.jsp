<%--
  Created by IntelliJ IDEA.
  User: Ben
  Date: 15/11/2020
  Time: 23:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin Home</title>
</head>
<body>
<div>
    <form name="getDraws" method="post" action="GetUserNumbers">
    <button type="submit" value="Submit">Get Draws</button>
    </form>
</div>
    <%=request.getAttribute("accounts")%>
</body>
</html>
