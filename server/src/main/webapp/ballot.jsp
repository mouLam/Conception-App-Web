<%@ page import="java.util.Map" %>
<%@ page import="fr.univlyon1.m1if.m1if03.C09.classes.Ballot" %>
<%@ page import="java.util.HashMap" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: mou_lamine
  Date: 06/10/2021
  Time: 17:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="ballots" type="java.util.Map" beanName="ballots" scope="application"/>
<html>
<head>
    <title>Ballot</title>
    <link rel="stylesheet" type="text/css" href="static/vote.css">
</head>
<body>
    <header>
        <c:choose>
            <c:when test="${sessionScope.user != null}">
                <p class="header-user"> Bonjour ${sessionScope.user.nom}</p>
            </c:when>
            <c:otherwise>
                <c:redirect url="index.html" />
            </c:otherwise>
        </c:choose>
        <h1 class="header-titre">Votre preuve de vote</h1>
    </header>

    <main id="contenu" class="wrapper">
        <aside class="menu">
            <h2>Menu</h2>
            <ul>
                <li><a href="vote.jsp">Voter</a></li>
                <li><a href="ballot.jsp">Votre vote</a></li>
                <li><a href="resultats.jsp">Résultats</a></li>
                <li><a href="Deco">Déconnexion</a></li>
            </ul>
        </aside>
        <article class="contenu">
            <%
                Map<String, String> monVote = new HashMap<>();
                ballots = (Map<String, Ballot>) application.getAttribute("ballots");

            %>

            <c:choose>
                <c:when test="${pageContext.request.getAttribute(\"selectCandidat\") != null}">

                    <p>Votre vote : ${ballots.values().size()}</p>
                </c:when>
                <c:otherwise>
                    <h2>Vous n'avez pas encore voté.</h2>
                </c:otherwise>
            </c:choose>
        </article>

    </main>
</body>
</html>
