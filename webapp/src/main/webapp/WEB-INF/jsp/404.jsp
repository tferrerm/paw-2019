<%@ taglib  prefix="c"  uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib  prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
  <head>
    <link rel="stylesheet"  href="<c:url  value="/css/style.css"/>"  />
  </head>
  <body>
    <h1>Nothing found over here (404)</h1>
    <span class="just-row">
      <span><spring:message code="click_oops"/></span>
      <a class="link-text" href="<c:url value='/' />"><span class="link-text"><spring:message code="here"/></span></a>
    </span>
  </body>
</html>
