<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: mou_lamine
  Date: 11/10/2021
  Time: 00:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8" %>


<aside class="menu">

    <h2>Menu</h2>
    <ul>
        <c:choose>
            <c:when test="${sessionScope.user == null}">
                <li><a href="<c:url value="resultat"/>">Résultats</a></li>
            </c:when>
            <c:otherwise>
                <li><a href="<c:url value="/election/user"/>">Mettre à jour mon profil</a></li>
                <li><a href="<c:url value="/election/vote"/>">Voter</a></li>
                <li><a href="<c:url value="/election/ballot"/>">Votre vote</a></li>
                <li><a href="<c:url value="/election/resultats"/>">Résultats</a></li>
                <c:if test="${sessionScope.user.admin}">
                    <li><a href="<c:url value="/election/listBallots"/>">Liste de ballots</a></li>
                </c:if>
                <li><a href="<c:url value="/DecoController"/>">Déconnexion</a></li>
            </c:otherwise>
        </c:choose>
    </ul>

</aside>


