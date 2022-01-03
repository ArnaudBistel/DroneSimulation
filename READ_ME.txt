lancer l'application dans eclipse :

- lancer la class DroneSimulation.sarl comme une sarl application

Paramètres de simulation :

- Drone / entrepôts : ]0; +inf[
- nb entrepôts :  ]0; +inf[
- nb clients : ]0; +inf[
- poids colis : ]0; 5] ou ]0; 5]-]0; 5]
- vitesse du programme : [0.1; 5]

Paramètres Recuit simulé :

- T° initiale/range : ]0; +inf[
- Alpha : ]0; 1[
- Nb itér. par palier : [1; +inf[

Paramètres Q-Learning :

- Alpha : [0; 1]
- Gamma : [0; 1]
- Epsilon : [0; 1] (cette valeur doit être proche de 0 pour éviter des coûts infinis, si cela arrive dans le multitest, le graphique n'affichera plus rien et il faudra relancer l'application)


pour la partie multitest, pour écrire une range pour un des paramètres, écrire valeur1-valeur2