<%--
  Created by IntelliJ IDEA.
  User: johnmace
  Date: 21/10/2020
  Time: 16:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Account</title>
    <script type="text/javascript">
        function generateNumbers(){
            for (var i=0; i<6; i++) {
                document.forms["addUN"][i].value = getSecureRandInt();
            }
        }
        function getSecureRandInt (){
            var numbers = new Uint8Array(6);
            window.crypto.getRandomValues(numbers);
            var range = 61;
            var max_range = 256;
            if (numbers[0] >= Math.floor(max_range/range)*range) {
                return getSecureRandInt();
            }
            return (numbers[0] % range);
        }
        function validateForm(){
            for (var i=0; i<6; i++) if (document.forms["addUN"][i].value === "") {
                alert("Number/s blank.");
                return false;
            }
            return true;
        }
    </script>
</head>
<body>
<h1>User Account</h1>

<p><%= request.getAttribute("message") %></p>
<h1>User Details</h1>
<div>
    <output>Firstname: </output><%= request.getAttribute("firstname") %><br>
    <output>Lastname: </output><%= request.getAttribute("lastname") %><br>
    <output>Username: </output><%= request.getAttribute("username") %><br>
    <output>Email: </output><%= request.getAttribute("email") %><br>
</div>
<h2>Select New Lottery Draw</h2>
<div>
    <form name="addUN" method="post" onsubmit="return validateForm()" action="AddUserNumbers">
        <label for="nu1">No.1:</label>
        <input type="number" id="nu1" name="nu1" min="0" max="60"><br>
        <label for="nu2">No.2:</label>
        <input type="number" id="nu2" name="nu2" min="0" max="60"><br>
        <label for="nu3">No.3:</label>
        <input type="number" id="nu3" name="nu3" min="0" max="60"><br>
        <label for="nu4">No.4:</label>
        <input type="number" id="nu4" name="nu4" min="0" max="60"><br>
        <label for="nu5">No.5:</label>
        <input type="number" id="nu5" name="nu5" min="0" max="60"><br>
        <label for="nu6">No.6:</label>
        <input type="number" id="nu6" name="nu6" min="0" max="60"><br>
        <button type="submit" value="Submit">Submit</button>
    </form>
    <button onclick="generateNumbers()">Lucky Dip</button>
    <form name="getDraws" method="post" action="GetUserNumbers"></form>
</div>
<a href="index.jsp">Home Page</a>

</body>
</html>
