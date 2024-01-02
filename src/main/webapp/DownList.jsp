<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.io.UnsupportedEncodingException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<ul>
    <c:forEach var="filename" items="${fileNames}">
        <li>
            <a href="${pageContext.request.contextPath}/DownloadFile.do?fileName=${filename}">${filename}</a>
        </li>
    </c:forEach>

</ul>
</body>
</html>
