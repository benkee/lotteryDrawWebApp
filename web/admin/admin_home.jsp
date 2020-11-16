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
    <br>
    <form name="getAccounts" method="post" action="DisplayData">
    <button type="submit" value="Submit">Get Accounts</button>
    </form>
</div>
    <%=request.getAttribute("accounts")%>
</body>
</html>
