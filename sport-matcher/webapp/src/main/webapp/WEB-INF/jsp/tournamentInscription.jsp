<%@	taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@	taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix = "cr" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/style.css' />" >
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
		<link href="https://fonts.googleapis.com/css?family=Barlow+Condensed" rel="stylesheet">
		<link href="https://fonts.googleapis.com/css?family=Archivo+Narrow" rel="stylesheet">
		<title><spring:message code="sport_matcher" /> - <spring:message code="tournaments" /></title>
	</head>
	<body>
	<%@ include file="header.jsp" %>
	<div class="main-container">
		<%@include file="sidebar.jsp" %>
		<div class="content-container">
			<div class="profile-title">
				<h2><c:out value="${tournament.name}"/></h2>
			</div>
			<span class="help-message notice w-70 justify-center"><spring:message code="tournament_inscription"/></span>
			<span class="help-message notice w-70 justify-center">
				<spring:message code="tournament_description_one" />
				<c:out value="${roundsAmount}"/>
				<spring:message code="tournament_description_two" />
				<fmt:timeZone value="AR">
					<fmt:parseDate value="${startsAt}" var="parsedDateTime" type="both" pattern="yyyy-MM-dd'T'HH:mm:ss'Z'" />
					<fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }" timeZone="GMT-3" />
				</fmt:timeZone>
			</span>
			<div class="w-50 mv-10">
				<div class="comment-profile-row">
					<span class="event-info-label"><spring:message code="club" /></span>
					<span>: <c:out value="${club.name}"/></span>
				</div>
				<div>
					<span class="event-info-label"><spring:message code="end_date"/><span>
					<span>
						: <fmt:timeZone value="AR">
							<fmt:parseDate value="${tournament.endsInscriptionAt}" var="parsedDateTime" type="both" pattern="yyyy-MM-dd'T'HH:mm:ss'Z'" />
							<fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }" timeZone="GMT-3" />
						</fmt:timeZone>
					</span>
				</div>
			</div>
			<div class="detail-container minw-300">
				<div class="w-100 flex flex-grow justify-center">
					<h3><spring:message code="teams"/></h3>
				</div>
				<div class="flex-column w-100">
					<cr:forEach begin="0" end="${tournament.maxTeams - 1}" step="2" var="teamIndex">
						<div>
							<div class="flex">
							<div class="team-description minw-100 w-100">
								<span class="event-info-label"><spring:message code="${teams[teamIndex].teamName}"/></span>
								<div class="flex w-100">
									<div class="flex flex-column align-center w-100">
										<cr:set var="teamid" scope="page" value="${teams[teamIndex].teamid}"/>
										<cr:forEach begin="1" end="${fn:length(teamsUsers[teamid])}" step="1" var="userIndex">
											<a class="link-text" href="<c:url value="/user/${teamsUsers[teamid][userIndex-1].userid}" /> "><c:out value="${teamsUsers[teamid][userIndex-1].firstname}"/> <c:out value="${teamsUsers[teamid][userIndex-1].lastname}"/></a>
										</cr:forEach>
										<cr:forEach begin="1" end="${tournament.teamSize - fn:length(teamsUsers[teamid])}" step="1">
											<span>-</span>
										</cr:forEach>
									</div>
								</div>
								<div class="flex flex-grow justify-center">
									<c:if test="${!userJoined && (tournament.teamSize > fn:length(teamsUsers[teamid]))}">
										<form method="POST" action="<c:url value="/tournament/${tournament.tournamentid}/team/${teams[teamIndex].teamid}/join"/>">
											<button type="submit" class="btn btn-primary view-event"><spring:message code="join_team"/></button>
										</form>
									</c:if>
								</div>
							</div>
							<div class="team-description minw-100 w-100">
								<span class="event-info-label"><spring:message code="${teams[teamIndex + 1].teamName}"/></span>
								<div class="flex w-100">
									<div class="flex flex-column align-center w-100">
										<cr:set var="teamid" scope="page" value="${teams[teamIndex + 1].teamid}"/>
										<cr:forEach begin="1" end="${fn:length(teamsUsers[teamid])}" step="1" var="userIndex">
											<a class="link-text" href="<c:url value="/user/${teamsUsers[teamid][userIndex-1].userid}" /> "><c:out value="${teamsUsers[teamid][userIndex-1].firstname}"/> <c:out value="${teamsUsers[teamid][userIndex-1].lastname}"/></a>
										</cr:forEach>
										<cr:forEach begin="1" end="${tournament.teamSize - fn:length(teamsUsers[teamid])}" step="1">
											<span>-</span>
										</cr:forEach>
									</div>
								</div>
								<div class="flex flex-grow justify-center">
									<c:if test="${!userJoined && (tournament.teamSize > fn:length(teamsUsers[teamid]))}">
										<form method="POST" action="<c:url value="/tournament/${tournament.tournamentid}/team/${teams[teamIndex + 1].teamid}/join"/>">
											<button type="submit" class="btn btn-primary view-event"><spring:message code="join_team"/></button>
										</form>
									</c:if>
								</div>
							</div>
						</div>
					</cr:forEach>
				</div>
			</div>
			<c:if test="${userBusyError == true}">
				<span class="form-error notice">
					<spring:message code="user_busy_error"/>
				</span>
			</c:if>
			<c:if test="${userJoined}">
				<form class="progress-bar-completion" method="POST" action="<c:url value="/tournament/${tournament.tournamentid}/leave"/>">
					<button type="submit" class="btn btn-danger join-button"><spring:message code="leave"/></button>
				</form>
			</c:if>
		</div>
	</div>


	</body>
</html>