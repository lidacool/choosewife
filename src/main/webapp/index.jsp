<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>login</title>
    <style type="text/css">
        * {
            margin: 0;
            padding: 0;
        }

        form {
            margin: 0 auto;
            padding: 15px;
            width: 300px;
            height: 300px;
            text-align: center;
        }

        #submit {
            padding: 10px
        }

        #submit input {
            width: 50px;
            height: 24px;
        }
    </style>
</head>
<body>
<div class="wrapper">
    <form action="MyServlet?method=login" method="post">
        <label>用户名:</label>
        <input type="text" name="username" value=""/><br><br>
        <label>密码：</label>
        <input type="password" name="password"/><br>

        <div id="submit">
            <input type="submit" value="登录"/>
        </div>
    </form>

</div>

<%
    if (request.getAttribute("login") != null && request.getAttribute("login") != "") {
        out.print("<script>alert('" + request.getAttribute("login") + "');</script>");
    }
%>
</body>
</html>


