<%@	taglib	prefix="c"	uri="http://java.sun.com/jstl/core_rt"%>
<%@	taglib	prefix="form"	uri="http://www.springframework.org/tags/form"	%>
<%@ taglib  prefix="spring" uri="http://www.springframework.org/tags"%>
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
		<title>
			<spring:message code="sport_matcher" /> - <spring:message code="register" />
		</title>
	</head>
	<body>
	<div class="main-container">
		<div class="registration-container bgd-field-image">
			<div class="registration-form-container maxw-600">
				<h1><spring:message code="sport_matcher" /></h1>
				<span class="registration-intro-title"><spring:message code="registration_intro_title" /></span>
				<div class="comment-profile-row">
					<span class="registration-intro-text"><spring:message code="registration_intro_text" /></span>
					<img class="registration-image" src="https://findaplayer.com/img/athletes/tennis-player.png" width="200" height="300" />
				</div>
			</div>
			<div class="registration-form-container">
				<div class="profile-title">
	  			<h2 class="color-white"><spring:message code="register"/></h2>
	  		</div>
				<div class="form-container">
					<c:url value="/user/create" var="postPath"/>
					<form:form modelAttribute="signupForm" action="${postPath}"	method="post" enctype="multipart/form-data">
						<div>
							<form:label path="username"><spring:message code="username"/> * </form:label>
							<form:input  cssClass="form-control" type="text" maxlength="100" path="username"/>
							<form:errors path="username" cssClass="form-error" element="span"/>
							<c:if test="${duplicateUsername != null}">
							<div class="form-error">
								<spring:message code="username" />
								<c:out value="${duplicateUsername}" />
								<spring:message code="already_exists" />
							</div>
						</c:if>
						</div>
						<div>
							<form:label path="firstName"><spring:message code="first_name"/> * </form:label>
							<form:input  cssClass="form-control" type="text" maxlength="100" path="firstName"/>
							<form:errors path="firstName" cssClass="form-error" element="span"/>
						</div>
						<div>
							<form:label path="lastName"><spring:message code="last_name"/> *</form:label>
							<form:input  cssClass="form-control" type="text" maxlength="100" path="lastName"/>
							<form:errors path="lastName" cssClass="form-error" element="span"/>
						</div>
						<div>
							<form:label path="password"><spring:message code="password"/> *</form:label>
							<form:input	cssClass="form-control" type="password" maxlength="100" path="password"/>
							<form:errors path="password" cssClass="form-error" element="span"/>
						</div>
						<div>
							<form:label	path="repeatPassword"><spring:message code="repeat_password"/> *</form:label>
							<form:input	cssClass="form-control" type="password" maxlength="100" path="repeatPassword"/>
							<form:errors path="repeatPassword" cssClass="form-error" element="span"/>
						</div>
						<div>
							<form:label path="profilePicture"  style="display: flex; margin-bottom: 5px"><spring:message code="profile_picture"/> </form:label>
							<form:input type="file" accept="image/*" path="profilePicture" style="display: none" id="profilePictureButton"/>
							<button type="button" class="btn btn-secondary"  onclick="document.getElementById('profilePictureButton').click()"><spring:message code="choose_file"/></button>
							<span style="padding-left: 20px; font-size: 16px" id="filenameDisplay"><spring:message code="no_file"/></span>
						</div>


						<c:if test="${fileErrorMessage != null}">
							<div class="form-error">
								<spring:message code="file_error" />
								<c:out value="${fileErrorMessage}" />
							</div>
						</c:if>
						<div class="submit-container">
							<button type="submit" class="btn btn-primary submit-btn"><spring:message code="register"/></button>
						</div>
						<div class="bottom-message">
							<span><spring:message code="already_have_account"/></span>
							<a class="link-text" href="<c:url value='/login' />"><spring:message code="login"/></a>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</div>


	</body>

</html>
