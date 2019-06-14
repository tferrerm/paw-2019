<%@	taglib	prefix="c"	uri="http://java.sun.com/jstl/core_rt" %>
<%@	taglib	prefix="form"	uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib  prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/style.css' />" >
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
		<link href="https://fonts.googleapis.com/css?family=Barlow+Condensed" rel="stylesheet">
		<link href="https://fonts.googleapis.com/css?family=Archivo+Narrow" rel="stylesheet">
		<title>
			<spring:message code="sport_matcher" /> - <spring:message code="myEvents" />
		</title>
	</head>
	<body>
		<%@ include file="header.jsp" %>
		<div class="main-container">
			<%@ include file="sidebar.jsp" %>
			<div class="content-container">
				<div class="profile-title">
                    <h2>Event</h2>
                </div>
                <span class="help-message notice"><spring:message code="event_completed_description"/></span>
				<div class="detail-box">
					<div class="event-detail">
						<div><spring:message code="event_detail"/></div>
					</div>
					<div>
						<div class="detail-box-data">Dato 1</div>
						<div class="detail-box-data">Dato 2</div>
						<div class="detail-box-data">Dato 3</div>
					</div>
				</div>
				<div class="tbl">
							<div class="table-header">
								<div class="flex-grow justify-center my-events-tbl-sub">
									Equipo 1
								</div>
								<div class="flex-grow justify-center my-events-tbl-sub">
									Equipo 2
								</div>
							</div>
							<div class="flex-grow w-100">
								<div class=" flex-grow flex-column right-border w-50">
									<div class="score">
										3
									</div>
									<div class="team-description">
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
								</div>
								<div class="flex-grow flex-column w-50">
								    <div class="score">
										0
									</div>
									<div class="team-description">
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
								</div>
							</div>
						</div>
			</div>
		</div>
	</body>
</html>
