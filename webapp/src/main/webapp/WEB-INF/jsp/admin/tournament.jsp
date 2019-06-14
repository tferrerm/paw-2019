<%@	taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@	taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib  prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/style.css' />" >
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
		<link href="https://fonts.googleapis.com/css?family=Barlow+Condensed" rel="stylesheet">
		<link href="https://fonts.googleapis.com/css?family=Archivo+Narrow" rel="stylesheet">
		<title><spring:message code="sport_matcher" /> - <spring:message code="event" /></title>
	</head>
	<body>
	<%@ include file="header.jsp" %>
	<div class="main-container">
		<%@include file="sidebar.jsp" %>
		<div class="content-container">
			<div class="profile-title">
				<h2>${tournament.name}</h2>
			</div>
			<div class="detail-container">
				<div class="w-100 flex flex-grow justify-center">
					<h3><spring:message code="teams"/></h3>
				</div>
				<div class="flex-column w-100">
					<div class="flex">
						<div class="team-description">
							<span class="event-info-label">Team 1 </span>
							<div class="flex w-100">
								<div class="flex flex-column align-center w-50">
									<span>Juan Perez</span>
									<span>Pedro Gomez</span>
									<span>Santiago Swinnen</span>
								</div>
								<div class="flex flex-column align-center w-50">
									<span>Marcos Lund</span>
									<span>Tomas Ferrer</span>
									<span>Guido Princ</span>
								</div>
							</div>
							<div class="flex flex-grow justify-center">
								<a href="<c:url value="tournament/${tournamentId}/team/1/join"/>"> <button type="button" class="btn btn-primary view-event"><spring:message code="join_team"/></button></a>
							</div>
						</div>
						<div class="team-description">
							<span class="event-info-label">Team 1 </span>
							<div class="flex w-100">
								<div class="flex flex-column align-center w-50">
									<span>Juan Perez</span>
									<span>Pedro Gomez</span>
									<span>Santiago Swinnen</span>
								</div>
								<div class="flex flex-column align-center w-50">
									<span>Marcos Lund</span>
									<span>Tomas Ferrer</span>
									<span>Guido Princ</span>
								</div>
							</div>
							<div class="flex flex-grow justify-center">
								<a href="<c:url value="tournament/${tournamentId}/team/1/join"/>"> <button type="button" class="btn btn-primary view-event"><spring:message code="join_team"/></button></a>
							</div>
						</div>
					</div>
					<div  class="flex flex-grow">
						<div class="team-description">
							<span class="event-info-label">Team 1 </span>
							<div class="flex w-100">
								<div class="flex flex-column align-center w-50">
									<span>Juan Perez</span>
									<span>Pedro Gomez</span>
									<span>Santiago Swinnen</span>
								</div>
								<div class="flex flex-column align-center w-50">
									<span>Marcos Lund</span>
									<span>Tomas Ferrer</span>
									<span>Guido Princ</span>
								</div>
							</div>
							<div class="flex flex-grow justify-center">
								<a href="<c:url value="tournament/${tournamentId}/team/1/join"/>"> <button type="button" class="btn btn-primary view-event"><spring:message code="join_team"/></button></a>
							</div>
						</div>
						<div class="team-description">
							<span class="event-info-label">Team 1 </span>
							<div class="flex w-100">
								<div class="flex flex-column align-center w-50">
									<span>Juan Perez</span>
									<span>Pedro Gomez</span>
									<span>Santiago Swinnen</span>
								</div>
								<div class="flex flex-column align-center w-50">
									<span>Marcos Lund</span>
									<span>Tomas Ferrer</span>
									<span>Guido Princ</span>
								</div>
							</div>
							<div class="flex flex-grow justify-center">
								<a href="<c:url value="tournament/${tournamentId}/team/1/join"/>"> <button type="button" class="btn btn-primary view-event"><spring:message code="join_team"/></button></a>
							</div>
						</div>
					</div>
					<div  class="flex flex-grow">
						<div class="team-description">
							<span class="event-info-label">Team 1 </span>
							<div class="flex w-100">
								<div class="flex flex-column align-center w-50">
									<span>Juan Perez</span>
									<span>Pedro Gomez</span>
									<span>Santiago Swinnen</span>
								</div>
								<div class="flex flex-column align-center w-50">
									<span>Marcos Lund</span>
									<span>Tomas Ferrer</span>
									<span>Guido Princ</span>
								</div>
							</div>
							<div class="flex flex-grow justify-center">
								<a href="<c:url value="tournament/${tournamentId}/team/1/join"/>"> <button type="button" class="btn btn-primary view-event"><spring:message code="join_team"/></button></a>
							</div>
						</div>
						<div class="team-description">
							<span class="event-info-label">Team 1 </span>
							<div class="flex w-100">
								<div class="flex flex-column align-center w-50">
									<span>Juan Perez</span>
									<span>Pedro Gomez</span>
									<span>Santiago Swinnen</span>
								</div>
								<div class="flex flex-column align-center w-50">
									<span>Marcos Lund</span>
									<span>Tomas Ferrer</span>
									<span>Guido Princ</span>
								</div>
							</div>
							<div class="flex flex-grow justify-center">
								<a href="<c:url value="tournament/${tournamentId}/team/1/join"/>"> <button type="button" class="btn btn-primary view-event"><spring:message code="join_team"/></button></a>
							</div>
						</div>
					</div>
					<div  class="flex flex-grow">
						<div class="team-description">
							<span class="event-info-label">Team 1 </span>
							<div class="flex w-100">
								<div class="flex flex-column align-center w-50">
									<span>Juan Perez</span>
									<span>Pedro Gomez</span>
									<span>Santiago Swinnen</span>
								</div>
								<div class="flex flex-column align-center w-50">
									<span>Marcos Lund</span>
									<span>Tomas Ferrer</span>
									<span>Guido Princ</span>
								</div>
							</div>
							<div class="flex flex-grow justify-center">
								<a href="<c:url value="tournament/${tournamentId}/team/1/join"/>"> <button type="button" class="btn btn-primary view-event"><spring:message code="join_team"/></button></a>
							</div>
						</div>
						<div class="team-description">
							<span class="event-info-label">Team 1 </span>
							<div class="flex w-100">
								<div class="flex flex-column align-center w-50">
									<span>Juan Perez</span>
									<span>Pedro Gomez</span>
									<span>Santiago Swinnen</span>
								</div>
								<div class="flex flex-column align-center w-50">
									<span>Marcos Lund</span>
									<span>Tomas Ferrer</span>
									<span>Guido Princ</span>
								</div>
							</div>
							<div class="flex flex-grow justify-center">
								<a href="<c:url value="tournament/${tournamentId}/team/1/join"/>"> <button type="button" class="btn btn-primary view-event"><spring:message code="join_team"/></button></a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>


	</body>
</html>
