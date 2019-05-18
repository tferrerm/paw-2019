<%@	taglib	prefix="c"	uri="http://java.sun.com/jstl/core_rt"%>
<%@	taglib	prefix="form"	uri="http://www.springframework.org/tags/form"	%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/style.css' />" >
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
	<link href="https://fonts.googleapis.com/css?family=Barlow+Condensed" rel="stylesheet">
	<link href="https://fonts.googleapis.com/css?family=Archivo+Narrow" rel="stylesheet">
	<title>Sport Matcher - History</title>
</head>
<body>
<%@include file="header.jsp" %>
<div class="main-container">
	<%@include file="sidebar.jsp" %>
	<div class="content-container">
		<h2><spring:message code="myParticipations" /></h2>
		<div class="tbl">
			<div class="table-header">
				<div class="flex-grow justify-center my-events-tbl-sub">
					<spring:message code="pastParticipation" />
				</div>
				<div class="flex-grow justify-center my-events-tbl-sub">
					<spring:message code="upcomingParticipations" />
				</div>
			</div>
			<div class="flex-grow w-100">
				<div class="events-column flex-grow flex-column right-border w-50">
				    <c:forEach var="event" items="${past_participations}">
							<a href="<c:url value="/event/${event.eventId}" /> ">
								<div class="my-event-item">
									<span>${event.name}</span>
									<span><spring:message code="${event.pitch.sport}"/></span>
									<fmt:timeZone value="AR">
										<fmt:parseDate value="${event.startsAt}" var="parsedDateTime" type="both" pattern="yyyy-MM-dd'T'HH:mm:ss'Z'" />
										<fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }" timeZone="GMT-3" />
									</fmt:timeZone>
								</div>
							</a>
				    </c:forEach>
				</div>
				<div class="events-column flex-grow flex-column w-50">
				    <c:forEach var="event" items="${future_participations}">
				        <a href="<c:url value="/event/${event.eventId}" /> ">
							<div class="my-event-item">
								<span>${event.name}</span>
								<span><spring:message code="${event.pitch.sport}"/></span>
								<fmt:timeZone value="AR">
									<fmt:parseDate value="${event.startsAt}" var="parsedDateTime" type="both" pattern="yyyy-MM-dd'T'HH:mm:ss'Z'" />
									<fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }" timeZone="GMT-3" />
								</fmt:timeZone>
							</div>
				        </a>
				    </c:forEach>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
</html>
