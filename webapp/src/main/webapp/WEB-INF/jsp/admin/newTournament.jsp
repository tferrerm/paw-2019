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
			<div class="form-container">
				<c:url value="/admin/tournament/create" var="postPath"/>
				<form:form modelAttribute="newTournamentForm" action="${postPath}" method="post" enctype="multipart/form-data">
					<div>
						<form:label path="name"><spring:message code="name"/> * </form:label>
						<form:input  cssClass="form-control" type="text" maxlength="100" path="name"/>
						<form:errors path="name" cssClass="form-error" element="span"/>
					</div>
					<div>
						<form:label path="club"><spring:message code="club"/> * </form:label>
						<form:input  cssClass="form-control" type="text" maxlength="100" path="club"/>
						<form:errors path="club" cssClass="form-error" element="span"/>
					</div>
					<div>
						<form:label path="sport"><spring:message code="sport"/> * </form:label>
						<form:input  cssClass="form-control" type="text" maxlength="100" path="sport"/>
						<form:errors path="sport" cssClass="form-error" element="span"/>
					</div>
					<div>
						<form:label path="minParticipants"><spring:message code="min_participants"/> * </form:label>
						<form:input  cssClass="form-control" type="number" maxlength="100" path="minParticipants"/>
						<form:errors path="minParticipants" cssClass="form-error" element="span"/>
					</div>
					<div>
						<form:label path="maxParticipants"><spring:message code="max_participants"/> * </form:label>
						<form:input  cssClass="form-control" type="number" maxlength="100" path="maxParticipants"/>
						<form:errors path="maxParticipants" cssClass="form-error" element="span"/>
					</div>
					<div>
						<form:label path="teamSize"><spring:message code="team_size"/> * </form:label>
						<form:input  cssClass="form-control" type="number" maxlength="100" path="teamSize"/>
						<form:errors path="teamSize" cssClass="form-error" element="span"/>
					</div>
					<div class="submit-container">
						<button type="submit" class="btn btn-primary submit-btn btn-success"><spring:message code="create"/></button>
					</div>
				</form:form>
			</div>
		</div>
	</div>


	</body>

</html>
