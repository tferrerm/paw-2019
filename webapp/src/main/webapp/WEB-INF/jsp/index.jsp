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
	<h2>Register</h2>
	<div class="form-container">
		<c:url value="/user/create" var="postPath"/>
		<form:form modelAttribute="signupForm" action="${postPath}"	method="post">
			<div>
				<form:label path="username">Username: </form:label>
				<form:input  cssClass="form-control" type="text" path="username"/>
				<form:errors path="username" cssClass="formError" element="p"/>
			</div>
			<div>
				<form:label path="password">Password: </form:label>
				<form:input	cssClass="form-control" type="password"	path="password"/>
				<form:errors path="password" cssClass="formError" element="p"/>
			</div>
			<div>
				<form:label	path="repeatPassword">Repeat password: </form:label>
				<form:input	cssClass="form-control" type="password"	path="repeatPassword"/>
				<form:errors path="repeatPassword" cssClass="formError"	element="p"/>
			</div>
			<div class="submit-container">
				<button type="submit" class="btn btn-primary submit-btn"><spring:message code="register"/></button>
			</div>
			<div class="bottom-message">
				<span><spring:message code="already_have_account"/> </span><span class="link-text"><spring:message code="login"/></span>
			</div>
		</form:form>
	</div>

	</body>
</html>
