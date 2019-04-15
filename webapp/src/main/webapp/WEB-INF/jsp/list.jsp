<%@	taglib	prefix="c"	uri="http://java.sun.com/jstl/core_rt"%>
<%@	taglib	prefix="form"	uri="http://www.springframework.org/tags/form"	%>
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
	</head>

	<body>
	<%@include file="header.jsp" %>
	<div class="main-container">
		<%@include file="sidebar.jsp" %>
		<div class="content-container">
			<h2><spring:message code="allEvents" /></h2>
			<div class="tbl">
				<div class="table-header">
                    <c:url value='/events/filter' var="postPath"/>
                    <form:form id="searchfilters" class="searchfilters" modelAttribute="filtersForm" action="${postPath}">
                        <div class="table-titles">
                            <div>
                                <form:label path="establishment"><spring:message code="establishment" /></form:label>
                                <form:input class="form-control" type="text" path="establishment"/>
                                <form:errors path="establishment" cssClass="formError" element="p"/>
                            </div>
                            <div>
                                <form:label path="sport"><spring:message code="sport" /></form:label>
                                <form:input class="form-control" type="text" path="sport"/>
                                <form:errors path="sport" cssClass="form-error" element="p"/>
                            </div>
                            <div>
                                <form:label path="organizer"><spring:message code="organizer" /></form:label>
                                <form:input class="form-control" type="text" path="organizer"/>
                                <form:errors path="organizer" cssClass="form-error" element="p"/>
                            </div>
                            <div>
                                <form:label path="vacancies"><spring:message code="vacancies" /></form:label>
                                <form:input class="form-control" type="text" path="vacancies"/>
                                <form:errors path="vacancies" cssClass="form-error" element="p"/>
                            </div>
                            <div>
                                <form:label path="date"><spring:message code="date" /></form:label>
                                <form:input class="form-control" type="text" path="date"/>
                                <form:errors path="date" cssClass="form-error" element="p"/>
                            </div>
                            <div>
                                <button class="btn btn-primary" type="submit"><spring:message code="filter" /></button>
                            </div>
                        </div>
                    </form:form>

                </div>
                <c:forEach var="event" items="${events}">
                    <div class="custom-row">
                        <div>${event.location}</div>
                        <div>Hardcoded</div>
                        <div>${event.owner.firstname} ${event.owner.lastname}</div>
                        <div>${event.maxParticipants}</div>
                        <div>${event.startsAt}</div>
                        <div>
                            <a href="<c:url value="/event/${event.eventId}"/>"> <button type="button" class="btn btn-primary view-event"><spring:message code="view_event"/></button></a>
                        </div>
                    </div>
                </c:forEach>
			</div>
			<div class="table-navigator">
				<div>
                    <a href="<c:url value='/events/1${queryString}' />"><button type="button" class="btn btn-secondary"><spring:message code="first"/></button></a>
                    <a href="<c:url value='/events/${page-1}${queryString}' />"><button type="button" class="btn btn-secondary"><spring:message code="back"/></button></a>
                </div>
				<span><spring:message code="showing_items"/> 0-5 <spring:message code="of"/> 5</span>
				<div>
                    <a href="<c:url value='/events/${page+1}${queryString}' />"><button type="button" class="btn btn-secondary"><spring:message code="next"/></button></a>
                    <a href="<c:url value='/events/${lastPageNum}${queryString}' />"><button type="button" class="btn btn-secondary"><spring:message code="last"/></button></div>
			</div>
        </div>

    </div>


	</body>
</html>
