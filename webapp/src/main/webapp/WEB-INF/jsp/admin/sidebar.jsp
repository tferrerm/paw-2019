<%@ taglib  prefix="c"  uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib  prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <link href="https://fonts.googleapis.com/css?family=Barlow+Condensed" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Archivo+Narrow" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/style.css' />" >
    <title><spring:message code="sport_matcher" /></title>
</head>
<body>
    <div class="sidebar">
        <ul>
            <li><a href="<c:url value='/admin/' />"><spring:message code="home"/></a></li>
            <li><a href="<c:url value='/admin/events/1' />"><spring:message code="allEvents"/></a></li>
            <li><a href="<c:url value='/admin/club/new' />"><spring:message code="create_club"/></a></li>
            <li><a href="<c:url value='/admin/clubs/1' />"><spring:message code="all_clubs"/></a></li>
        </ul>
    </div>
</body>
</html>
