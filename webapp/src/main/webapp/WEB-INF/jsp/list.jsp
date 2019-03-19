<%@	taglib	prefix="c"	uri="http://java.sun.com/jstl/core_rt"%>
<%@	taglib	prefix="form"	uri="http://www.springframework.org/tags/form"	%>
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
	</head>

	<body>
	<%@include file="header.jsp" %>
	<div class="main-container">
		<%@include file="sidebar.jsp" %>
		<div class="content-container">
			<h2>List title</h2>
			<div class="tbl">
				<div class="table-header">
					<div><spring:message code="establishment"/></div>
					<div><spring:message code="sport"/></div>
                    <div><spring:message code="organizer"/></div>
                    <div><spring:message code="vacancies"/></div>
                    <div><spring:message code="date"/></div>
                    <div></div>
				</div>
				<div class="custom-row">
					<div>These</div>
					<div>Values</div>
					<div>Are</div>
					<div>Now</div>
					<div>Hardcoded</div>
                    <div>
                        <a href="<c:url value="/event/1"/>"> <button type="button" class="btn btn-primary view-event"><spring:message code="view_event"/></button></a>
                    </div>

                </div>
				<div class="custom-row">
					<div>These</div>
					<div>Values</div>
					<div>Are</div>
					<div>Now</div>
					<div>Hardcoded</div>
                    <div>
                        <button type="button" class="btn btn-primary view-event"><spring:message code="view_event"/></button>
                    </div>
				</div>
				<div class="custom-row">
					<div>These</div>
					<div>Values</div>
					<div>Are</div>
					<div>Now</div>
					<div>Hardcoded</div>
                    <div>
                        <button type="button" class="btn btn-primary view-event"><spring:message code="view_event"/></button>
                    </div>
				</div>
				<div class="custom-row">
					<div>These</div>
					<div>Values</div>
					<div>Are</div>
					<div>Now</div>
					<div>Hardcoded</div>
                    <div>
                        <button type="button" class="btn btn-primary view-event"><spring:message code="view_event"/></button>
                    </div>
				</div>
                <div class="custom-row">
                    <div>These</div>
                    <div>Values</div>
                    <div>Are</div>
                    <div>Now</div>
                    <div>Hardcoded</div>
                    <div>
                        <button type="button" class="btn btn-primary view-event"><spring:message code="view_event"/></button>
                    </div>
                </div>
                <div class="custom-row">
                    <div>These</div>
                    <div>Values</div>
                    <div>Are</div>
                    <div>Now</div>
                    <div>Hardcoded</div>
                    <div>
                        <button type="button" class="btn btn-primary view-event"><spring:message code="view_event"/></button>
                    </div>
                </div>
                <div class="custom-row">
                    <div>These</div>
                    <div>Values</div>
                    <div>Are</div>
                    <div>Now</div>
                    <div>Hardcoded</div>
                    <div>
                        <button type="button" class="btn btn-primary view-event"><spring:message code="view_event"/></button>
                    </div>
                </div>
                <div class="custom-row">
                    <div>These</div>
                    <div>Values</div>
                    <div>Are</div>
                    <div>Now</div>
                    <div>Hardcoded</div>
                    <div>
                        <button type="button" class="btn btn-primary view-event"><spring:message code="view_event"/></button>
                    </div>
                </div>
                <div class="custom-row">
                    <div>These</div>
                    <div>Values</div>
                    <div>Are</div>
                    <div>Now</div>
                    <div>Hardcoded</div>
                    <div>
                        <button type="button" class="btn btn-primary view-event"><spring:message code="view_event"/></button>
                    </div>
                </div>
                <div class="custom-row">
                    <div>These</div>
                    <div>Values</div>
                    <div>Are</div>
                    <div>Now</div>
                    <div>Hardcoded</div>
                    <div>
                        <button type="button" class="btn btn-primary view-event"><spring:message code="view_event"/></button>
                    </div>
                </div>
                <div class="custom-row">
                    <div>These</div>
                    <div>Values</div>
                    <div>Are</div>
                    <div>Now</div>
                    <div>Hardcoded</div>
                    <div>
                        <button type="button" class="btn btn-primary view-event"><spring:message code="view_event"/></button>
                    </div>
                </div>
                <div class="custom-row">
                    <div>These</div>
                    <div>Values</div>
                    <div>Are</div>
                    <div>Now</div>
                    <div>Hardcoded</div>
                    <div>
                        <button type="button" class="btn btn-primary view-event"><spring:message code="view_event"/></button>
                    </div>
                </div>
                <div class="custom-row">
                    <div>These</div>
                    <div>Values</div>
                    <div>Are</div>
                    <div>Now</div>
                    <div>Hardcoded</div>
                    <div>
                        <button type="button" class="btn btn-primary view-event"><spring:message code="view_event"/></button>
                    </div>
                </div>
			</div>
			<div class="table-navigator">
				<div><button type="button" class="btn btn-secondary"><spring:message code="first"/></button><button type="button" class="btn btn-secondary"><spring:message code="back"/></button></div>
				<span><spring:message code="showing_items"/> 0-5 <spring:message code="of"/> 5</span>
				<div><button type="button" class="btn btn-secondary"><spring:message code="next"/></button><button type="button" class="btn btn-secondary"><spring:message code="last"/></button></div>
			</div>
        </div>

    </div>


	</body>
</html>