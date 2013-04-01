<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Simulator</title>
	
		<script src="${pageContext.request.contextPath}/resources/js/jquery.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/resources/js/jquery-ui-1.8.20.custom.min.js" type="text/javascript"></script>

<script src="${pageContext.request.contextPath}/resources/js/jquery.menu.js" type="text/javascript"></script>

<link href="${pageContext.request.contextPath}/resources/css/main.css"
rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/resources/css/piemenu.css"
rel="stylesheet" type="text/css" />
	
</head>
<body>
<h1>
	Simulator  
</h1>

<P>  Here comes the simulator for : <c:out value="${robotType}"/></P>
</body>
</html>
