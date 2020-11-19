<%@ page import="java.util.Enumeration" %><%--
  Created by IntelliJ IDEA.
  User: johnmace
  Date: 21/10/2020
  Time: 15:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home</title>
    <style>
        form label {
            display: inline-block;
            width: 100px;
            color: green;
        }

        form div {
            margin-bottom: 10px;
        }
    </style>
    <%
        Enumeration<String> list;
        list = session.getAttributeNames();
        session.removeAttribute("firstname");
        session.removeAttribute("lastname");
        session.removeAttribute("username");
        session.removeAttribute("email");
        session.removeAttribute("hashedPassword");
        System.out.println(list);
    %>
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
            var regtest = new RegExp(REGEX)
            result = regtest.test(fname);
            if (!result){
                alert("Invalid first name, must be letters only");
                return false;
            }
            REGEX = /^[a-zA-Z]+$/;
            var regtest = new RegExp(REGEX)
            result = regtest.test(lname);
            if (!result){
                alert("Invalid last name, must be letters only");
                return false;
            }
            REGEX = /^[a-zA-Z0-9.-_]+$/;
            var regtest = new RegExp(REGEX)
            result = regtest.test(username);
            if (!result){
                alert("Invalid username, must be letters/numbers/.-_ only");
                return false;
            }
            REGEX = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d]{8,15}$/;
            var regtest = new RegExp(REGEX)
            result = regtest.test(password);
            if (!result){
                alert("Invalid password, must be between 8 and 15 characters with at least" +
                    " 1 uppercase and 1 lowercase character and 1 digit");
                return false;
            }
            REGEX = /^([\d]{2}(-)[\d]{4}(-)[\d]{7})$/;
            var regtest = new RegExp(REGEX)
            result = regtest.test(phone);
            if (!result){
                alert("Invalid phone number, must in format xx-xxxx-xxxxxxx");
                return false;
            }
            //REGEX = /\A[a-z0-9!#$%&'*+=?^_‘{|}~-]+(?:.[a-z0-9!#$%&'*+=?^_‘{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\z/;
            REGEX = /[\s\S]*/;
            var regtest = new RegExp(REGEX);
            result = regtest.test(email);
            if (!result){
                alert(email);
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
            var regtest = new RegExp(REGEX)
            result = regtest.test(username);
            if (!result){
                alert("Invalid username, must be letters/numbers/.-_ only");
                return false;
            }
            REGEX = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d]{8,15}$/;
            var regtest = new RegExp(REGEX)
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
<div/>
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
