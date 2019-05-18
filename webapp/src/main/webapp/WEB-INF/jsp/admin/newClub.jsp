<%@	taglib	prefix="c"	uri="http://java.sun.com/jstl/core_rt"%>
<%@	taglib	prefix="form"	uri="http://www.springframework.org/tags/form"	%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/style.css' />" >
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
		<link href="https://fonts.googleapis.com/css?family=Barlow+Condensed" rel="stylesheet">
		<link href="https://fonts.googleapis.com/css?family=Archivo+Narrow" rel="stylesheet">
		<title>Sport Matcher - New Club</title>
	</head>
	<body>
	<%@include file="header.jsp" %>
	<div class="main-container">
		<%@include file="sidebar.jsp" %>
		<div class="content-container">
			<h2><spring:message code="create_club"/></h2>
			<div class="form-container">
				<c:url value="/admin/club/create" var="postPath"/>
				<form:form modelAttribute="newClubForm" action="${postPath}" method="post" enctype="multipart/form-data">
					<div>
						<form:label path="name"><spring:message code="event_name"/> * </form:label>
						<form:input  cssClass="form-control" type="text" path="name"/>
						<form:errors path="name" cssClass="form-error" element="span"/>
					</div>
					<div>
						<form:label path="location"><spring:message code="event_location"/> * </form:label>
						<form:input  cssClass="form-control" type="text" path="location"/>
						<form:errors path="location" cssClass="form-error" element="span"/>
					</div>
					<div class="submit-container">
						<button type="submit" class="btn btn-primary submit-btn"><spring:message code="create"/></button>
					</div>
				</form:form>
			</div>
		</div>
	</div>


	</body>

</html>
