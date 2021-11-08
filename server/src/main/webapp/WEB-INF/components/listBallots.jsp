<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.User" %><%--
  Created by IntelliJ IDEA.
  User: Lionel
  Date: 11/10/2021
  Time: 08:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<jsp:useBean id="ballots" type="java.util.Map" scope="application" beanName="ballots"/>

<html>
<head>
    <title>Liste des ballots (admin)</title>
    <base href="..">
    <link rel="stylesheet" type="text/css" href="static/vote.css">
</head>
<body>
<jsp:include page="title.jsp?title=Liste des ballots (admin)"/>
<main id="contenu" class="wrapper">
    <jsp:include page="menu.jsp"/>
    <article class="contenu">
        <h2>Voici la liste des <c:out value="${ballots.size()}" /> votants</h2>

        <ul>
            <c:forEach items="${ballots}" var="ballotEntry">
                <li>
                    <form action="election/deleteVote" method="post">
                        <c:out value="${ballotEntry.key}"/>
                        <input type="hidden" name="user" value="${ballotEntry.key}">
                        <input type="submit" value="supprimer">
                    </form>
                </li>
            </c:forEach>
        </ul>
    </article>
</main>
</body>
</html>