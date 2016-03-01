<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="rsp" value="/rsp" scope="request" />

<html>
<head>
<link href=".${rsp}/css/my.css?version=<%= Math.random() %>" rel="stylesheet" type="text/css" />
</head>
<body>
	[home.jsp]
<hr />
	<c:import url="${rsp}/module1/module1.jsp" />
<hr />
	<c:import url="${rsp}/module2/module2.jsp" />
<hr />
	Menu <c:import url="${rsp}/jsp2/menuItem.jsp"/>
<hr />
	[end home.jsp]
</body>
</html>