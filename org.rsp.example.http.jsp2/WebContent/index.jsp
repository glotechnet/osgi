<html>
<head>
	<link href="./../css/my.css?version=<%= Math.random() %>" rel="stylesheet" type="text/css"/>
</head>
<body>
	[jsp2 index.jsp] (Bundle Name: ${initParam['org.rsp.http.HttpActivator.BUNDLE_NAME']})<br/>
<hr/>
	<jsp:include page="/sub.jsp" />
<hr/>
	<img src="images/tomcat-power.gif" border="0"/>
<p/>
	Working with server: <%= application.getServerInfo() %><br/>
	Servlet Specification: <%= application.getMajorVersion() %>.<%= application.getMinorVersion() %> <br>
	JSP version: <%= JspFactory.getDefaultFactory().getEngineInfo().getSpecificationVersion() %><br>
	Java Version: <%= System.getProperty("java.version") %>
<p/>
	ContextPath: <%= request.getContextPath() %><br/>
	Bundle URI Namespace: <%= application.getInitParameter(org.rsp.http.HttpActivator.BUNDLE_URI_NAMESPACE) %><br/>
	ServletPath: <%= request.getServletPath() %><br/>
	Actual path: <%= application.getRealPath("/") %>
<hr/>
	El is working ?  ${1 == 1}<br/>
	1 + 1 = ${1 + 1}
<hr/>
[jsp2 index.jsp end]
</body>
</html>