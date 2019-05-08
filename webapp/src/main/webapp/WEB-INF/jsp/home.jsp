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
			<h3><spring:message code="favourite_sport_events"/></h3>
			<ul class="home-event">
			  <li class="flex-2 home-header"><spring:message code="event_name"/></li>
			  <li class="flex-2 home-header"><spring:message code="event_location"/></li>
			  <li class="flex-1 home-header"><spring:message code="sport"/></li>
			  <li class="flex-1 home-header"><spring:message code="vacancy"/></li>
			</ul>
			<div class="bottom-home-container">
				<div class="bottom-home-item">
					<h3><spring:message code="introAllEvents"/></h3>
					<button><a href="<c:url value='/events/1' />"><spring:message code="allEvents"/></a></button>
				</div>
				<div class="bottom-home-item">
					<h3><spring:message code="introCreateEvent"/></h3>
					<button><a href="<c:url value='/event/new' />"><spring:message code="create_event"/></a></button>
				</div>
			</div>
		</div>
	</div>
	</body>
</html>
