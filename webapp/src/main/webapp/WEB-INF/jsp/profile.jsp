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
		<h2>${user.username}</h2>
		<div class="tbl profile-cont">
			<div class="profile-top">
				<div class="profile-pic-container">
					<img src="<c:url value='/user/${user.userid}/picture'/>"/>
				</div>
				<div class="stats">
					<div style="padding: 5px 0">
						<h4><spring:message code="events_owned" /><span> 23</span></h4>
					</div>
					<div style="padding: 5px 0">
						<h4><spring:message code="total_events" /><span> 23</span></h4>
					</div>
					<div style="padding: 5px 0">
						<h4><spring:message code="favourite_sport" /><span> Soccer (12)</span></h4>
					</div>
					<div style="padding: 5px 0">
						<h4><spring:message code="main_establishment" /><span> San Lorenzo (9)</span></h4>
					</div>
				</div>
			</div>
		</div>
		<div class="tbl profile-cont profile-second">
			<div class="">

			</div>
		</div>

	</div>
</div>


</body>
</html>