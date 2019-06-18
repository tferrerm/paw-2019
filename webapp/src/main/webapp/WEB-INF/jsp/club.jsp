<%@	taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@	taglib prefix="form" uri="http://www.springframework.org/tags/form"	%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib  prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/style.css'/>"/>
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous"/>
		<link href="https://fonts.googleapis.com/css?family=Barlow+Condensed" rel="stylesheet"/>
		<link href="https://fonts.googleapis.com/css?family=Archivo+Narrow" rel="stylesheet"/>
		<title><spring:message code="sport_matcher" /> - <spring:message code="club" /></title>
	</head>
	<body>
		<%@ include file="header.jsp" %>
		<div class="main-container">
			<%@ include file="sidebar.jsp" %>
			<div class="content-container">
				<div class="profile-title">
					<h2><c:out value="${club.name}"/></h2>
				</div>
				<div class="club-detail-container">
					<div class="description-body">
						<div class="description-item">
							<div class="flex">
								<div class="h4 pitch-info-label">
									<spring:message code="club_location"/>
									<span class="h4"> <c:out value="${club.location}"/></span>
								</div>
							</div>
							<h4><spring:message code="hosted"/> <c:out value="${past_events_count}"/> <spring:message code="event_s"/></h4>
						</div>
					</div>
					<div class="description-item">
						<span class="h4 pitch-info-label"><spring:message code="pitches"/></span>
						<c:choose>
							<c:when test="${empty pitches}">
								<span><spring:message code="no_pitches"/></span>
							</c:when>
							<c:otherwise>
								<ul>
									<c:forEach var="pitch" items="${pitches}">
										<div class="custom-row flex-space-around club-pitches-list">
											<div><c:out value="${pitch.name}" /></div>
											<div><spring:message code="${pitch.sport}"/></div>
											<div>
												<a href="<c:url value="/pitch/${pitch.pitchid}" /> "> <button type="button" class="btn btn-primary view-club"><spring:message code="view_pitch"/></button></a>
											</div>
										</div>
									</c:forEach>
								</ul>
							</c:otherwise>
						</c:choose>
					</div>
				</div>

				<div class="tbl profile-cont">
					<c:if test="${haveRelationship}">
						<c:url value="/club/${club.clubid}/comment" var="postPath"/>
						<form:form id="commentForm" modelAttribute="commentForm" action="${postPath}" class="comments_form">
							<form:label path="comment"><spring:message code="comment"/></form:label>
							<form:input class="form-control" type="text" path="comment" maxlength="500"/>
							<form:errors path="comment" cssClass="form-error" element="span"/>
							<div class="submit-container">
								<button type="submit" class="btn btn-primary submit-btn btn-primary"><spring:message code="comment_action"/></button>
							</div>
						</form:form>
					</c:if>
					<c:forEach var="cmt" items="${comments}">
						<div class="comment-container">
							<div class="comment-profile-row mv-10">
								<img class="comment-image" src="<c:url value='/user/${cmt.commenter.userid}/picture'/>" width="40" height="40"/>
								<a class="link-text ml-10" href="<c:url value="/user/${cmt.commenter.userid}" /> "><c:out value="${cmt.commenter.firstname}"/> <c:out value="${cmt.commenter.lastname}"/></a>
							</div>
							<span class="comment-text ml-10"><c:out value="${cmt.comment}"/></span>
							<div class="comment-date ml-10 mt-10">
								<fmt:timeZone value="AR">
									<fmt:parseDate value="${cmt.createdAt}" var="parsedDateTime" type="both" pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" />
									<fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }" timeZone="GMT-3" />
								</fmt:timeZone>
							</div>
						</div>
          </c:forEach>
					<div class="table-navigator">
            <c:choose>
              <c:when test="${commentQty > 0}">
                <c:if test="${currCommentPage != 1}">
          				<div>
                      <a href="<c:url value='/club/${clubid}?cmt=1' />">
                          <button type="button" class="btn btn-secondary">
                              <spring:message code="first"/>
                          </button>
                      </a>
                      <a href="<c:url value='/club/${clubid}?cmt=${currCommentPage-1}' />">
                          <button type="button" class="btn btn-secondary">
                              <spring:message code="back"/>
                          </button>
                      </a>
                  </div>
                </c:if>
                <span class="flex"><spring:message code="showing_items"/> <c:out value="${commentsPageInitIndex}"/>-<c:out value="${commentsPageInitIndex + commentQty - 1}"/> <spring:message code="of"/> <c:out value="${totalCommentQty}"/></span>
                <c:if test="${currCommentPage != maxCommentPage}">
          				<div>
                      <a href="<c:url value='/club/${clubid}?cmt=${currCommentPage+1}' />">
                          <button type="button" class="btn btn-secondary"><spring:message code="next"/></button>
                      </a>
                      <a href="<c:url value='/club/${clubid}?cmt=${maxCommentPage}' />">
                          <button type="button" class="btn btn-secondary"><spring:message code="last"/></button>
                      </a>
                  </div>
                </c:if>
              </c:when>
              <c:otherwise>
                  <div class="notice">
                      <spring:message code="no_comments"/>
                  </div>
              </c:otherwise>
          </c:choose>
  			</div>
			</div>
		</div>
	</div>
</body>
</html>
