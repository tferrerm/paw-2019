<%@	taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@	taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib  prefix="spring" uri="http://www.springframework.org/tags"%>
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
		<title>
			<spring:message code="sport_matcher" /> - <spring:message code="tournaments" />
		</title>
	</head>

	<body>
    	<%@ include file="header.jsp" %>
    	<div class="main-container">
    		<%@ include file="sidebar.jsp" %>
    		<div class="content-container">
                <div class="profile-title">
                    <h2><spring:message code="choose_tournament"/></h2>
                </div>
                <span class="help-message notice"><spring:message code="tournament_list_help"/></span>
    						<div class="tbl">
    							<div class="background-dodgerblue table-header">
                      <span class="justify-center flex-1 color-white mt-10"><spring:message code="event_name" /></span>
                      <span class="justify-center flex-1 color-white mt-10"><spring:message code="club" /></span>
                      <span class="justify-center flex-1 color-white mt-10"><spring:message code="sport" /></span>
											<span class="justify-center flex-1" />
                  </div>
                  <c:forEach var="tournament" items="${tournaments}">
                      <div class="custom-row">
                          <div>${tournament.name}</div>
                          <div>${tournament.tournamentClub.name}</div>
                          <div><spring:message code="${tournament.sport}"/></div>
                          <div>
                              <a href="<c:url value="/tournament/${tournament.tournamentid}"/>"> <button type="button" class="btn btn-primary view-event"><spring:message code="view_event"/></button></a>
                          </div>
                      </div>
                  </c:forEach>
    					</div>
                <div class="table-navigator">
                    <c:choose>
                        <c:when test="${tournamentQty > 0}">
                            <c:if test="${page != 1}">
                                <div>
                                    <a href="<c:url value='/tournaments/1' />">
                                        <button type="button" class="btn btn-secondary">
                                            <spring:message code="first"/>
                                        </button>
                                    </a>
                                    <a href="<c:url value='/tournaments/${page-1}' />">
                                        <button type="button" class="btn btn-secondary">
                                            <spring:message code="back"/>
                                        </button>
                                    </a>
                                </div>
                            </c:if>
                            <span class="flex"><spring:message code="showing_items"/> ${pageInitialIndex}-${pageInitialIndex + tournamentQty - 1} <spring:message code="of"/> ${totalTournamentQty}</span>
                            <c:if test="${page != lastPageNum}">
                                <div>
                                    <a href="<c:url value='/tournaments/${page+1}' />">
                                        <button type="button" class="btn btn-secondary"><spring:message code="next"/></button>
                                    </a>
                                    <a href="<c:url value='/tournaments/${lastPageNum}' />">
                                        <button type="button" class="btn btn-secondary"><spring:message code="last"/></button>
                                    </a>
                                </div>
                            </c:if>
                        </c:when>
                        <c:otherwise>
                            <div class="notice">
                                <spring:message code="no_results"/>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
	</body>
</html>
