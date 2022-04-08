# Parties
## P1 : Infrastructure côté serveur (serveur Web, protocole HTTP, PHP)
## P2 : Programmation côté serveur 2 (servlets / JSP)
## P3 : Design patterns et cache
## P4 : Web API (programmation REST)
## P5 : Programmation côté client (JavaScript, XHR, AJAX, DOM, jQuery)
## P6 : Performance des applications Web
## P7 : Optimisation d'une Single-Page Application

# TP5 - v3 - C09

## API

L'API a été déployée sur notre VM à cette adresse : http://192.168.75.41:8080/v3/ 

Cependant depuis la VM, visiblement seule une requête (celle de _/election/resultats_ avec l'url : http://192.168.75.41:8080/v3/election/resultats ) marche (sur Postman aussi).

Comme vous nous avez expliqué lors de la présentation, ce problème pourrait être dû à la non configuration du CORS.

### Solution à ce problème :

Afin que vous puissez visualiser le travail établit tout au long de ce projet, il est fortement recommandé d'exécuter le programme en `LOCALHOST`.

Adresse du lien en localhost : http://localhost:8080/v3_war/index.html

Ceci montre que nous avons bien implémenté l'API (COMPLET) demandé.

## Client

Nous avons utilisé notre API pour les requêtes, cependant notre API n'est pas fonctionnelle sur la VM. Le client peut tout de même être utilisé en **local host**.

Pour visualiser que le déploiement du client marche, vous pouvez aller sur ce lien : https://192.168.75.41/api/client

Remarque : Aucunes requêtes vers notre API n'a marché à cause du problème relevé ci-dessus.

### Bibliothèques
Nous avons utilisé [JQuery](https://jquery.com), [Mustache](https://github.com/janl/mustache.js), [Materialize](https://materializecss.com) et [JWT Web Tokens](https://jwt.io/).

