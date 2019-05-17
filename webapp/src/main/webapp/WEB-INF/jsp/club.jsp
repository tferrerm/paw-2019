<%@	taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@	taglib prefix="form" uri="http://www.springframework.org/tags/form"	%>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/style.css'/>"/>
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous"/>
		<link href="https://fonts.googleapis.com/css?family=Barlow+Condensed" rel="stylesheet"/>
		<link href="https://fonts.googleapis.com/css?family=Archivo+Narrow" rel="stylesheet"/>
	</head>
	<body>
		<%@ include file="header.jsp" %>
		<div class="main-container">
			<%@ include file="sidebar.jsp" %>
			<div class="content-container">
				<h2>${club.name}</h2>
				<div class="club-detail-container">
					<div class="description-item">
						<span class="event-info-label"><spring:message code="location"/></span>
						<p>${club.location}</p>
					</div>
					<div class="description-item">
						<span class="event-info-label"><spring:message code="pitches"/></span>
						<c:choose>
							<c:when test="${empty pitches}">
								<span><spring:message code="no_pitches"/></span>
							</c:when>
							<c:otherwise>
								<ul>
									<c:forEach var="pitch" items="${pitches}">
										<div class="custom-row flex-space-around club-pitches-list">
											<a href="<c:url value="/pitch/${pitch.pitchid}" /> ">${pitch.name}</a>
											<div><spring:message code="${pitch.sport}"/></div>
											<div>
												<a href="<c:url value="/club/${club.clubid}"/>"> <button type="button" class="btn btn-primary view-club"><spring:message code="view_club"/></button></a>
											</div>
										</div>
									</c:forEach>
								</ul>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>
