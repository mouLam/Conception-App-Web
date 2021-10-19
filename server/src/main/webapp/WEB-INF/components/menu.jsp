<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: mou_lamine
  Date: 11/10/2021
  Time: 00:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<aside class="menu">

    <h2>Menu</h2>
    <ul>
        <c:choose>
            <c:when test="${sessionScope.user == null}">
                <li><a href="${pageContext.request.contextPath}/resultat">Résultats</a></li>
            </c:when>
            <c:otherwise>
                <li><a href="${pageContext.request.contextPath}/election/user">Mettre à jour mon profil</a></li>
                <li><a href="${pageContext.request.contextPath}/election/vote">Voter</a></li>
                <li><a href="${pageContext.request.contextPath}/election/ballot">Votre vote</a></li>
                <li><a href="${pageContext.request.contextPath}/election/resultats">Résultats</a></li>
                <c:if test="${sessionScope.user.admin}">
                    <li><a href="${pageContext.request.contextPath}/election/listBallots">Liste de ballots</a></li>
                </c:if>
                <li><a href="${pageContext.request.contextPath}/DecoController">Déconnexion</a></li>
            </c:otherwise>
        </c:choose>
    </ul>

</aside>


