
<%--
  Created by IntelliJ IDEA.
  User: mou_lamine
  Date: 06/10/2021
  Time: 17:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Vote</title>
    <link rel="stylesheet" type="text/css" href="static/vote.css">
</head>
<body>
    <header>
        <c:if test="${sessionScope.user != null}">
            <p class="header-user"> Bonjour ${sessionScope.user.nom}</p>
        </c:if>
        <h1 class="header-titre">Votez pour qui vous voulez</h1>
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
            

        </article>

    </main>
</body>
</html>
