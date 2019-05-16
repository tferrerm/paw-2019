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
			<h2><spring:message code="myEvents" /></h2>
			<c:choose>
				<c:when test="${empty pastEvents && empty future_events}">
					<div class="notice">
						<spring:message code="no_past_or_future_events"/>
					</div>
				</c:when>
				<c:otherwise>
					<div class="tbl">
						<div class="table-header">
							<div class="flex-grow justify-center my-events-tbl-sub">
								<spring:message code="pastEvents" />
							</div>
							<div class="flex-grow justify-center my-events-tbl-sub">
								<spring:message code="upcomingEvents" />
							</div>
						</div>
						<div class="flex-grow w-100">
							<div class="events-column flex-grow flex-column right-border w-50">
							    <c:forEach var="event" items="${past_events}">
										<a href="<c:url value="/event/${event.eventId}" /> "><div>${event.name}</div></a>
							    </c:forEach>
							</div>
							<div class="events-column flex-grow flex-column w-50">
							    <c:forEach var="event" items="${future_events}">
							        <a href="<c:url value="/event/${event.eventId}" /> "><div>${event.name}</div></a>
							    </c:forEach>
							</div>
						</div>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	</body>
</html>
