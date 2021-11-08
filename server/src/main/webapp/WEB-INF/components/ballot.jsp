<%--
  Created by IntelliJ IDEA.
  User: Lionel
  Date: 03/10/2021
  Time: 20:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Ballot</title>
    <base href="..">
    <link rel="stylesheet" type="text/css" href="static/vote.css">
</head>
<body>
<jsp:include page="title.jsp?title=Votre preuve de vote"/>
<main id="contenu" class="wrapper">
    <jsp:include page="menu.jsp"/>
    <article class="contenu">
        <c:choose>
            <c:when test="${requestScope.ballot != null}">
                <h2>Vous avez voté pour</h2>
                <c:set scope="request" var="candidat" value="${requestScope.ballot.bulletin.candidat}"/>
                <p><strong>${candidat.prenom} ${candidat.nom}</strong></p>
                <form action="election/deleteVote" method="post">
                    <input type="submit" value="supprimer">
                </form>
            </c:when>
            <c:otherwise>
                <h2>Vous n'avez pas encore voté.</h2>
            </c:otherwise>
        </c:choose>
    </article>
</main>
</body>
</html>
