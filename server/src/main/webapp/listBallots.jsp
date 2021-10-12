<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="fr.univlyon1.m1if.m1if03.C09.classes.User" %><%--
  Created by IntelliJ IDEA.
  User: Lionel
  Date: 11/10/2021
  Time: 08:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<jsp:useBean id="ballots" type="java.util.Map" scope="application" beanName="ballots"/>

<c:if test="${sessionScope.user == null || !sessionScope.user.admin}">
    <%
        // Remarque : on ne peut pas avoir 2 blocs c:if qui font chacun un sendError (erreur 500).
        // D'où la double condition et les 2 types d'erreurs dans la même instruction sendError()
        response.sendError(session.getAttribute("user") == null ? HttpServletResponse.SC_FORBIDDEN : HttpServletResponse.SC_UNAUTHORIZED, "Admin : " + ((User) session.getAttribute("User")).isAdmin());
    %>
</c:if>

<html>
<head>
    <title>Liste des ballots (admin)</title>
    <link rel="stylesheet" type="text/css" href="static/vote.css">
</head>
<body>
<jsp:include page="WEB-INF/components/header.jsp?title=Liste des ballots (admin)"/>
<main id="contenu" class="wrapper">
    <jsp:include page="WEB-INF/components/menu.jsp"/>
    <article class="contenu">
        <h2>Voici la liste des <c:out value="${ballots.size()}" /> votants</h2>

        <ul>
            <c:forEach items="${ballots}" var="ballotEntry">
                <li>
                    <form action="deleteVote" method="post">
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
