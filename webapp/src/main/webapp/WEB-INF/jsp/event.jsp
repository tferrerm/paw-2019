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
			<h2>Event name</h2>
			<div class="detail-container">
				<div class="status">
					<h3><spring:message code="status"/> Uncompleted</h3>
					<div class="progress">
						<div class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar" style="width: 10%" aria-valuenow="10" aria-valuemin="0" aria-valuemax="100"></div>
					</div>
					<h4 class="progress-bar-completion">3/11</h4>
				</div>
				<div class="description-body">
					<div>
						<div class="description-item">
							<span class="event-info-label"><spring:message code="organizer"/> </span>
							<span>Jorgito</span>
						</div>
						<div class="description-item">
							<span class="event-info-label"><spring:message code="sport"/></span>
							<span>Soccer</span>
						</div>
						<div class="description-item">
							<span class="event-info-label"><spring:message code="establishment"/></span>
							<span>Club Atletico San Lorenzo de Almagro</span>
						</div>
						<div class="double-box">
							<div class="description-item">
								<span class="event-info-label"><spring:message code="date"/></span>
								<span>April 29, 2019 8:00 pm</span>
							</div>
							<div class="description-item">
								<span class="event-info-label"><spring:message code="vacancies"/></span>
								<span>9</span>
							</div>

						</div>
					</div>
					<div class="participants-list">
						<span class="event-info-label"><spring:message code="participants"/></span>
						<ul>
							<li>Santiago Swinnen</li>
							<li>Guido Princ</li>
							<li>Marcos Lund</li>
							<li>Tomas Ferrer</li>
							<li>Jorgito Trapito</li>
							<li>Santiago Swinnen</li>
							<li>Guido Princ</li>
							<li>Marcos Lund</li>
							<li>Tomas Ferrer</li>
						</ul>
					</div>
				</div>
			</div>
			<button type="button" class="btn btn-success join-button"><spring:message code="join"/></button>
		</div>
	</div>


	</body>
</html>
