# TP4 - v3 - C09

## 1. Ressources

Deux contrôleurs ont été créés, un contrôleur principal qui prend en charge toutes les requêtes qui commencent par /election/* et un autre pour celles qui commencent par /user/* .
Nos URLs ont été modifiées pour qu'elles correspondent à des URLs de ressouces en REST.

Nous avons implémenté les URLs suivantes : 

### Résultats 
- /election/resultats (GET)

### Candidats
- /election/candidats (GET)
- /election/candidats/noms (GET)
- /election/candidats/{candidatId} (GET)
- /election/candidats/update (POST)

### Ballots
- /election/ballots (GET)
- /election/ballots (POST)
- /election/ballots/{ballotId} (GET)
- /election/ballots/{ballotId} (DELETE)
- /election/ballots/byUser/{ballotId} (GET)

### Votes
- /election/votes/{voteId} (GET)
- /election/votes/byUser/{voteId} (GET)
- /election/votes/byUser/{voteId} (PUT)

### Users
- /users (GET)
- /users/{userId} (GET)
- /users/{userId}/nom (PUT)
- /users/{userId}/ballot (GET)
- /users/{userId}/vote (GET)
- /users/login (POST)
- /users/logout (POST)

## 2. Transactions sans états
Nous avons supprimé l'utilisation de HttpSession pour la gestion de la session utilisateur, et utilisé la bibliothèque Java-JWT pour la remplacer par l'utilisation d'un token. Ce token sera utilisé pour les requêtes nécessitant une vérification de connexion.

