<%@ taglib  prefix="c"  uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib  prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
  <body>
    <c:url value="/login" var="loginUrl"/>
    <form action="${loginUrl}" method="post" enctype="application/x-www-form-urlencoded">
      <div>
        <label for="username">Username:  </label>
          <input id="username" name="login_username" type="text"/>
      </div>
      <div>
        <label for="password">Password:  </label>
        <input id="password" name="login_password" type="password"/>
      </div>
      <div>
        <label>
          <input name="login_rememberme" type="checkbox"/>
          <spring:message code="remember_me"/>
        </label>
      </div>
      <c:if test="${error == true}">
        <span class="formError">Error!</span>
      </c:if>
      <div class="submitButton">
        <input class="submitInput" type="submit" value=<spring:message code="login"/>/>
      </div>
    </form>
  </body>
</html>
