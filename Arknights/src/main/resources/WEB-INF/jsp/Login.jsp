<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <script type="text/javascript">
        function submitForm() {
            var form = document.getElementById('login-form');
            form.submit();
        }
    </script>
</head>
<body onload="document.getElementById('username').focus()"> <!-- 页面加载时自动选中用户名输入框 -->
<h1>Login Page</h1>
<form id="login-form" action="/login" method="post">
    <label for="username">Username:</label><br/>
    <input type="text" id="username" name="username" class="username"><br>
    <label for="password">Password:</label><br/>
    <input type="password" id="password" name="password" class="password"><br>
    <button type="submit" id="button">登陆</button>
</form>
</body>
</html>
