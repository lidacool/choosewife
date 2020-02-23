<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--JSP和Servlet版本导致el功能默认关闭，加入<%@page isELIgnored="false"%>标签手动开启el功能。--%>
<%@page isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>图片上传</title>
</head>
<body>
<center>
<form action="MyServlet?method=upload" method="post" enctype="multipart/form-data">
    <%--<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>--%>
    <input type="file" name="file" multiple="multiple" accept="image/*">
    <br>
    <input type="submit" value="上传" class="btn btn-success">
</form>
</center>
<hr>
<b>已上传的图片：</b>共 ${fn:length(fileNames)} 张
<br>
<table>
    <c:forEach var="l" items="${fileNames}">
        <tr>
            <td>${l}</td>
            <td>
                <form action="MyServlet?method=deleteImg" method="post">
                    <input type="hidden" name="fileName" value="${l}"/>
                    <input type="submit" value="delete"/>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
