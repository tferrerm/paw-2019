<%@	taglib	prefix="c"	uri="http://java.sun.com/jstl/core_rt"%>
<%@	taglib	prefix="form"	uri="http://www.springframework.org/tags/form"	%>
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
		<title>
			<spring:message code="sport_matcher" /> - <spring:message code="clubs" />
		</title>
	</head>

	<body>
		<%@ include file="header.jsp" %>
		<div class="main-container">
			<%@ include file="sidebar.jsp" %>
			<div class="content-container">
				<h2><spring:message code="all_clubs" /></h2>
				<div class="tbl club-table-size">
					<div class="table-header">
		                <c:url value='/admin/clubs/filter' var="postPath"/>
		                <form:form id="searchfilters" class="searchfilters" modelAttribute="clubsFiltersForm" action="${postPath}">
		                    <div class="table-titles flex-space-around">
		                        <div>
		                            <form:label path="name"><spring:message code="name" /></form:label>
		                            <form:input class="form-control" type="text" path="name"/>
		                            <form:errors path="name" cssClass="form-error" element="p"/>
		                        </div>
		                        <div>
		                            <form:label path="location"><spring:message code="location" /></form:label>
		                            <form:input class="form-control" type="text" path="location"/>
		                            <form:errors path="location" cssClass="formError" element="p"/>
		                        </div>
		                        <div>
		                            <button class="btn btn-primary" type="submit"><spring:message code="filter" /></button>
		                        </div>
		                    </div>
		                </form:form>
		            </div>
					<c:forEach var="club" items="${clubs}">
						<div class="custom-row flex-space-around">
							<div>${club.name}</div>
							<div>${club.location}</div>
							<div>
								<a href="<c:url value="/admin/club/${club.clubid}"/>"> <button type="button" class="btn btn-primary view-club"><spring:message code="view_club"/></button></a>
							</div>
						</div>
					</c:forEach>
				</div>
				<div class="table-navigator">
					<c:choose>
						<c:when test="${clubQty > 0}">
							<c:if test="${pageNum != 1}">
								<div>
									<a href="<c:url value='/clubs/1' />"><button type="button" class="btn btn-secondary"><spring:message code="first"/></button></a>
									<a href="<c:url value='/clubs/${page-1}' />"><button type="button" class="btn btn-secondary"><spring:message code="back"/></button></a>
								</div>
							</c:if>
							<span><spring:message code="showing_items"/> ${pageInitialIndex}-${pageInitialIndex + clubQty - 1} <spring:message code="of"/> ${totalClubQty}</span>
		                    <c:if test="${pageNum != lastPageNum}">
								<div>
									<a href="<c:url value='/clubs/${page+1}' />"><button type="button" class="btn btn-secondary"><spring:message code="next"/></button></a>
									<a href="<c:url value='/clubs/${lastPageNum}' />"><button type="button" class="btn btn-secondary"><spring:message code="last"/></button></a>
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
