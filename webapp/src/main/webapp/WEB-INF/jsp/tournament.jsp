<%@	taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@	taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib  prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix = "cr" uri = "http://java.sun.com/jsp/jstl/core" %>
<html>
	<head>
        <script
            src="http://code.jquery.com/jquery-3.3.1.min.js"
            integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
            crossorigin="anonymous"></script>
        <script type="text/javascript" src="<c:url value='/resources/js/main.js' />"></script>
		<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/style.css' />" >
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
		<link href="https://fonts.googleapis.com/css?family=Barlow+Condensed" rel="stylesheet">
		<link href="https://fonts.googleapis.com/css?family=Archivo+Narrow" rel="stylesheet">
		<title>
			<spring:message code="sport_matcher" /> - <spring:message code="tournaments" />
		</title>
	</head>

	<body>
    	<%@ include file="header.jsp" %>
    	<div class="main-container">
    		<%@ include file="sidebar.jsp" %>
    		<div class="content-container">
          <div class="profile-title">
              <h2><c:out value="${tournament.name}"/></h2>
          </div>
          <span class="help-message notice w-70 justify-center"><spring:message code="tournament_description"/></span>
					<c:if test="${currentRound != null}">
						<div class="progress">
							<div class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar" style="width:${currentRound * 100 / maxRoundPage}%; background-color: ${currentRound == maxRoundPage ? "green" : "dodgerblue"};" aria-valuenow="${currentRound}" aria-valuemin="0" aria-valuemax="${maxRoundPage}"></div>
						</div>
					</c:if>
    			<div class="tbl">
						<div class="background-dodgerblue table-header">
                <span class="justify-center flex-1 color-white mt-10 event-info-label"><spring:message code="teams" /></span>
                <span class="justify-center flex-1 color-white mt-10 event-info-label"><spring:message code="score" /></span>
            </div>
            <c:forEach var="teamEntry" items="${teamsScoresMap}">
                <div class="custom-row">
                    <div class="justify-center flex-1"><spring:message code="${teamEntry.key.teamName}"/></div>
                    <div class="justify-center flex-1"><c:out value="${teamEntry.value}"/></div>
                </div>
            </c:forEach>
    			</div>
          <div class="tbl profile-cont">
              <span class="event-info-label notice"><spring:message code="round" /> <c:out value="${currRoundPage}"/></span>
							<span>
								<spring:message code="date" />:
                <fmt:timeZone value="AR">
                    <fmt:parseDate value="${roundEvents[0].startsAt}" var="parsedDateTime" type="both" pattern="yyyy-MM-dd'T'HH:mm:ss'Z'" />
                    <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }" timeZone="GMT-3" />
                </fmt:timeZone>
                <fmt:timeZone value="AR">
                    <fmt:parseDate value="${roundEvents[0].endsAt}" var="parsedDateTime" type="both" pattern="yyyy-MM-dd'T'HH:mm:ss'Z'" />
                    <fmt:formatDate pattern="HH:mm" value="${ parsedDateTime }" timeZone="GMT-3" />
                </fmt:timeZone>
							</span>
              <c:forEach var="event" items="${roundEvents}">
                  <cr:set var="eventid" scope="page" value="${event.eventId}"/>
                  <div class="flex pitch-item w-100 justify-center">
                      <span class="event-info-label"><spring:message code="${event.firstTeam.teamName}"/></span>
                      <c:choose>
                          <c:when test="${eventsHaveResult[eventid]}">
                              <div class="score score-tournament">
                                  <span><c:out value="${event.firstTeamScore}"/></span>
                                  <span><c:out value="${event.secondTeamScore}"/></span>
                              </div>
                          </c:when>
                          <c:otherwise>
                              <div class="score score-tournament">
                                  <span>-</span>
                                  <span>-</span>
                              </div>
                          </c:otherwise>
                      </c:choose>
                      <span class="event-info-label"><spring:message code="${event.secondTeam.teamName}"/></span>
                  </div>
                  <div class="w-100 justify-center">
                      <a href="<c:url value="/tournament/${tournament.tournamentid}/event/${eventid}"/>"> <button type="button" class="btn btn-primary view-event"><spring:message code="view_event"/></button></a>
                  </div>
              </c:forEach>
              <div class="table-navigator w-100 justify-center">
									<div class="${currRoundPage != 1 ? "" : "hidden"}">
											<a href="<c:url value='/tournament/${tournament.tournamentid}?round=1' />">
													<button type="button" class="btn btn-secondary">
															<spring:message code="first"/>
													</button>
											</a>
											<a href="<c:url value='/tournament/${tournament.tournamentid}?round=${currRoundPage-1}' />">
													<button type="button" class="btn btn-secondary">
															<spring:message code="back"/>
													</button>
											</a>
									</div>
                  <span class="flex create-event"><spring:message code="showing_pages"/> <c:out value="${currRoundPage}"/> <spring:message code="of"/> <c:out value="${maxRoundPage}"/></span>
                  <c:if test="${currRoundPage != maxRoundPage}">
                      <div>
                          <a href="<c:url value='/tournament/${tournament.tournamentid}?round=${currRoundPage+1}' />">
                              <button type="button" class="btn btn-secondary"><spring:message code="next"/></button>
                          </a>
                          <a href="<c:url value='/tournament/${tournament.tournamentid}?round=${maxRoundPage}' />">
                              <button type="button" class="btn btn-secondary"><spring:message code="last"/></button>
                          </a>
                      </div>
                  </c:if>
              </div>
          </div>
            </div>
        </div>
	</body>
</html>
