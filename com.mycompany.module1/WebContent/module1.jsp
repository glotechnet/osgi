<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>

	[module1.jsp] (Bundle Name: <%= application.getInitParameter(org.rsp.http.HttpActivator.BUNDLE_NAME) %>)
<p/>
	<h3>OSGi Module 1: Business Report</h3>
<p/>
	<c:import url="/myservlet"/>
<p/>
	[end module1.jsp]

