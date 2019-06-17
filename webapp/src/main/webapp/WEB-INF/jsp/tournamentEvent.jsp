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
			<spring:message code="sport_matcher" /> - <spring:message code="tournamentEvents" />
		</title>
	</head>
	<body>
		<%@ include file="header.jsp" %>
		<div class="main-container">
			<%@ include file="sidebar.jsp" %>
			<div class="content-container">
				<a class="link-text detail-box-data w-100" href="<c:url value="/tournament/${tournament.tournamentid}" /> "><spring:message code="go_to_tournament"/></a>
				<div class="profile-title">
                    <h2>${tournamentEvent.name}</h2>
                </div>
                <span class="help-message notice"><spring:message code="event_completed_description"/></span>
				<div class="detail-box">
					<div class="event-detail background-dodgerblue">
						<div class="color-white event-info-label"><spring:message code="event_detail"/></div>
					</div>
					<div>
						<div class="detail-box-data"><spring:message code="tournament"/>: ${tournament.name}</div>
						<div class="detail-box-data"><spring:message code="club"/>: ${tournament.tournamentClub.name}</div>
						<div class="detail-box-data"><spring:message code="pitch"/>: ${tournamentEvent.pitch.name}</div>
						<div class="detail-box-data">
							<spring:message code="start"/>:
							<fmt:timeZone value="AR">
                  <fmt:parseDate value="${tournamentEvent.startsAt}" var="parsedDateTime" type="both" pattern="yyyy-MM-dd'T'HH:mm:ss'Z'" />
                  <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }" timeZone="GMT-3" />
              </fmt:timeZone>
						</div>
						<div class="detail-box-data">
							<spring:message code="end"/>:
              <fmt:timeZone value="AR">
                  <fmt:parseDate value="${tournamentEvent.endsAt}" var="parsedDateTime" type="both" pattern="yyyy-MM-dd'T'HH:mm:ss'Z'" />
                  <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }" timeZone="GMT-3" />
              </fmt:timeZone>
						</div>
					</div>
				</div>
				<div class="tbl">
					<div class="table-header">
						<div class="flex-grow justify-center my-events-tbl-sub">
							${tournamentEvent.firstTeam.teamName}
						</div>
						<div class="flex-grow justify-center my-events-tbl-sub">
							${tournamentEvent.secondTeam.teamName}
						</div>
					</div>
					<div class="flex-grow w-100">
						<div class=" flex-grow flex-column right-border w-50">
							<div class="score">
								${tournamentEvent.firstTeamScore}
							</div>
							<div class="team-description">
								<div class="flex flex-column align-center w-100">
									<c:forEach var="teamMember" items="${firstTeamMembers}">
										<span>${teamMember.firstname} ${teamMember.lastname}</span>
									</c:forEach>
								</div>
							</div>
						</div>
						<div class="flex-grow flex-column w-50">
						    <div class="score">
								${tournamentEvent.secondTeamScore}
							</div>
							<div class="team-description">
								<div class="flex flex-column align-center w-100">
									<c:forEach var="teamMember" items="${secondTeamMembers}">
										<span>${teamMember.firstname} ${teamMember.lastname}</span>
									</c:forEach>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>
