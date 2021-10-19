# TP3 - v2.2 - C09

## 2.1 Pattern Contexte

L'initialisation de la liste de candidats est réalisée dans la méthode init(). Une exception est levée en cas de problème durant le chargement et un candidat vide est ajouté à la liste.

## 2.2 Pattern Chaine de Responsabilité

Création et ajout des filtres Authentification et Autorisation dans le fichire web.xml .

### 2.2.1 

Création du filtre Authentification qui permet de vérifier l'existence d'une session utilisateur ou non et de rediriger un utilisateur non connecté essayant d'accéder à certaines ressources interdites.

### 2.2.2

Création du filtre Autorisation permettant à l'administrateur d'accéder à la liste des ballots.

## 2.3 Pattern MVC

Création des servlets :
- Controleur : controleur principal
- VoteControleur : contrôle l'accès à la page vote.jsp si appelé via une méthode GET ou ballot.jsp si appelé via le formulaire de vote avec une méthode POST
- DecoControleur : contrôleur de déconnexion
- ResultatsControleur : comptabilise les votes et affiche les résultats de l'élection
- UserControleur : affiche le profil utilisateur via la méthode GET et met à jour le nom d'utilisateur via la méthode POST
- DeleteVoteControleur : supprime le ballot via une méthode POST
- BallotControleur : affiche le ballot de l'utilisateur (GET)
- ListBallotsControleur : affiche la liste des ballots

## Gestion du cache

Création d'un filtre qui intercepte les méthodes GET et POST sur /election/vote et /election/listBallot et vérifie l'attribut LastModifiedSince du Header de réponse. Cette fonctionnalité n'est pas encore opérationnelle. 
