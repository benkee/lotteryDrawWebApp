<%@ page import="java.util.Enumeration" %><%--
  Created by IntelliJ IDEA.
  User: Ben
  Date: 21/10/2020
  Time: 15:57
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home</title>
    <!--The session attributes are removes so every time user is on the index page they cannot get back into
     the accounts or admin page -->
    <%
        session.removeAttribute("firstname");
        session.removeAttribute("lastname");
        session.removeAttribute("username");
        session.removeAttribute("email");
        session.removeAttribute("hashedPassword");
        session.removeAttribute("role");
    %>
    <!--These function gets all the values from the form when the user presses submit then checks if they match
       the appropriate regular expression, if the inserted data is allowed, the form is submitted, and vice versa-->
    <script type="text/javascript">
        function validateAccountForm(){
            var fname = document.forms["createaccount"][0].value;
            var lname = document.forms["createaccount"][1].value;
            var username = document.forms["createaccount"][2].value;
            var password = document.forms["createaccount"][5].value;
            var phone = document.forms["createaccount"][3].value;
            var email = document.forms["createaccount"][4].value;
            var REGEX;
            var result;
            REGEX = /^[a-zA-Z]+$/;
            var regtest = new RegExp(REGEX);
            result = regtest.test(fname);
            if (!result){
                alert("Invalid first name, must be letters only");
                return false;
            }
            REGEX = /^[a-zA-Z]+$/;
            var regtest = new RegExp(REGEX);
            result = regtest.test(lname);
            if (!result){
                alert("Invalid last name, must be letters only");
                return false;
            }
            REGEX = /^[a-zA-Z0-9.-_]+$/;
            var regtest = new RegExp(REGEX);
            result = regtest.test(username);
            if (!result){
                alert("Invalid username, must be letters/numbers/.-_ only");
                return false;
            }
            REGEX = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d]{8,15}$/;
            var regtest = new RegExp(REGEX);
            result = regtest.test(password);
            if (!result){
                alert("Invalid password, must be between 8 and 15 characters with at least" +
                    " 1 uppercase and 1 lowercase character and 1 digit");
                return false;
            }
            REGEX = /^([\d]{2}(-)[\d]{4}(-)[\d]{7})$/;
            var regtest = new RegExp(REGEX);
            result = regtest.test(phone);
            if (!result){
                alert("Invalid phone number, must in format xx-xxxx-xxxxxxx");
                return false;
            }
            REGEX = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
            var regtest = new RegExp(REGEX);
            result = regtest.test(email);
            if (!result){
                alert("Invalid email, must be a valid email address");
                return false;
            }
            return true;
        }
        function validateLoginForm() {
            var REGEX;
            var result;
            var username = document.forms["login"][0].value;
            var password = document.forms["login"][1].value;
            REGEX = /^[a-zA-Z0-9.-_]+$/;
            var regtest = new RegExp(REGEX);
            result = regtest.test(username);
            if (!result){
                alert("Invalid username, must be letters/numbers/.-_ only");
                return false;
            }
            REGEX = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d]{8,15}$/;
            var regtest = new RegExp(REGEX);
            result = regtest.test(password);
            if (!result){
                alert("Invalid password, must be between 8 and 15 characters with at least" +
                    " 1 uppercase and 1 lowercase character and 1 digit");
                return false;
            }
            return true;
        }
    </script>
</head>
<body>
<h1>Home Page</h1>
<div>
        <%
        if(request.getAttribute("message")!=null){
        %>
            <%= request.getAttribute("message") %><br>
        <%}%>
<div/><!--These are forms where the user inputs information, on submit they do the doPost in the java servlet,
        named in action -->
<h2>Login:</h2>
<form name="login" onsubmit="return validateLoginForm()" method="post" action="UserLogin">
    <label for="usernameLog">Username:</label><br>
    <input type="text" id="usernameLog" name="usernameLog"><br>
    <label for="passwordLog">Password:</label><br>
    <input type="password" id="passwordLog" name="passwordLog"  placeholder="&#9679;&#9679;&#9679;&#9679;&#9679;"><br>
    <input type="submit" value="Submit">
</form>
<h2>Register:</h2>
<form name="createaccount" onsubmit="return validateAccountForm()" method="post" action="CreateAccount" >
    <label for="firstname">First name:</label><br>
    <input type="text" id="firstname" name="firstname"><br>
    <label for="lastname">Last name:</label><br>
    <input type="text" id="lastname" name="lastname"><br>
    <label for="username">Username:</label><br>
    <input type="text" id="username" name="username"><br>
    <label for="phone">Phone Number:</label><br>
    <input type="text" id="phone" name="phone"  placeholder="44-0191-1234567"><br>
    <label for="email">Email:</label><br>
    <input type="email" id="email" name="email"  placeholder="joebloggs@email.com"><br>
    <label for="password">Password:</label><br>
    <input type="password" id="password" name="password"  placeholder="&#9679;&#9679;&#9679;&#9679;&#9679;"><br>
    <select id="role" name="role">
        <option value="public">Public</option>
        <option value="admin">Admin</option>
    </select><br>
    <input type="submit" value="Submit">
</form>
</body>
</html>
