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
		<spring:message code="sport_matcher" /> - <spring:message code="history" />
	</title>
</head>
	<body>
	<%@ include file="header.jsp" %>
		<div class="main-container">
			<%@ include file="sidebar.jsp" %>
			<div class="content-container">
				<div class="profile-title">
            <h2><spring:message code="myParticipations" /></h2>
        </div>
        <span class="help-message notice"><spring:message code="history_help"/></span>
				<c:choose>
          <c:when test="${eventQty > 0}">
						<div class="tbl">
							<div class="table-header">
								<div class="flex-grow justify-center my-events-tbl-sub">
									<spring:message code="event_name" />
								</div>
								<div class="flex-grow justify-center my-events-tbl-sub">
									<spring:message code="event_location" />
								</div>
								<div class="flex-grow justify-center my-events-tbl-sub">
									<spring:message code="event_startsAt" />
								</div>
							</div>
							<div class="flex-grow w-100">
								<div class="events-column flex-grow flex-column w-100">
								    <c:forEach var="event" items="${past_participations}">
											<a href="<c:url value="/event/${event.eventId}" /> ">
												<div class="my-event-item">
													<span class="flex flex-1 justify-center home-header"><c:out value="${event.name}"/></span>
													<span class="flex flex-1 justify-center home-header"><c:out value="${event.pitch.club.location}"/></span>
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
	                    <a href="<c:url value='/history/1' />">
	                        <button type="button" class="btn btn-secondary">
	                            <spring:message code="first"/>
	                        </button>
	                    </a>
	                    <a href="<c:url value='/history/${page-1}' />">
	                        <button type="button" class="btn btn-secondary">
	                            <spring:message code="back"/>
	                        </button>
	                    </a>
	                </div>
			        		<span class="flex"><spring:message code="showing_items"/> <c:out value="${pageInitialIndex}"/>-<c:out value="${pageInitialIndex + eventQty - 1}"/> <spring:message code="of"/> <c:out value="${totalEventQty}"/></span>
									<div class="${page != lastPageNum ? "" : "hidden"}">
	                    <a href="<c:url value='/history/${page+1}' />">
	                        <button type="button" class="btn btn-secondary"><spring:message code="next"/></button>
	                    </a>
	                    <a href="<c:url value='/history/${lastPageNum}' />">
	                        <button type="button" class="btn btn-secondary"><spring:message code="last"/></button>
	                    </a>
	                </div>
	            	</div>
           	</c:when>
              <c:otherwise>
                  <div class="notice">
                      <spring:message code="no_results"/>
                  </div>
              </c:otherwise>
          </c:choose>
				</div>
			</div>
	</body>
</html>
