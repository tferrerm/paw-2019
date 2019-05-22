<%@	taglib	prefix="c"	uri="http://java.sun.com/jstl/core_rt" %>
<%@	taglib	prefix="form"	uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib  prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/style.css' />" >
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
		<link href="https://fonts.googleapis.com/css?family=Barlow+Condensed" rel="stylesheet">
		<link href="https://fonts.googleapis.com/css?family=Archivo+Narrow" rel="stylesheet">
		<title>
			<spring:message code="sport_matcher" /> - <spring:message code="event" />
		</title>
	</head>
	<body>
		<%@ include file="header.jsp" %>
		<div class="main-container">
			<%@include file="sidebar.jsp" %>
			<div class="content-container">
				<div class="profile-title">
					<h2>${event.name}</h2>
				</div>
				<div class="detail-container">
					<div class="status">
						<c:choose>
							<c:when test="${participant_count < event.maxParticipants}">
								<h3><spring:message code="status"/> <spring:message code="uncompleted"/></h3>
							</c:when>
							<c:otherwise>
								<h3><spring:message code="status"/> <spring:message code="completed"/></h3>
							</c:otherwise>
						</c:choose>
						<div class="progress">
							<div class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar" style="width:${participant_count * 100 / event.maxParticipants}%; background-color: ${participant_count == event.maxParticipants ? "green" : "dodgerblue"};" aria-valuenow="${participant_count}" aria-valuemin="0" aria-valuemax="${event.maxParticipants}"></div>
						</div>
						<h4 class="progress-bar-completion">${participant_count}/${event.maxParticipants}</h4>
					</div>
					<div class="description-body">
						<div class="margin-right">
							<div class="description-item">
								<span class="event-info-label"><spring:message code="organizer"/> </span>
								<a href="<c:url value="/user/${event.owner.userid}" /> ">${event.owner.firstname} ${event.owner.lastname}</a>
							</div>
							<div class="description-item">
								<span class="event-info-label"><spring:message code="sport"/></span>
								<span><spring:message code="${event.pitch.sport}"/></span>
							</div>
							<div class="description-item">
								<span class="event-info-label"><spring:message code="club"/></span>
								<span>${event.pitch.name}</span>
							</div>
							<div class="double-box">
								<div class="description-item">
									<span class="event-info-label"><spring:message code="date"/></span>
									<span class="just-row">
										<fmt:timeZone value="AR">
											<fmt:parseDate value="${event.startsAt}" var="parsedDateTime" type="both" pattern="yyyy-MM-dd'T'HH:mm:ss'Z'" />
											<fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }" timeZone="GMT-3" />
										</fmt:timeZone>
										<span class="date-separator">-</span>
										<fmt:timeZone value="AR">
											<fmt:parseDate value="${event.endsAt}" var="parsedDateTime" type="both" pattern="yyyy-MM-dd'T'HH:mm:ss'Z'" />
											<fmt:formatDate pattern="HH:mm" value="${ parsedDateTime }" timeZone="GMT-3" />
										</fmt:timeZone>
									</span>
								</div>
								<div class="description-item">
									<span class="event-info-label"><spring:message code="vacancies"/></span>
									<span>${event.maxParticipants - participant_count}</span>
								</div>

							</div>
						</div>
						<div class="participants-list">
							<span class="event-info-label"><spring:message code="participants"/></span>
							<ul>
							    <c:forEach var="user" items="${participants}">
					              	<p class="event-participants">${user.firstname} ${user.lastname}</p>
					            </c:forEach>
				          	</ul>
						</div>
					</div>
				</div>
				<form method="POST" action="<c:url value="/admin/event/${event.eventId}/delete"/>">
					<button type="submit" class="btn btn-danger join-button"><spring:message code="delete_event"/></button>
				</form>
			</div>
		</div>
	</body>
</html>
