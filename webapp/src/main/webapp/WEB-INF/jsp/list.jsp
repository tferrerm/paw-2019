<%@	taglib	prefix="c"	uri="http://java.sun.com/jstl/core_rt"%>
<%@	taglib	prefix="form"	uri="http://www.springframework.org/tags/form"	%>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/style.css' />" >
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
		<link href="https://fonts.googleapis.com/css?family=Barlow+Condensed" rel="stylesheet">
		<link href="https://fonts.googleapis.com/css?family=Archivo+Narrow" rel="stylesheet">
	</head>
	<body>
	<%@include file="header.jsp" %>
	<div class="main-container">
		<%@include file="sidebar.jsp" %>
		<div class="content-container">
			<h2>List title</h2>
			<div class="tbl">
				<div class="table-header">
					<div>Element</div>
					<div>For</div>
					<div>Testing</div>
					<div>Hardcoded</div>
					<div>Table</div>
				</div>
				<div class="custom-row">
					<div>These</div>
					<div>Values</div>
					<div>Are</div>
					<div>Now</div>
					<div>Hardcoded</div>
				</div>
				<div class="custom-row">
					<div>These</div>
					<div>Values</div>
					<div>Are</div>
					<div>Now</div>
					<div>Hardcoded</div>
				</div>
				<div class="custom-row">
					<div>These</div>
					<div>Values</div>
					<div>Are</div>
					<div>Now</div>
					<div>Hardcoded</div>
				</div>
				<div class="custom-row">
					<div>These</div>
					<div>Values</div>
					<div>Are</div>
					<div>Now</div>
					<div>Hardcoded</div>
				</div>
			</div>
			<div class="table-navigator">
				<div><span class="navigator-button"><spring:message code="first"/></span><span class="navigator-button"><spring:message code="back"/></span></div>
				<span><spring:message code="showing_items"/> 0-5 <spring:message code="of"/> 5</span>
				<div><span class="navigator-button"><spring:message code="next"/></span><span class="navigator-button"><spring:message code="last"/></span></div>
			</div>
		</div>
	</div>


	</body>
</html>
