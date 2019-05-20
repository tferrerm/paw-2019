<%@	taglib	prefix="c"	uri="http://java.sun.com/jstl/core_rt" %>
<%@	taglib	prefix="form"	uri="http://www.springframework.org/tags/form" %>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/style.css' />" >
		<script
				src="http://code.jquery.com/jquery-3.3.1.min.js"
				integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
				crossorigin="anonymous"></script>
		<script type="text/javascript" src="<c:url value='/resources/js/main.js' />"></script>
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
		<link href="https://fonts.googleapis.com/css?family=Barlow+Condensed" rel="stylesheet">
		<link href="https://fonts.googleapis.com/css?family=Archivo+Narrow" rel="stylesheet">
		<title>Sport Matcher - Club</title>
	</head>
	<body>
		<%@ include file="header.jsp" %>
		<div class="main-container">
			<%@include file="sidebar.jsp" %>
			<div class="content-container">
				<h2 class="no-margin">${club.name}</h2>
				<div class="detail-container">
					<div class="description-body">
						<div class="description-item">
							<span class="event-info-label"><spring:message code="pitches"/></span>
							<ul class="pitch-item">
								<c:forEach var="pitch" items="${pitches}">
									<div class="custom-row flex-space-between pitch-item">
										<span class="flex-1">${pitch.name}</span>
										<div class="flex-1"><spring:message code="${pitch.sport}"/></div>
										<form method="POST" class="no-margin" action="<c:url value="/admin/pitch/${pitch.pitchid}/delete"/>" >
											<button type="submit" class="btn btn-primary btn-danger"><spring:message code="delete_pitch"/></button>
										</form>
										<a  > </a>
									</div>
								</c:forEach>
							</ul>
						</div>
						<div class="club-address">
							<span class="event-info-label"><spring:message code="address"/></span>
							<p>${club.location}</p>
						</div>
					</div>
					<div class="status no-margin">
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
											<form:option value="${sport}"><spring:message code="${sport}"/></form:option>
										</c:forEach>
									</form:select>
									<form:errors path="sport" cssClass="form-error" element="span"/>
								</div>
								<div class="choose-file">
									<form:input type="file" accept="image/*" path="pitchPicture" style="display: none" id="pitchPictureButton"/>
									<button type="button" class="btn btn-secondary"  onclick="document.getElementById('pitchPictureButton').click()"><spring:message code="choose_file"/></button>
									<span style="padding-left: 20px; font-size: 16px" id="filenameDisplay"><spring:message code="no_file"/></span>
								</div>
								<div class="submit-container">
									<button type="submit" class="btn btn-primary submit-btn btn-success"><spring:message code="create"/></button>
								</div>
							</form:form>
						</div>
					</div>
				</div>
				<form method="POST" action="<c:url value="/admin/club/${club.clubid}/delete"/>">
					<button type="submit" class="btn btn-danger join-button"><spring:message code="delete_club"/></button>
				</form>
			</div>
		</div>
	</body>
</html>
