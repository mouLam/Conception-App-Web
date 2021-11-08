<%--
  Created by IntelliJ IDEA.
  User: Lionel
  Date: 03/10/2021
  Time: 13:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Résultats</title>
    <base href="..">
    <link rel="stylesheet" type="text/css" href="static/vote.css">
</head>
<body>
<jsp:include page="title.jsp?title=Résultats de l'élection"/>
<main id="contenu" class="wrapper">
    <jsp:include page="menu.jsp" />
    <article class="contenu">
        <h2>Voici le résultat courant de l'élection</h2>

        <ul>
            <c:forEach items="${requestScope.votes}" var="nomCandidat">
                <li><c:out value="${nomCandidat.key}"/> : <c:out value="${requestScope.votes[nomCandidat.key]}"/> vote(s)</li>
            </c:forEach>
        </ul>
    </article>
</main>
</body>
</html>