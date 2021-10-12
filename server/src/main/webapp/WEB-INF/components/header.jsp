<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: mou_lamine
  Date: 11/10/2021
  Time: 00:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String uri = request.getRequestURI();
    String pageName = uri.substring(uri.lastIndexOf("/")+1);
    pageContext.setAttribute("pageName", pageName);
%>

<header>
    <c:choose>
        <c:when test="${sessionScope.user != null}">
            <p class="header-user"> Bonjour ${sessionScope.user.nom}</p>
            <c:choose>
                <c:when test="${pageName == 'vote.jsp'}">
                    <h1 class="header-titre"><%= request.getParameter("title")%></h1>
                </c:when>
                <c:when test="${pageName == 'ballot.jsp'}">
                    <h1 class="header-titre"><%= request.getParameter("title")%></h1>
                </c:when>
            </c:choose>
        </c:when>
        <c:otherwise>
            <% response.sendError(response.SC_FORBIDDEN, "AUTHENTIFICATION REQUISE"); //403 %>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${pageName == 'Deco'}">
            <h1 class="header-titre">Bienvenue sur notre application de vote révolutionnaire !</h1>
        </c:when>
        <c:when test="${pageName == 'update.jsp'}">
            <h1 class="header-titre"><%= request.getParameter("title")%></h1>
        </c:when>
        <c:when test="${pageName == 'resultats.jsp'}">
            <h1 class="header-titre"><%= request.getParameter("title")%></h1>
        </c:when>
        <c:when test="${pageName == 'index.jsp' || pageName == '' }">
            <h1 class="header-titre"><%= request.getParameter("title")%></h1>
        </c:when>
    </c:choose>

</header>