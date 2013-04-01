<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>



<html>
<head>
	<title>Choose your robot</title>
	
	
	<script src="${pageContext.request.contextPath}/resources/js/jquery.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/resources/js/jquery-ui-1.8.20.custom.min.js" type="text/javascript"></script>

<script src="${pageContext.request.contextPath}/resources/js/jquery.menu.js" type="text/javascript"></script>

<link href="${pageContext.request.contextPath}/resources/css/main.css"
rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/resources/css/piemenu.css"
rel="stylesheet" type="text/css" />
	
	
	
</head>
<body>

<h1><img src="${pageContext.request.contextPath}/resources/img/robot-simili.png" height="80px" width="60px">Simili simulator</h1>

  <div id='outer_container' class="outer_container" >
		<a class="menu_button" >Choose your robot</a>
		<ul class="menu_option">
		  <li> <img src="${pageContext.request.contextPath}/resources/img/khepera.gif" style="height: 50px;width: 50px;"/><a href="./simulator?robotType=khepera3" >KheperaIII</a></li>
		  <li> <img src="${pageContext.request.contextPath}/resources/img/whiteaibo.gif" style="height: 50px;width: 50px;"/><a href="./simulator?robotType=aibo">Aibo</a></li>
		  <li><img src="${pageContext.request.contextPath}/resources/img/roomba.gif" style="height: 50px;width: 50px;"/><a href="./simulator?robotType=roomba">Roomba</a></li>
		  <li><a href="./simulator?robotType=other">Other</a></li>
		
		</ul>
	</div>

<script type="text/javascript">
	
	 $('#outer_container').PieMenu({
	        'starting_angel':0,
	        'angel_difference' : 360,
	        'radius':150,
	    }); 
	
	</script>

</body>
</html>
