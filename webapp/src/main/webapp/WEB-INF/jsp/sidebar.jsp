<%@ taglib  prefix="c"  uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib  prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <link href="https://fonts.googleapis.com/css?family=Barlow+Condensed" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Archivo+Narrow" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/style.css' />" >
    <title>Title</title>
</head>
<body>
    <div class="sidebar">
        <ul>
            <li><a href="<c:url value='/home' />">Home</a></li>
            <li><a href="<c:url value='/list' />">List</a></li>
            <li>Settings</li>
        </ul>
    </div>
</body>
</html>
