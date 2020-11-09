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
    <style>
        form label {
            display: inline-block;
            width: 100px;
            color: purple;
        }

        form div {
            margin-bottom: 10px;
        }
        .error {
            color: red;
            margin-left: 5px;
        }

        label.error {
            display: inline;
        }
        </style>
    <script type="text/javascript">
    function validateForm(){
        var fname = document.forms["createaccount"][0].value;
        var lname = document.forms["createaccount"][1].value;
        var username = document.forms["createaccount"][2].value;
        var password = document.forms["createaccount"][3].value;
        var phone = document.forms["createaccount"][4].value;
        var email = document.forms["createaccount"][5].value;
        var REGEX = /^[a-zA-Z]+$/;
        var regtest = new RegExp(REGEX)
        var result = regtest.test(fname);
        if (result == false){
            alert("Invalid first name, must be letters only");
            return false;
        }
        var REGEX = /^[a-zA-Z]+$/;
        var regtest = new RegExp(REGEX)
        var result = regtest.test(lname);
        if (result == false){
            alert("Invalid last name, must be letters only");
            return false;
        }
        var REGEX = /^[a-zA-Z0-9.-_]+$/;
        var regtest = new RegExp(REGEX)
        var result = regtest.test(username);
        if (result == false){
            alert("Invalid username, must be letters/numbers/.-_ only");
            return false;
        }
        var REGEX = /[(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])]{8,15}$/;
        var regtest = new RegExp(REGEX)
        var result = regtest.test(password);
        if (result == false){
            alert("Invalid password, must be letters/numbers only, between 8 and 15 characters with at least" +
                " 1 uppercase and 1 lowercase character and 1 digit");
            return false;
        }

    }
    </script>
</head>
<body>

<h1>Home Page</h1>

<form name="createaccount" onsubmit="return validateForm()" method="post" action="CreateAccount" >
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
