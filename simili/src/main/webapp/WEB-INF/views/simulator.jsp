<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>

<html>
<head>
<title>Simulator</title>

<script
	src="${pageContext.request.contextPath}/resources/js/jquery.min.js"
	type="text/javascript"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/jquery-ui-1.8.20.custom.min.js"
	type="text/javascript"></script>

<link href="${pageContext.request.contextPath}/resources/css/main.css"
	rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/resources/css/robot.css"
	rel="stylesheet" type="text/css" />
<link
	href="${pageContext.request.contextPath}/resources/css/piemenu.css"
	rel="stylesheet" type="text/css" />

<script type="text/javascript">
	var jq = jQuery.noConflict();
</script>

</head>
<body onload="getWorld()">
	<h1>Simulator  (1square = 1m*1m)</h1>

	<div style="display: none;">
		<img id="robotImg"
			src="${pageContext.request.contextPath}/resources/img/${robotType}.gif">
	</div>

	<div style="width:100%">
		<div style=" float: left;">
			<canvas id="simulator"></canvas>
		</div>
		<div style="float: right;width:33%;heigth:100%;">
			<span id="x"></span> </br>
			<span id="y"></span> </br>
			<span id="theta"></span> </br>
			<span id="velocity"></span> </br>
			<span id="angularVelocity"></span></br>
			<span id="v_right"></span></br>
			<span id="v_left"></span>
			
			
		</div>
		<div style="clear:both;"></div> 
	</div>

	<div>
		<input
			type="submit" value="START" onclick="start()" />
			<input
			type="submit" value="STOP" onclick="stop()" />
			<input
			type="submit" value="RESET" onclick="reset()" />

	</div>


	<script type="text/javascript">
		function start() {
			jq(function() {
				jq.post("./simulator/start");
			});
		}
		
		function reset() {
			jq(function() {
				jq.post("./simulator/reset");
			});
		}

		function getWorld() {
			jq(function() {
				jq.post("./simulator/getWorld", function(data) {
refreshCanvas(data);
refreshData(data);
				});
			});
			setTimeout(getWorld, 100);
		}

		function stop() {
			jq(function() {
				jq.post("./simulator/stop");
			});
		}


		function refreshCanvas(data) {

			var c = document.getElementById("simulator");

			var ctx = c.getContext("2d");

			//grid width and height
			var bw = 600;
			var bh = 600;
			//padding around grid
			var p = 20;
			//size of canvas
			var cw = bw + (p * 2) + 1;
			var ch = bh + (p * 2) + 1;

			c.width = cw;
			c.height = ch;

			function drawGrid() {
				for ( var x = 0; x <= c.width; x += 100) {
					ctx.moveTo(0.5 + x + p, p);
					ctx.lineTo(0.5 + x + p, bh + p);
				}

				for ( var x = 0; x <= c.height; x += 100) {
					ctx.moveTo(p, 0.5 + x + p);
					ctx.lineTo(bw + p, 0.5 + x + p);
				}

				ctx.strokeStyle = "black";
				ctx.stroke();
			}

			//clear all
			ctx.clearRect(0, 0, c.width, c.height);

			var scale = 100; //objects are 10 times less than normal but display 100 times bigger in px

			//grid
			drawGrid();

			//goal
			ctx.fillStyle = "#FF0000";
			ctx.fillRect(data.robotList[0].goalPosition.x * scale - 5 + p, data.robotList[0].goalPosition.y * scale - 5 + p, 10, 10);

			//robot
			ctx.save();
			var img = document.getElementById("robotImg");
			
			var x = data.robotList[0].centerPosition.x * scale + p;
			var y = data.robotList[0].centerPosition.y * scale + p;

			//center of the robot
			ctx.translate(x, y);
			ctx.rotate(data.robotList[0].centerPosition.theta);
			ctx.drawImage(img,-13,-13);
			ctx.restore();
		}
		
		function refreshData(data){
			
			jq(function() {
			jq("#x").replaceWith('<span id="x">X = '+data.robotList[0].centerPosition.x+'</span>');
			jq("#y").replaceWith('<span id="y">Y = '+data.robotList[0].centerPosition.y+'</span>');
			jq("#theta").replaceWith('<span id="theta">Theta = '+data.robotList[0].centerPosition.theta+'</span>');
			jq("#velocity").replaceWith('<span id="velocity">velocity = '+data.robotList[0].unicycleDriveState.v+'</span>');
			jq("#angularVelocity").replaceWith('<span id="angularVelocity">angularVelocity = '+data.robotList[0].unicycleDriveState.w+'</span>');
			jq("#v_right").replaceWith('<span id="v_right">v_right = '+data.robotList[0].state.v_right+'</span>');
			jq("#v_left").replaceWith('<span id="v_left">v_left = '+data.robotList[0].state.v_left+'</span>');

			
			});
		}
		
	</script>


</body>
</html>
