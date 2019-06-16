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
			<spring:message code="sport_matcher" /> - <spring:message code="events" />
		</title>
	</head>

	<body>
    	<%@ include file="header.jsp" %>
    	<div class="main-container">
    		<%@ include file="sidebar.jsp" %>
    		<div class="content-container">
                <div class="profile-title">
                    <h2>${tournament.name}</h2>
                </div>
                <span class="help-message notice"><spring:message code="tournament_list_help"/></span>
    			<div class="tbl">
    				<div class="table-header">
                        <span><spring:message code="teams" /></span>
                        <span><spring:message code="score" /></span>
                    </div>
                    <c:forEach var="teamEntry" items="${teamsScoresMap}">
                        <div class="custom-row">
                            <div>${teamEntry.key.teamName}</div>
                            <div>${teamEntry.value}</div>
                        </div>
                    </c:forEach>
    			</div>
                <div class="tbl profile-cont">
                    <span><spring:message code="round" /> ${currRoundPage}</span>
                    <fmt:timeZone value="AR">
                        <fmt:parseDate value="${roundStartsAt}" var="parsedDateTime" type="both" pattern="yyyy-MM-dd'T'HH:mm:ss'Z'" />
                        <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }" timeZone="GMT-3" />
                    </fmt:timeZone>
                    <fmt:timeZone value="AR">
                        <fmt:parseDate value="${roundEndsAt}" var="parsedDateTime" type="both" pattern="yyyy-MM-dd'T'HH:mm:ss'Z'" />
                        <fmt:formatDate pattern="HH:mm" value="${ parsedDateTime }" timeZone="GMT-3" />
                    </fmt:timeZone>
                    <c:forEach var="event" items="${roundEvents}">
                        <cr:set var="eventid" scope="page" value="${event.eventid}"/>
                        <div class="flex">
                            <span>${event.firstTeam.teamName}</span>
                            <c:choose>
                                <c:when test="${eventsHaveResult[eventid]}">
                                    <div class="score">
                                        <span>${event.firstTeamScore}</span>
                                        <span>${event.secondTeamScore}</span>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="score">
                                        <span>-</span>
                                        <span>-</span>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                            <span>${event.secondTeam.teamName}</span>
                            <span>${event.event.pitch.name}</span>
                        </div>
                        <div>
                            <a href="<c:url value="/tournament/${tournament.tournamentid}/event/${event.eventid}"/>"> <button type="button" class="btn btn-primary view-event"><spring:message code="view_event"/></button></a>
                        </div>
                    </c:forEach>
                    <div class="table-navigator">
                        <c:if test="${currRoundPage != 1}">
                            <div>
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
                        </c:if>
                        <span class="flex"><spring:message code="showing_pages"/> ${currRoundPage} <spring:message code="of"/> ${maxRoundPage}</span>
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
