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
		<h2><spring:message code="all_clubs" /></h2>
		<div class="tbl">
			</div>
			<c:forEach var="club" items="${clubs}">
				<div class="custom-row">
					<div>${club.name}</div>
					<div>Hardcoded</div>
					<div>${club.location}</div>
					<div>
						<a href="<c:url value="/admin/club/${club.clubId}"/>"> <button type="button" class="btn btn-primary view-club"><spring:message code="view_club"/></button></a>
					</div>
				</div>
			</c:forEach>
		</div>
		<div class="table-navigator">
			<div>
				<a href="<c:url value='/admin/clubs/1' />"><button type="button" class="btn btn-secondary"><spring:message code="first"/></button></a>
				<a href="<c:url value='/admin/clubs/${page-1}' />"><button type="button" class="btn btn-secondary"><spring:message code="back"/></button></a>
			</div>
			<span><spring:message code="showing_items"/> 0-5 <spring:message code="of"/> 5</span>
			<div>
				<a href="<c:url value='/admin/clubs/${page+1}' />"><button type="button" class="btn btn-secondary"><spring:message code="next"/></button></a>
				<a href="<c:url value='/admin/clubs/${lastPageNum}' />"><button type="button" class="btn btn-secondary"><spring:message code="last"/></button></div>
		</div>
	</div>

</div>


</body>
</html>
