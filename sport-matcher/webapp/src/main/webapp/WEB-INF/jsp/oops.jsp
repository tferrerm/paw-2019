<%@ taglib  prefix="c"  uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib  prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
  <head>
    <link rel="stylesheet" href="<c:url value="/css/style.css"/>"/>
    <title><spring:message code="sport_matcher" /> - <spring:message code="oops" /></title>
  </head>
  <body>
    <%@ include file="header.jsp" %>
    <div class="http-error">
      <h2><spring:message code="oops"/></h2>
      <span class="just-row">
        <span class="notice"><spring:message code="click_oops"/> <br/></span>
        <a class="link-text" href="<c:url value='/' />"><span class="link-text notice"><spring:message code="here"/></span></a>
      </span>
    </div>
  </body>
</html>
