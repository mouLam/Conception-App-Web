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
                <li><a href="vote.jsp">Voter</a></li>
                <li><a href="ballot.jsp">Votre vote</a></li>
                <li><a href="${pageContext.request.contextPath}/resultat">Résultats</a></li>
                <li><a href="Deco">Déconnexion</a></li>
            </c:otherwise>
        </c:choose>
    </ul>

</aside>


