<%--
  Created by IntelliJ IDEA.
  User: johnmace
  Date: 21/10/2020
  Time: 15:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Home</title>
    <script> src="/InputValidation.js"</script>
  </head>
  <body>

  <h1>Home Page</h1>

  <form action="CreateAccount" method="post">
      <label for="firstname">First name:</label><br>
      <input type="text" id="firstname" name="firstname"><br>
      <label for="lastname">Last name:</label><br>
      <input type="text" id="lastname" name="lastname"><br>
      <label for="username">Username:</label><br>
      <input type="text" id="username" name="username"><br>
      <label for="phone">Phone Number:</label><br>
      <input type="number" id="phone" name="phone"  placeholder="44-0191-1234567"><br>
      <label for="email">Email:</label><br>
      <input type="email" id="email" name="email"  placeholder="joebloggs@email.com"><br>
      <label for="password">Password:</label><br>
      <input type="password" id="password" name="password"  placeholder="&#9679;&#9679;&#9679;&#9679;&#9679;"><br>
      <input type="submit" value="Submit">
  </form>
  </body>
</html>
