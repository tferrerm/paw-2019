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
            <h2><spring:message code="myEvents" /></h2>
        </div>
        <span class="help-message notice w-70 justify-center"><spring:message code="my_events_help"/></span>
				<c:choose>
					<c:when test="${empty past_events && empty future_events}">
						<div class="notice">
							<spring:message code="no_past_or_future_events"/>
						</div>
					</c:when>
					<c:otherwise>
						<div class="tbl">
							<div class="table-header">
								<div class="flex-grow justify-center my-events-tbl-sub">
									<spring:message code="pastEvents" />
								</div>
								<div class="flex-grow justify-center my-events-tbl-sub">
									<spring:message code="upcomingEvents" />
								</div>
							</div>
							<div class="flex-grow w-100">
								<div class="events-column flex-grow flex-column right-border w-50">
								    <c:forEach var="event" items="${past_events}">
											<a href="<c:url value="/event/${event.eventId}" /> ">
												<div class="my-event-item">
													<span class="flex flex-1 justify-center home-header"><c:out value="${event.name}"/></span>
													<span class="flex flex-1 justify-center home-header"><spring:message code="${event.pitch.sport}"/></span>
													<div class="flex flex-1 justify-center home-header">
														<fmt:timeZone value="AR">
															<fmt:parseDate value="${event.startsAt}" var="parsedDateTime" type="both" pattern="yyyy-MM-dd'T'HH:mm:ss'Z'" />
															<fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }" timeZone="GMT-3" />
														</fmt:timeZone>
													</div>
												</div>
							        </a>
								    </c:forEach>
								</div>
								<div class="events-column flex-grow flex-column w-50">
								    <c:forEach var="event" items="${future_events}">
											<a href="<c:url value="/event/${event.eventId}" /> ">
												<div class="my-event-item">
													<span class="flex flex-1 justify-center home-header"><c:out value="${event.name}"/></span>
													<span class="flex flex-1 justify-center home-header"><spring:message code="${event.pitch.sport}"/></span>
													<div class="flex flex-1 justify-center home-header">
														<fmt:timeZone value="AR">
															<fmt:parseDate value="${event.startsAt}" var="parsedDateTime" type="both" pattern="yyyy-MM-dd'T'HH:mm:ss'Z'" />
															<fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }" timeZone="GMT-3" />
														</fmt:timeZone>
													</div>
												</div>
							        </a>
								    </c:forEach>
								</div>
							</div>
						</div>
						<div class="table-navigator w-100 justify-center">
      				<div class="${page != 1 ? "" : "hidden"}">
                <a href="<c:url value='/my-events/1' />">
                  <button type="button" class="btn btn-secondary">
                      <spring:message code="first"/>
                  </button>
                </a>
                <a href="<c:url value='/my-events/${page-1}' />">
                  <button type="button" class="btn btn-secondary">
                      <spring:message code="back"/>
                  </button>
                </a>
            	</div>
              <span class="flex"><spring:message code="showing_pages"/> <c:out value="${page}"/> <spring:message code="of"/> <c:out value="${lastPageNum}"/></span>
							<div class="${page != lastPageNum ? "" : "hidden"}">
                  <a href="<c:url value='/my-events/${page+1}' />">
                      <button type="button" class="btn btn-secondary"><spring:message code="next"/></button>
                  </a>
                  <a href="<c:url value='/my-events/${lastPageNum}' />">
                      <button type="button" class="btn btn-secondary"><spring:message code="last"/></button>
                  </a>
              </div>
  					</div>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</body>
</html>
