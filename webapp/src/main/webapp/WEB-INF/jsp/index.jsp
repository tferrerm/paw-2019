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
		<div class="content-container">
			<h2><spring:message code="register"/></h2>
			<div class="form-container">
				<c:url value="/user/create" var="postPath"/>
				<form:form modelAttribute="signupForm" action="${postPath}"	method="post">
					<div>
						<form:label path="username"><spring:message code="username"/> </form:label>
						<form:input  cssClass="form-control" type="text" path="username"/>
						<form:errors path="username" cssClass="form-error" element="span"/>
					</div>
					<div>
						<form:label path="password"><spring:message code="password"/> </form:label>
						<form:input	cssClass="form-control" type="password"	path="password"/>
						<form:errors path="password" cssClass="form-error" element="span"/>
					</div>
					<div>
						<form:label	path="repeatPassword"><spring:message code="repeat_password"/> </form:label>
						<form:input	cssClass="form-control" type="password"	path="repeatPassword"/>
						<form:errors path="repeatPassword" cssClass="form-error" element="span"/>
					</div>
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


	</body>
</html>
