<%--
  Created by IntelliJ IDEA.
  User: Lionel
  Date: 09/10/2021
  Time: 00:57
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<aside class="menu">
    <h2>Menu</h2>
    <ul>
        <c:if test="${sessionScope.user == null}">
            <li><a href="index.html">Accueil</a></li>
        </c:if>
        <li><a href="election/resultats">Résultats</a></li>
        <c:if test="${sessionScope.user != null}">
            <li><a href="election/vote">Voter</a></li>
            <li><a href="election/ballot">Votre vote</a></li>
            <c:if test="${sessionScope.user.admin}">
                <li><a href="election/listBallots">Liste des ballots</a></li>
            </c:if>
            <li><a href="Deco">Déconnexion</a></li>
        </c:if>
    </ul>
</aside>