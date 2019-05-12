<%@	taglib	prefix="c"	uri="http://java.sun.com/jstl/core_rt"%>
<%@	taglib	prefix="form"	uri="http://www.springframework.org/tags/form"	%>
<html>
	<head>
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
			<h2>${club.name}</h2>
			<div class="detail-container">
				<div class="description-body">
					<div class="description-item">
						<span class="event-info-label"><spring:message code="pitches"/></span>
						<ul>
							<c:forEach var="pitch" items="${pitches}">
								<div class="custom-row">
									<div>${pitch.name}</div>
									<div>${pitch.sport}</div>
									<div>
										<a href="<c:url value="/admin/club/${club.clubid}"/>"> <button type="button" class="btn btn-primary view-club"><spring:message code="view_club"/></button></a>
									</div>
								</div>
							</c:forEach>
						</ul>
					</div>
					<div class="participants-list">
						<span class="event-info-label"><spring:message code="address"/></span>
						<p>${club.location}</p>
					</div>
				</div>
				<div class="status">
					<div class="pitch-form-container">
						<h3><spring:message code="create_pitch"/></h3>
						<c:url value="/admin/club/${club.clubid}/pitch/create" var="postPath"/>
						<form:form modelAttribute="newPitchForm" action="${postPath}" method="post" enctype="multipart/form-data">
							<div>
								<form:label path="name"><spring:message code="event_name"/> * </form:label>
								<form:input  cssClass="form-control" type="text" path="name"/>
								<form:errors path="name" cssClass="form-error" element="span"/>
							</div>
							<div style="margin: 5px 0">
								<form:label path="sport"><spring:message code="sport"/> * </form:label>
								<form:select path="sport" cssClass="form-control">
									<c:forEach var="sport" items="${sports}">
										<form:option value="${sport}"/>
									</c:forEach>
								</form:select>
								<form:errors path="sport" cssClass="form-error" element="span"/>
							</div>
							<div class="submit-container">
								<button type="submit" class="btn btn-primary submit-btn"><spring:message code="create"/></button>
							</div>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>


	</body>
</html>
