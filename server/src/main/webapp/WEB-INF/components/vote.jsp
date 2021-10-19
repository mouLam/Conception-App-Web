<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="fr.univlyon1.m1if.m1if03.C09.classes.Candidat" %>
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
        <meta charset="UTF-8">
        <title>Vote</title>
        <style type="text/css">
            <%@include file="../../static/vote.css" %>
        </style>
    </head>
    <body>
        <jsp:include page="header.jsp">
            <jsp:param name="title" value="${'Votez pour qui vous voulez'}"/>
        </jsp:include>

        <main id="contenu" class="wrapper">

            <%@ include file="menu.jsp" %>

            <article class="contenu">

                <!-- Verification s'il a déjà voté -->
                <c:choose>
                    <c:when test="${pageContext.servletContext.getAttribute(\"selectCandidat\") != null
                            && pageContext.request.getParameter(\"selectCandidat\") != \"\"}">
                        <p> Vous avez déjà voté !</p>

                    </c:when>
                    <c:otherwise>
                        <form action="${pageContext.servletContext.contextPath}/election/vote" method="post">
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
        <%@ include file="footer.html" %>
    </body>
</html>
