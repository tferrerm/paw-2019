<%@	taglib	prefix="c"	uri="http://java.sun.com/jstl/core_rt"%>
<%@	taglib	prefix="form"	uri="http://www.springframework.org/tags/form"	%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib  prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/style.css' />" >
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
	<link href="https://fonts.googleapis.com/css?family=Barlow+Condensed" rel="stylesheet">
	<link href="https://fonts.googleapis.com/css?family=Archivo+Narrow" rel="stylesheet">
	<title>
		<spring:message code="sport_matcher" /> - <spring:message code="profile" />
	</title>
</head>
<body>
<%@include file="header.jsp" %>
<div class="main-container">
	<%@include file="sidebar.jsp" %>
	<div class="content-container">
		<c:choose>
   			<c:when test="${user.userid == loggedUser.userid}">
   				<div class="profile-title">
              <h2><spring:message code="user.greeting" arguments="${user.firstname} ${user.lastname}"/></h2>
          </div>
			</c:when>
			<c:otherwise>
				<div class="profile-title">
	                <h2><c:out value="${user.firstname}"/> <c:out value="${user.lastname}"/></h2>
	            </div>
			</c:otherwise>
		</c:choose>
		<div class="tbl profile-cont">
			<div class="profile-top">
				<img class="profile-pic" src="<c:url value='/user/${user.userid}/picture'/>"/>
				<div class="stats">
					<div class="notice" style="padding: 5px 0">
						<spring:message code="curr_event_participant"/>
						<span class="notice"> <c:out value="${currEventsParticipant}"/> </span>
						<spring:message code="event_s"/>
					</div>
					<div class="notice" style="padding: 5px 0">
						<spring:message code="curr_events_owned"/>
						<span class="notice"> <c:out value="${currEventsOwned}"/> </span>
						<spring:message code="event_s"/>
					</div>
					<div class="notice" style="padding: 5px 0">
						<spring:message code="past_events_participant"/>
						<span class="notice"> <c:out value="${pastEventsParticipant}"/> </span>
						<spring:message code="event_s"/>
					</div>
					<c:if test="${favoriteSport != null}">
						<div class="notice" style="padding: 5px 0">
							<spring:message code="favorite_sport" />
							<span class="notice"><spring:message code="${favoriteSport}"/></span>
						</div>
					</c:if>
					<c:if test="${mainClub != null}">
						<div class="notice" style="padding: 5px 0">
							<spring:message code="main_club" />
							<span class="notice"> <c:out value="${mainClub.name}"/></span>
						</div>
					</c:if>
					<div class="notice" style="padding: 5px 0">
						<spring:message code="user_vote_balance"/>
						<span class="notice"> <c:out value="${votes_received}"/> </span>
					</div>
				</div>
			</div>
		</div>
		<div class="tbl profile-cont">
			<c:if test="${haveRelationship}">
				<c:url value="/user/${user.userid}/comment" var="postPath"/>
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
				<div class="table-navigator w-100 justify-center">
            <c:choose>
                <c:when test="${commentQty > 0}">
    								<div class="${currCommentPage != 1 ? "" : "hidden"}">
                        <a href="<c:url value='/user/${userid}?cmt=1' />">
                            <button type="button" class="btn btn-secondary">
                                <spring:message code="first"/>
                            </button>
                        </a>
                        <a href="<c:url value='/user/${userid}?cmt=${currCommentPage-1}' />">
                            <button type="button" class="btn btn-secondary">
                                <spring:message code="back"/>
                            </button>
                        </a>
                    </div>
                    <span class="flex"><spring:message code="showing_items"/> <c:out value="${commentsPageInitIndex}"/>-<c:out value="${commentsPageInitIndex + commentQty - 1}"/> <spring:message code="of"/> <c:out value="${totalCommentQty}"/></span>
    								<div class="${currCommentPage != maxCommentPage ? "" : "hidden"}">
                        <a href="<c:url value='/user/${userid}?cmt=${currCommentPage+1}' />">
                            <button type="button" class="btn btn-secondary"><spring:message code="next"/></button>
                        </a>
                        <a href="<c:url value='/user/${userid}?cmt=${maxCommentPage}' />">
                            <button type="button" class="btn btn-secondary"><spring:message code="last"/></button>
                        </a>
                    </div>
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
</div>


</body>
</html>
