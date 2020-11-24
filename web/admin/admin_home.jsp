<%--suppress HtmlUnknownTarget --%>
<%--
  Created by IntelliJ IDEA.
  User: Ben
  Date: 15/11/2020
  Time: 23:23
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <!--Checks if the session attribute role is valid, if not the user is returned to the home page-->
    <%if(session.getAttribute("role").equals("public") || session.getAttribute("role") == null){
        response.sendRedirect("/web/index.jsp");
    }%>
    <title></title>
</head>
<body>
<div>
    <h1>Admin Home</h1>
    <br>
    <form name="getAccounts" method="post" action="DisplayData">
    <button type="submit" value="Submit">Get Accounts</button>
    </form>
</div>
    <%if (request.getAttribute("accounts")!= null){%>
<%=request.getAttribute("accounts")%><%}%>
<br>
<a href="index.jsp">Home Page</a>
</body>
</html>
