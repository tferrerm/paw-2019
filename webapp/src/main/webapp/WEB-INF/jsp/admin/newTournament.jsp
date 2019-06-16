<%@	taglib	prefix="c"	uri="http://java.sun.com/jstl/core_rt"%>
<%@	taglib	prefix="form"	uri="http://www.springframework.org/tags/form"	%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib  prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/style.css' />" >
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
		<link href="https://fonts.googleapis.com/css?family=Barlow+Condensed" rel="stylesheet">
		<link href="https://fonts.googleapis.com/css?family=Archivo+Narrow" rel="stylesheet">
		<title>
			<spring:message code="sport_matcher" /> - <spring:message code="new_club" />
		</title>
	</head>
	<body>
	<%@include file="header.jsp" %>
	<div class="main-container">
		<%@include file="sidebar.jsp" %>
		<div class="content-container">
			<h2><spring:message code="create_tournament"/></h2>
			<div class="form-container-tournament">
				<table class="schedule-table flex">
                    <tr>
                        <th class="schedule-table-cell-size schedule-table-hours"></th>
                        <c:forEach var="dayMessage" items="${scheduleHeaders}">
                            <th class="schedule-table-cell-size"><spring:message code="${dayMessage}"/></th>
                        </c:forEach>
                    </tr>
                    <c:set var="hours" value="${minHour}"/>
                    <c:forEach var="row" items="${schedule}">
                        <tr>
                            <td class="schedule-table-hours"><c:out value="${hours}"/>:00</td>
                            <c:forEach var="column" items="${row}">
                            	<c:choose>
                                    <c:when test="${(pitchQty - column) == 0}">
                                        <td class="schedule-table background-red schedule-table-cell-size">${pitchQty - column}</td>
                                    </c:when>
                                    <c:otherwise>
                                        <td class="schedule-table background-yellow schedule-table-cell-size">${pitchQty - column}</td>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </tr>
                        <c:set var="hours" value="${hours + 1}"/>
                    </c:forEach>
                </table>
				<c:url value="/admin/club/${club.clubid}/tournament/create" var="postPath"/>
				<div class="left-space">
				<form:form modelAttribute="newTournamentForm" action="${postPath}" method="post" enctype="multipart/form-data">
					<div>
						<form:label path="name"><spring:message code="name"/> * </form:label>
						<form:input  cssClass="form-control" type="text" maxlength="100" path="name"/>
						<form:errors path="name" cssClass="form-error" element="span"/>
					</div>
					<div>
						<form:label path="maxTeams"><spring:message code="team_number"/> * </form:label>
						<form:input  cssClass="form-control" type="number" maxlength="100" path="maxTeams"/>
						<form:errors path="maxTeams" cssClass="form-error" element="span"/>
					</div>
					<div>
						<form:label path="teamSize"><spring:message code="team_size"/> * </form:label>
						<form:input  cssClass="form-control" type="number" maxlength="100" path="teamSize"/>
						<form:errors path="teamSize" cssClass="form-error" element="span"/>
					</div>
					<div class="form-field flex-space-between">
                        <div class="form-field-horizontal">
                            <form:label path="firstRoundDate"><spring:message code="new_event_date"/> *</form:label>
                            <form:input cssClass="form-control date-input" type="date" path="firstRoundDate"/>
                            <form:errors path="firstRoundDate" cssClass="form-error" element="span"/>
                        </div>
                        <div>
                            <form:label path="startsAtHour"><spring:message code="event_startsAt"/> *</form:label>
                            <form:select path="startsAtHour" cssClass="form-control">
                                <c:forEach var="hourEntry" items="${availableHours}">
                                    <c:if test="${hourEntry.key < maxHour}">
                                        <form:option value="${hourEntry.key}">${hourEntry.value}</form:option>
                                    </c:if>
                                </c:forEach>
                            </form:select>
                            <form:errors path="startsAtHour" cssClass="form-error" element="span"/>
                        </div>
                        <div>
                            <form:label path="endsAtHour"><spring:message code="event_endsAt"/> *</form:label>
                            <form:select path="endsAtHour" cssClass="form-control">
                                <c:forEach var="hourEntry" items="${availableHours}">
                                    <c:if test="${hourEntry.key > minHour}">
                                        <form:option value="${hourEntry.key}">${hourEntry.value}</form:option>
                                    </c:if>
                                </c:forEach>
                            </form:select>
                            <form:errors path="endsAtHour" cssClass="form-error" element="span"/>
                        </div>
                    </div>
                    <div>
                        <form:label path="inscriptionEndDate"><spring:message code="end_date"/> *</form:label>
                        <form:input cssClass="form-control w-100" type="datetime-local" path="inscriptionEndDate"/>
                        <form:errors path="inscriptionEndDate" cssClass="form-error" element="span"/>
                    </div>
					<div class="submit-container">
						<button type="submit" class="btn btn-primary submit-btn btn-success"><spring:message code="create"/></button>
					</div>
				</form:form>
				</div>
			</div>
		</div>
	</div>


	</body>

</html>
