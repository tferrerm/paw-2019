<%@	taglib	prefix="c"	uri="http://java.sun.com/jstl/core_rt"%>
<%@	taglib	prefix="form"	uri="http://www.springframework.org/tags/form"	%>
<%@ taglib  prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/style.css' />" >
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
		<link href="https://fonts.googleapis.com/css?family=Barlow+Condensed" rel="stylesheet">
		<link href="https://fonts.googleapis.com/css?family=Archivo+Narrow" rel="stylesheet">
		<title>
			<spring:message code="sport_matcher" /> - <spring:message code="home" />
		</title>
	</head>
	<body>
	<%@include file="header.jsp" %>
	<div class="main-container">
		<%@include file="sidebar.jsp" %>
		<div class="content-container">
			<h3 class="home-title"><spring:message code="upcomingParticipations"/></h3>
			<c:choose>
				<c:when test="${!noParticipations}">
					<span class="help-message notice"><spring:message code="home_help"/></span>
					<div class="home-evs-container">
						<div class="home-evs-group flex">
							<c:forEach var="dayMessage" items="${scheduleHeaders}">
								<span class="home-ev-title flex flex-1"><spring:message code="${dayMessage}"/></span>
							</c:forEach>
						</div>
					</div>
					<div class="home-all-events">
						<c:forEach var="row" items="${myEvents}">
							<div class="home-ev-column">
								<c:forEach var="event" items="${row}">
									<c:choose>
										<c:when test = "${event.name != null}">
											<a class="event-link" href="<c:url value='/event/${event.eventId}' />"><span class="event-link">${event.name}</span></a>
										</c:when>
									</c:choose>
								</c:forEach>
							</div>
						</c:forEach>
					</div>
				</c:when>
				<c:otherwise>
					<h4><spring:message code="home_default"/></h4>
				</c:otherwise>
			</c:choose>
			<div class="bottom-home-container">
				<div class="bottom-home-item">
					<h3><spring:message code="introAllEvents"/></h3>
					<button><a href="<c:url value='/events/1' />"><spring:message code="allEvents"/></a></button>
				</div>
				<div class="bottom-home-item">
					<h3><spring:message code="introCreateEvent"/></h3>
					<button><a href="<c:url value='/pitches/1' />"><spring:message code="create_event"/></a></button>
				</div>
			</div>
		</div>
	</div>
	</body>
</html>
