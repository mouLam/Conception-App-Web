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
    <title>Vote</title>
    <base href="..">
    <link rel="stylesheet" type="text/css" href="static/vote.css">
</head>
<body>
<jsp:include page="title.jsp?title=Votez pour qui vous voulez" />
<main id="contenu" class="wrapper">
    <jsp:include page="menu.jsp" />
    <article class="contenu">
        <c:choose>
            <c:when test="${requestScope.ballot != null}">
                <h2>Vous avez déjà voté.</h2>
            </c:when>
            <c:otherwise>
                <form method="post" action="election/vote">
                    <p>
                        <label for="candidat-select">Sélectionnez un candidat :</label>

                        <select name="candidat" id="candidat-select">
                            <option value="">----</option>
                            <c:forEach items="${applicationScope.candidats}" var="candidatEntry">
                                <option value='<c:out value="${candidatEntry.key}" />'><c:out value="${candidatEntry.value.nom}"/></option>
                            </c:forEach>
                        </select>
                    </p>
                    <p>
                        <input type="submit" name="action" value="Envoyer votre vote">
                    </p>
                </form>
            </c:otherwise>
        </c:choose>
    </article>
</main>
</body>
</html>
