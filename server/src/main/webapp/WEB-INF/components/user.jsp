<%--
  Created by IntelliJ IDEA.
  User: Lionel
  Date: 09/10/2021
  Time: 22:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<jsp:useBean id="user" type="fr.univlyon1.m1if.m1if03.classes.User" beanName="user" scope="session"/>
<html>
<head>
    <title>User <%= user.getLogin()%>
    </title>
    <base href="..">
    <link rel="stylesheet" type="text/css" href="static/vote.css">
</head>
<body>
<jsp:include page="title.jsp?title=Votre compte"/>
<main id="contenu" class="wrapper">
    <jsp:include page="menu.jsp"/>
    <article class="contenu">
        <h2>Vous pouvez uniquement modifier votre nom.</h2>
        <form method="post" action="election/user">
            <label for="login">Login : </label><input type="text" name="login" id="login" value="${user.login}" disabled><br>
            <label for="nom">Nom : </label><input type="text" name="nom" id="nom" value="${user.nom}"><br>
            <input type="submit" value="Modifier">
        </form>
    </article>
</main>
</body>
</html>