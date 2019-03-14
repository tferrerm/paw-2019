<%@ taglib  prefix="c"  uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib  prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <link href="https://fonts.googleapis.com/css?family=Barlow+Condensed" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Archivo+Narrow" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/style.css' />" >
    <title>Title</title>
</head>
<body>
    <div class="header">
        <h1 class="title">Company Name</h1>
        <div>
            <c:choose>
                <c:when test = "${loggedUser != null}">
                    <span class="username"><c:out value="${loggedUser.username}"/></span>
                    <span class="headerLoginSpan">
						<a href="<c:url value='/logout' />"><spring:message code="logout"/></a>
					</span>
                </c:when>
                <c:otherwise>
                    <a href="<c:url value='/login' />"><spring:message code="login"/></a>
                    <spring:message code="or"/>
                    <a href="<c:url value='/' />"><spring:message code="register"/></a>
                </c:otherwise>
            </c:choose>

        </div>
    </div>
</body>
</html>
