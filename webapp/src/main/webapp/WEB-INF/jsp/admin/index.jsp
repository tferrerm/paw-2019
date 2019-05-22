<%@	taglib	prefix="c"	uri="http://java.sun.com/jstl/core_rt"%>
<%@	taglib	prefix="form"	uri="http://www.springframework.org/tags/form"	%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib  prefix="spring" uri="http://www.springframework.org/tags"%>
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
		<div class="profile-title">
			<h2><spring:message code="allEvents" /></h2>
		</div>
		<div class="tbl">
			<div class="table-header">
				<c:url value='/admin/events/filter' var="postPath"/>
				<form:form id="searchfilters" class="searchfilters" modelAttribute="filtersForm" action="${postPath}">
					<div class="table-titles">
						<div>
							<form:label path="establishment"><spring:message code="club" /></form:label>
							<form:input class="form-control" type="text" path="establishment"/>
							<form:errors path="establishment" cssClass="formError" element="p"/>
						</div>
						<div>
							<form:label path="sport"><spring:message code="sport" /></form:label>
							<form:select path="sport" cssClass="form-control">
                                <form:option  value=""></form:option>
                                <c:forEach var="sport" items="${sports}">
                                    <form:option value="${sport}"><spring:message code="${sport}"/></form:option>
                                </c:forEach>
                            </form:select>
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
							<c:if test="${invalid_number_format == true}">
                <span class="form-error">
                  <spring:message code="invalid_number_format"/>
                </span>
              </c:if>
						</div>
						<div>
							<form:label path="date"><spring:message code="date" /></form:label>
							<form:input class="form-control" type="date" path="date"/>
							<form:errors path="date" cssClass="form-error" element="p"/>
							<c:if test="${invalid_date_format == true}">
                <span class="form-error">
                  <spring:message code="invalid_date_format"/>
                </span>
              </c:if>
						</div>
						<div>
							<button class="btn btn-primary" type="submit"><spring:message code="filter" /></button>
						</div>
					</div>
				</form:form>

			</div>
			<c:forEach var="event" items="${events}">
				<div class="custom-row">
					<div>${event.pitch.club.name}</div>
					<div><spring:message code="${event.pitch.sport}"/></div>
					<div>${event.owner.firstname} ${event.owner.lastname}</div>
					<div>${event.maxParticipants - event.inscriptions}</div>
					<div>
						<fmt:timeZone value="AR">
							<fmt:parseDate value="${event.startsAt}" var="parsedDateTime" type="both" pattern="yyyy-MM-dd'T'HH:mm:ss'Z'" />
							<fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }" timeZone="GMT-3" />
						</fmt:timeZone>
					</div>
					<div>
						<a href="<c:url value="/admin/event/${event.eventId}"/>"> <button type="button" class="btn btn-primary view-event"><spring:message code="view_event"/></button></a>
					</div>
				</div>
			</c:forEach>
		</div>
		<div class="table-navigator">
			<c:choose>
                <c:when test="${eventQty > 0}">
		            <c:if test="${page != 1}">
						<div>
		                    <a href="<c:url value='/admin/events/1${queryString}' />">
		                        <button type="button" class="btn btn-secondary">
		                            <spring:message code="first"/>
		                        </button>
		                    </a>
		                    <a href="<c:url value='/admin/events/${page-1}${queryString}' />">
		                        <button type="button" class="btn btn-secondary">
		                            <spring:message code="back"/>
		                        </button>
		                    </a>
		                </div>
		            </c:if>
         			<span class="flex"><spring:message code="showing_items"/> ${pageInitialIndex}-${pageInitialIndex + eventQty - 1} <spring:message code="of"/> ${totalEventQty}</span>
		            <c:if test="${page != lastPageNum}">
						<div>
		                    <a href="<c:url value='/admin/events/${page+1}${queryString}' />">
		                        <button type="button" class="btn btn-secondary"><spring:message code="next"/></button>
		                    </a>
		                    <a href="<c:url value='/admin/events/${lastPageNum}${queryString}' />">
		                        <button type="button" class="btn btn-secondary"><spring:message code="last"/></button>
		                    </a>
		                </div>
		            </c:if>
		        </c:when>
                <c:otherwise>
                    <div class="notice">
                        <spring:message code="no_results"/>
                    </div>
                </c:otherwise>
            </c:choose>
		</div>
	</div>

</div>


</body>
</html>
