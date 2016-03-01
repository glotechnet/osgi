<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>

	[jsp sub.jsp]  
<br>
	JSP: &lt;%= "myvalue" %&gt;: <%= "myvalue" %> 
<br>	
	JSTL: &lt;c:out value="value" /&gt;: <c:out value="outvalue"/>
<br>	
	ContextPath: <%= request.getContextPath() %>
<br>
	EL: pageContext.request.contextPath: ${contextPath}
<br>
	[jsp sub.jsp end]
