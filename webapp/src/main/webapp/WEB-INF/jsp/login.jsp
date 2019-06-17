<%@ taglib  prefix="c"  uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib  prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib  prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
  <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/style.css' />" >
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
  <link href="https://fonts.googleapis.com/css?family=Barlow+Condensed" rel="stylesheet">
  <link href="https://fonts.googleapis.com/css?family=Archivo+Narrow" rel="stylesheet">
  <title>
    <spring:message code="sport_matcher" /> - <spring:message code="login" />
  </title>
</head>
<body>
<%@include file="header.jsp" %>
<div class="main-container bgd-field-image">
  <div class="content-container">
    <div class="profile-title">
      <h2><spring:message code="login"/></h2>
    </div>
    <div class="form-container">
    <c:url value="/login" var="loginUrl"/>
    <form action="${loginUrl}" method="post" enctype="application/x-www-form-urlencoded">
      <div>
        <label for="username"><spring:message code="username"/>  </label>
        <input class="form-control" id="username" name="login_username" type="text"/>
      </div>
      <div>
        <label for="password"><spring:message code="password"/></label>
        <input id="password"  class="form-control" name="login_password" type="password"/>
      </div>
      <div class="form-check">
        <input type="checkbox" value="" id="defaultCheck1" name="login_remember_me"/>
        <label class="form-check-label" for="defaultCheck1"><spring:message code="remember_me"/></label>
      </div>
      <div class="submit-container">
        <button type="submit" class="btn btn-primary submit-btn"><spring:message code="login"/></button>
      </div>
      <c:if test="${error == true}">
        <div class="form-error login-error"><spring:message code="login_error"/></div>
      </c:if>
    </form>
  </body>
</html>
