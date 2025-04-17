<%--
  Created by IntelliJ IDEA.
  User: a-emad
  Date: 4/14/2025
  Time: 8:23 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="/Backend/api/user/register" method="post">
     <input type="text" name="username">
    <input type="password" name="password">
    <input type="text" name="isAdmin">

    <input type="submit">
</form>
</body>
</html>
