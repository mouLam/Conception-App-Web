<%--
  Created by IntelliJ IDEA.
  User: Lionel
  Date: 05/11/2021
  Time: 01:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Election</title>
    <base href="..">
    <link rel="stylesheet" type="text/css" href="static/vote.css">
</head>
<body>
<jsp:include page="title.jsp?title=Election%20-%20page d'accueil"/>
<main id="contenu" class="wrapper">
    <jsp:include page="menu.jsp"/>
    <article class="contenu">
        <p>Votre dernière action (<%= request.getAttribute("action") %>) a bien été réalisée.</p>
        <p><strong>Choisissez un item du menu...</strong></p>
    </article>
</main>
</body>
</html>