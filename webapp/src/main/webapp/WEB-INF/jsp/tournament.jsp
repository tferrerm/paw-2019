<%@	taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@	taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
            </div>
        </div>
	</body>
</html>
