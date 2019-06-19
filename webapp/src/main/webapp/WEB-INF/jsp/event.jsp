<%@	taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@	taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib  prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/style.css' />" >
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
		<link href="https://fonts.googleapis.com/css?family=Barlow+Condensed" rel="stylesheet">
		<link href="https://fonts.googleapis.com/css?family=Archivo+Narrow" rel="stylesheet">
		<title><spring:message code="sport_matcher" /> - <spring:message code="event" /></title>
	</head>
	<body>
	<%@ include file="header.jsp" %>
	<div class="main-container">
		<%@include file="sidebar.jsp" %>
		<div class="content-container">
			<div class="profile-title">
				<h2><c:out value="${event.name}"/></h2>
			</div>
			<div class="detail-container average-min-width">
				<c:if test="${!has_ended}">
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
						<h4 class="progress-bar-completion"><c:out value="${participant_count}"/>/<c:out value="${event.maxParticipants}"/></h4>
					</div>
				</c:if>
				<div class="description-body">
					<div class="margin-right">
						<div class="description-item">
							<span class="event-info-label"><spring:message code="organizer"/> </span>
							<a class="link-text" href="<c:url value="/user/${event.owner.userid}" /> "><c:out value="${event.owner.firstname}"/> <c:out value="${event.owner.lastname}"/></a>
						</div>
						<div class="description-item">
							<span class="event-info-label"><spring:message code="event_description"/></span>
							<span><c:out value="${event.description}"/></span>
						</div>
						<div class="description-item">
							<span class="event-info-label"><spring:message code="sport"/></span>
							<span><spring:message code="${event.pitch.sport}"/></span>
						</div>
						<div class="description-item">
							<span class="event-info-label"><spring:message code="club"/> </span>
							<a class="link-text" href="<c:url value="/club/${event.pitch.club.clubid}" /> "><c:out value="${event.pitch.club.name}"/></a>
							<span> - </span><a class="link-text" href="<c:url value="/pitch/${event.pitch.pitchid}" /> "><c:out value="${event.pitch.name}"/></a>
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
								<span><c:out value="${event.maxParticipants - participant_count}" /></span>
							</div>
						</div>
						<div class="description-item">
							<span class="event-info-label"><spring:message code="end_date"/></span>
							<span>
								<fmt:timeZone value="AR">
									<fmt:parseDate value="${event.endsInscriptionAt}" var="parsedDateTime" type="both" pattern="yyyy-MM-dd'T'HH:mm:ss'Z'" />
									<fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }" timeZone="GMT-3" />
								</fmt:timeZone>
							</span>
						</div>
					</div>
					<div class="participants-list">
						<span class="event-info-label"><spring:message code="participants"/></span>
						<ul>
						    <c:forEach var="i" items="${inscriptions}">
		              <form class="participant-item" method="POST" action="<c:url value="/event/${event.eventId}/kick-user/${i.inscriptedUser.userid}"/>">
			              <a class="link-text" href="<c:url value="/user/${i.inscriptedUser.userid}" /> "><c:out value="${i.inscriptedUser.firstname}"/> <c:out value="${i.inscriptedUser.lastname}"/></a>
										<c:if test="${!has_ended && isOwner && i.inscriptedUser.userid != event.owner.userid}">
			              	<button type="submit" class="kick-user-btn"><spring:message code="kick"/></button>
										</c:if>
		              </form>
		            </c:forEach>
          	</ul>
					</div>
				</div>
				<c:if test="${has_ended && is_participant}">
					<div class="event-points">
						<h4 class="pitch-info-label"><spring:message code="event_points"/></h4>
						<h5 class="pitch-info-label vote-balance"><c:out value="${vote_balance}"/></h5>
					</div>
					<div class="voting-buttons">
						<c:if test="${loggedUser != null && loggedUser.userid != event.owner.userid}">
							<c:choose>
								<c:when test="${user_vote > 0}">
									<form method="POST" action="<c:url value="/event/${event.eventId}/downvote"/>">
										<button type="submit" class="btn btn-danger join-button"><spring:message code="downvote"/></button>
									</form>
									<div>
										<button type="submit" class="btn btn-success join-button vote-balance" disabled="true"><spring:message code="upvoted"/></button>
									</div>
								</c:when>
								<c:when test="${user_vote < 0}">
									<div>
										<button type="submit" class="btn btn-danger join-button" disabled="true"><spring:message code="downvoted"/></button>
									</div>
									<form method="POST" action="<c:url value="/event/${event.eventId}/upvote"/>">
										<button type="submit" class="btn btn-success join-button vote-balance"><spring:message code="upvote"/></button>
									</form>
								</c:when>
								<c:otherwise>
									<form method="POST" action="<c:url value="/event/${event.eventId}/downvote"/>">
										<button type="submit" class="btn btn-danger join-button"><spring:message code="downvote"/></button>
									</form>
									<form method="POST" action="<c:url value="/event/${event.eventId}/upvote"/>">
										<button type="submit" class="btn btn-success join-button vote-balance"><spring:message code="upvote"/></button>
									</form>
								</c:otherwise>
							</c:choose>
						</c:if>
					</div>
				</c:if>
			</div>
			<c:if test="${eventFullError == true}">
				<span class="form-error notice">
					<spring:message code="event_full_error"/>
				</span>
			</c:if>
			<c:if test="${alreadyJoinedError == true}">
				<span class="form-error notice">
					<spring:message code="already_joined_error"/>
				</span>
			</c:if>
			<c:if test="${userBusyError == true}">
				<span class="form-error notice">
					<spring:message code="user_busy_error"/>
				</span>
			</c:if>
			<div class="club-buttons">
				<c:if test="${!has_started}">
					<c:choose>
						<c:when test="${is_participant}">
							<form method="POST" action="<c:url value="/event/${event.eventId}/leave"/>">
								<button type="submit" class="btn btn-danger join-button"><spring:message code="leave"/></button>
							</form>
						</c:when>
						<c:otherwise>
							<c:if test="${participant_count < event.maxParticipants}">
								<form method="POST" action="<c:url value="/event/${event.eventId}/join"/>">
									<button type="submit" class="btn btn-success join-button"><spring:message code="join"/></button>
								</form>
							</c:if>
						</c:otherwise>
					</c:choose>
					<c:if test="${isOwner}">
						<form method="POST" action="<c:url value="/event/${event.eventId}/delete"/>">
							<button type="submit" class="btn btn-danger join-button mb-10"><spring:message code="cancel_event"/></button>
						</form>
					</c:if>
				</c:if>
			</div>
		</div>
	</div>


	</body>
</html>
