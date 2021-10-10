<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="fr.univlyon1.m1if.m1if03.C09.classes.Candidat" %>
<%@ page import="java.util.logging.Logger" %>
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
        <c:choose>
            <c:when test="${sessionScope.user != null}">
                <p class="header-user"> Bonjour ${sessionScope.user.nom}</p>
            </c:when>
            <c:otherwise>
                <%
                    response.sendError(response.SC_FORBIDDEN, "AUTHENTIFICATION REQUISE"); //403
                %>
            </c:otherwise>
        </c:choose>
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

            <!-- Verification s'il a déjà voté -->
            <c:choose>
                <c:when test="${pageContext.servletContext.getAttribute(\"selectCandidat\") != null}">
                    <p> Vous avez déjà voté !</p>
                </c:when>
                <c:otherwise>
                    <form action="castVote" method="post">
                        <div>
                            <label for="candidatselect">Sélectionnez un candidat :</label>
                            <select name="selectCandidat" id="candidatselect" required>
                                <option value="----" selected disabled>---- </option>
                                <%
                                    Map<String, Integer> listeCandidats = new HashMap<>();
                                    Map<String, Candidat> recupCandidat = (Map<String, Candidat>) pageContext
                                            .getServletContext()
                                            .getAttribute("candidats");
                                    if (recupCandidat != null) {
                                        for (String nomCandidat
                                                : (recupCandidat.keySet())) {
                                            listeCandidats.put(nomCandidat, 0);
                                        }
                                    }
                                %>
                                <c:forEach items="<%= listeCandidats.keySet() %>" var="nomCandidat" >
                                    <option value="${nomCandidat}"> ${nomCandidat} </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="btnVote">
                            <button type="submit">Envoyer votre vote</button>
                        </div>
                    </form>
                </c:otherwise>
            </c:choose>

        </article>

    </main>
</body>
</html>
