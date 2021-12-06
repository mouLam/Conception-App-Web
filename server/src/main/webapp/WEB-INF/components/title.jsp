<%--
  Created by IntelliJ IDEA.
  User: Lionel
  Date: 04/10/2021
  Time: 08:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header>
    <c:if test="${sessionScope.user != null}">
        <p class="header-user"> Bonjour <a href="election/user">${sessionScope.user.nom}</a></p>
    </c:if>
    <h1 class="header-titre">${param.title}</h1>
</header>