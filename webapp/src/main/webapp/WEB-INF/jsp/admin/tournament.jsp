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
                        <fmt:parseDate value="${roundEvents[0].startsAt}" var="parsedDateTime" type="both" pattern="yyyy-MM-dd'T'HH:mm:ss'Z'" />
                        <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }" timeZone="GMT-3" />
                    </fmt:timeZone>
                    <fmt:timeZone value="AR">
                        <fmt:parseDate value="${roundEvents[0].endsAt}" var="parsedDateTime" type="both" pattern="yyyy-MM-dd'T'HH:mm:ss'Z'" />
                        <fmt:formatDate pattern="HH:mm" value="${ parsedDateTime }" timeZone="GMT-3" />
                    </fmt:timeZone>
                    <c:forEach var="event" items="${roundEvents}">
                        <cr:set var="eventid" scope="page" value="${event.eventId}"/>
                        <c:choose>
                            <c:when test="${roundInPast}">
                                <c:url value="/admin/tournament/${tournament.tournamentid}/event/${eventid}/result" var="postPath"/>
                                <form:form id="tournamentResultForm" modelAttribute="tournamentResultForm" action="${postPath}" class="comments_form">
                                    <div>
                                        <span>${event.firstTeam.teamName}</span>
                                        <c:choose>
                                            <c:when test="${eventsHaveResult[eventid]}">
                                                <form:input class="form-control" type="number" min="0" path="firstResult" maxlength="3" value="${event.firstTeamScore}"/>
                                                <form:errors path="firstResult" cssClass="form-error" element="span"/>

                                                <form:input class="form-control" type="number" min="0" path="secondResult" maxlength="3" value="${event.secondTeamScore}"/>
                                                <form:errors path="secondResult" cssClass="form-error" element="span"/>
                                            </c:when>
                                            <c:otherwise>
                                                <form:input class="form-control" type="number" min="0" path="firstResult" maxlength="3"/>
                                                <form:errors path="firstResult" cssClass="form-error" element="span"/>

                                                <form:input class="form-control" type="number" min="0" path="secondResult" maxlength="3"/>
                                                <form:errors path="secondResult" cssClass="form-error" element="span"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <span>${event.secondTeam.teamName}</span>
                                        <span>${event.pitch.name}</span>
                                    </div>
                                    <div class="submit-container">
                                        <button type="submit" class="btn btn-primary submit-btn btn-primary"><spring:message code="comment_action"/></button>
                                    </div>
                                </form:form>
                            </c:when>
                            <c:otherwise>
                                <div>
                                    <span>${event.firstTeam.teamName}</span>
                                    <span>-</span>
                                    <span>-</span>
                                    <span>${event.secondTeam.teamName}</span>
                                    <span>${event.pitch.name}</span>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                    <div class="table-navigator w-100 justify-center">
                      <div class="${currRoundPage != 1 ? "" : "hidden"}">
                        <a href="<c:url value='/admin/tournament/${tournament.tournamentid}?round=1' />">
                          <button type="button" class="btn btn-secondary">
                              <spring:message code="first"/>
                          </button>
                        </a>
                        <a href="<c:url value='/admin/tournament/${tournament.tournamentid}?round=${currRoundPage-1}' />">
                          <button type="button" class="btn btn-secondary">
                              <spring:message code="back"/>
                          </button>
                        </a>
                      </div>
                      <span class="flex"><spring:message code="showing_pages"/> ${currRoundPage} <spring:message code="of"/> ${maxRoundPage}</span>
                      <div class="${currRoundPage != maxRoundPage ? "" : "hidden"}">
                        <a href="<c:url value='/admin/tournament/${tournament.tournamentid}?round=${currRoundPage+1}' />">
                            <button type="button" class="btn btn-secondary"><spring:message code="next"/></button>
                        </a>
                        <a href="<c:url value='/admin/tournament/${tournament.tournamentid}?round=${maxRoundPage}' />">
                            <button type="button" class="btn btn-secondary"><spring:message code="last"/></button>
                        </a>
                      </div>
                    </div>
                </div>
            </div>
        </div>
	</body>
</html>
