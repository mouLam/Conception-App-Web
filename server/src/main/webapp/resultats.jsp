<%--
  Created by IntelliJ IDEA.
  User: mou_lamine
  Date: 06/10/2021
  Time: 16:59
  To change this template use File | Settings | File Templates.
--%>
<%--
  Created by IntelliJ IDEA.
  User: Lionel
  Date: 03/10/2021
  Time: 13:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="fr.univlyon1.m1if.m1if03.C09.classes.Bulletin" %>
<%@ page import="fr.univlyon1.m1if.m1if03.C09.classes.Candidat" %>
<%@ page import="java.util.List" %>
<%--jsp:useBean id="bulletins" type="java.util.List" beanName="bulletins" scope="application"/--%>

<html>
    <head>
        <title>Resultats Vote</title>
        <link rel="stylesheet" type="text/css" href="static/vote.css">
    </head>
    <body>
    <jsp:include page="./WEB-INF/components/header.jsp">
        <jsp:param name="title" value="Résultats de l'élection"/>
    </jsp:include>
    <main id="contenu" class="wrapper">
        <%@ include file="./WEB-INF/components/menu.jsp" %>
        <article class="contenu">
            <h2>Voici le résultat courant de l'élection</h2>

            <jsp:useBean id="votes" scope="request" class="java.util.HashMap" />
            <% votes = (HashMap) request.getServletContext().getAttribute("votes"); %>
            <ul>
                <c:forEach items="<%= votes.keySet()%>" var="nomCandidat">
                    <li><c:out value="${nomCandidat}"/> : <%= votes.get(pageContext.getAttribute("nomCandidat")) %> vote(s)</li>
                </c:forEach>
                <li>vote blanc : ${pageContext.servletContext.getAttribute("blancs")}</li>
            </ul>
        </article>
    </main>
    <%@ include file="./WEB-INF/components/footer.html" %>
    </body>
</html>
