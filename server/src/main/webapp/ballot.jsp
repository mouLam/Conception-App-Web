<%@ page import="java.util.Map" %>
<%@ page import="fr.univlyon1.m1if.m1if03.C09.classes.Ballot" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="fr.univlyon1.m1if.m1if03.C09.classes.Bulletin" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: mou_lamine
  Date: 06/10/2021
  Time: 17:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
                <%
                    response.sendError(response.SC_FORBIDDEN, "AUTHENTIFICATION REQUISE"); //403
                %>
            </c:otherwise>
        </c:choose>
        <h1 class="header-titre">Votre preuve de vote</h1>
    </header>

    <main id="contenu" class="wrapper">

        <%@ include file="./WEB-INF/components/menu.jsp" %>

        <article class="contenu">
            <%
                Bulletin monVote = (Bulletin) pageContext.getServletContext().getAttribute("monBulletin");
                pageContext.setAttribute("monVote", monVote);
            %>

            <c:choose>
                <c:when test="${pageContext.servletContext.getAttribute(\"selectCandidat\") != null &&
                        pageContext.getAttribute(\"monVote\") != null }">
                    <p>Votre vote : ${monVote.getCandidat().getPrenom()}  ${monVote.getCandidat().getNom()}</p>
                    <br>
                    <br>
                    <form action="deleteVote" method="post">
                        <p>
                            <input type="submit" name="actiondelete" value="Supprimer">
                        </p>
                    </form>
                </c:when>
                <c:otherwise>
                    <h2>Vous n'avez pas encore voté.</h2>
                </c:otherwise>
            </c:choose>
        </article>

    </main>

    <%@ include file="./WEB-INF/components/footer.html" %>

</body>
</html>
