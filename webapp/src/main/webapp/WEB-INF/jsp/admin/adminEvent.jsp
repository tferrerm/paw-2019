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
			<h2>${event.name}</h2>
			<div class="detail-container">
				<div class="status">
					<h3><spring:message code="status"/> Uncompleted</h3>
					<div class="progress">
						<div class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar" style="width:${participant_count * 100 / event.maxParticipants}%" aria-valuenow="${participant_count}" aria-valuemin="0" aria-valuemax="${event.maxParticipants}"></div>
					</div>
					<h4 class="progress-bar-completion">${participant_count}/${event.maxParticipants}</h4>
				</div>
				<div class="description-body">
					<div>
						<div class="description-item">
							<span class="event-info-label"><spring:message code="organizer"/> </span>
							<a href="<c:url value="/user/${event.owner.userid}" /> ">${event.owner.firstname} ${event.owner.lastname}</a>
						</div>
						<div class="description-item">
							<span class="event-info-label"><spring:message code="sport"/></span>
							<span>Hardcoded</span>
						</div>
						<div class="description-item">
							<span class="event-info-label"><spring:message code="establishment"/></span>
							<span>${event.location}</span>
						</div>
						<div class="double-box">
							<div class="description-item">
								<span class="event-info-label"><spring:message code="date"/></span>
								<span>${event.startsAt}</span>
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
				              <a href="<c:url value="/user/${user.userid}" /> "><p class="event-participants">${user.firstname} ${user.lastname}</p></a>
				            </c:forEach>
			          	</ul>
					</div>
				</div>
			</div>
			<form method="POST" action="<c:url value="/admin/event/${event.eventId}/delete"/>">
				<button type="submit" class="btn btn-danger join-button"><spring:message code="remove_event"/></button>
			</form>
		</div>
	</div>


	</body>
</html>
