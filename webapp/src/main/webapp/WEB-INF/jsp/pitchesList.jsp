<%@	taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@	taglib prefix="form" uri="http://www.springframework.org/tags/form"	%>
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
		<title>Sport Matcher - Pitches</title>
	</head>
	<body>
	<%@include file="header.jsp" %>
	<div class="main-container">
		<%@include file="sidebar.jsp" %>
		<div class="content-container">
			<h2><spring:message code="choosePitch" /></h2>
			<div class="tbl">
				<div class="table-header">
                    <c:url value='/pitches/filter' var="postPath"/>
                    <form:form id="searchfilters" class="searchfilters" modelAttribute="pitchesFiltersForm" action="${postPath}">
                        <div class="table-titles">
                            <div>
                                <form:label path="name"><spring:message code="name" /></form:label>
                                <form:input class="form-control" type="text" path="name"/>
                                <form:errors path="name" cssClass="formError" element="p"/>
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
                                <form:label path="location"><spring:message code="location" /></form:label>
                                <form:input class="form-control" type="text" path="location"/>
                                <form:errors path="location" cssClass="form-error" element="p"/>
                            </div>
                            <div>
                                <form:label path="clubName"><spring:message code="club" /></form:label>
                                <form:input class="form-control" type="text" path="clubName"/>
                                <form:errors path="clubName" cssClass="form-error" element="p"/>
                            </div>
                            <div>
                                <button class="btn btn-primary" type="submit"><spring:message code="filter" /></button>
                            </div>
                        </div>
                    </form:form>

                </div>
                <c:forEach var="pitch" items="${pitches}">
                    <div class="custom-row">
                        <div>${pitch.name}</div>
                        <div><spring:message code="${pitch.sport}"/></div>
                        <div>${pitch.club.location}</div>
                        <div>${pitch.club.name}</div>
                        <div>
                            <a href="<c:url value="/pitch/${pitch.pitchid}"/>"> <button type="button" class="btn btn-primary view-event"><spring:message code="view_pitch"/></button></a>
                        </div>
                    </div>
                </c:forEach>
			</div>
			<div class="table-navigator">
                <c:if test="${pageNum != 1}">
    				<div>
                        <a href="<c:url value='/pitches/1${queryString}' />"><button type="button" class="btn btn-secondary"><spring:message code="first"/></button></a>
                        <a href="<c:url value='/pitches/${page-1}${queryString}' />"><button type="button" class="btn btn-secondary"><spring:message code="back"/></button></a>
                    </div>
                </c:if>
                <c:choose>
                    <c:when test="${pitchQty > 0}">
                        <span><spring:message code="showing_items"/> ${pageInitialIndex}-${pageInitialIndex + pitchQty - 1} <spring:message code="of"/> ${totalPitchQty}</span>
                    </c:when>
                    <c:otherwise>
                        <div class="notice">
                            <spring:message code="no_results"/>
                        </div>
                    </c:otherwise>
                </c:choose>
				
                <c:if test="${pageNum != lastPageNum}">
    				<div>
                        <a href="<c:url value='/pitches/${page+1}${queryString}' />">
                            <button type="button" class="btn btn-secondary">
                                <spring:message code="next"/>
                            </button>
                        </a>
                        <a href="<c:url value='/pitches/${lastPageNum}${queryString}' />">
                            <button type="button" class="btn btn-secondary">
                                <spring:message code="last"/>
                            </button>
                        </a>
                    </div>
                </c:if>
			 </div>
        </div>
    </body>
</html>
