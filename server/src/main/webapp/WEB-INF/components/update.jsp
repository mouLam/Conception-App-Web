<%--
  Created by IntelliJ IDEA.
  User: mou_lamine
  Date: 11/10/2021
  Time: 20:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Update</title>
    <style type="text/css">
        <%@include file="../../static/vote.css" %>
    </style>
</head>
<body>
<jsp:include page="header.jsp">
    <jsp:param name="title" value="${'Mise à jour de votre profil'}"/>
</jsp:include>
<main id="contenu" class="wrapper">

    <%@ include file="menu.jsp" %>
    <article class="contenu">
        <form action="election/user" method="post">
            <div>
                <label>Votre vouveau nom :
                    <input type="text" name="newname" required>
                </label>
            </div>
            <div>
                <input type="submit" name="actionupdate" value="Mettre à jour">
            </div>
        </form>
    </article>

</main>

<%@ include file="footer.html" %>

</body>
</html>
