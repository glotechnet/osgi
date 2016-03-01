<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="rsp" value="" scope="application" />
<html>
<head>
<link href="./css/my.css?version=<%= Math.random() %>" rel="stylesheet" type="text/css" />
</head>
<body>
	[root:home.jsp] "Look Ma, no WAR!"
<hr />
	<c:import url="${rsp}/module1/module1.jsp" />
<hr />
	<c:import url="${rsp}/module2/module2.jsp" />
<hr />
	Menu | <c:import url="${rsp}/jsp2/menuItem.jsp"/>	
<hr />
	[end root:home.jsp]
</body>
</html>