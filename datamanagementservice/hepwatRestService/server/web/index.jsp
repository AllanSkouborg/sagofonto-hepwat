<%--
  Created by IntelliJ IDEA.
  User: margit.kjaer.bomholt
  Date: 23-03-2018
  Time: 16:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>hepwatRestService</title>
</head>
<body>

<p><a href="${requestScope['javax.servlet.forward.request_uri']}/rest/objecttype">Object type</a>
<p><a href="${requestScope['javax.servlet.forward.request_uri']}/rest/componenttype">Component type</a>
</body>
</html>
