# OpenClassrooms Projet 10 - Developpeur d'Application Java #

## Améliorez le système d’information de la bibliothèque ##


### Documentation ###
#### *La documentation relative aux différents tickets (README.md, SQL, UML) se trouve dans le répertoire -docs* ####
+ sql : structure et data de la base de données PostgreSQL
+ uml : diagrammes UML (classes, MPD, arborescence, usecase, ...)

### Langages et Technologies utilisées : ###

+ Java / Html / Css

+ SpringBoot / Web / Jpa

+ PostgreSQL

+ SpringSecurity

+ SpringCloud Gateway

+ SpringMail / SpringScheduler

+ Eureka

+ Feign

+ Actuator / Lombok / DevTools / ModelMapper-MapStruct

+ Thymeleaf / Bootstrap

### Base de données : *Installation Version finale* ###

Note : Des instructions pour initialiser les bases de données pour chaque étape de l'implémentation sont indiquées dans les fichiers README-Ticket#.md
dans les répertoires -docs/docs-ticket#/sql

#### Configuration de la base de données Postgres dans application.properties ####

#### Création de la base de données dans pgAdmin ####

[//]: # (TODO : inclure les images installation base de données P10 version finale)
 

### Installation ###

IntelliJ = File -> New ->Project from Version Control

url : https://github.com/DocMcCoy30/OCprojet10.git

file : dossier de destination

ou

```
$ cd ../chemin/vers/le/dossier/de/destination
$ git clone https://github.com/DocMcCoy30/OCprojet7.git

$mvn install pour chaque service si nécessaire
```

### Demarrage des services ###

1- imageserver contient les images (couvertures des livres notamment) : les infos pour l'installation et la configuration sont dans le ReadMe du module.

2- eureka-server

3- utilisateur-service / livre-service / emprunt-service / reservation-service

4- clientUI / email-service

5- gateway-server

Pour chaque service
```
$ cd ../path/to/the/file/target
$ java -jar nom-du-service.jar
```

### Utilisation et fonctionnalités : *Version Finale ###

Page d'accueil accessible à : https://localhost:8999/ (gateway)

Note : Les informations sur les différentes fonctionnalités implémentées sont dans les fichiers README-Ticket# : 
-docs/docs-ticket#/README-Ticket#.md

[//]: # (TODO : récupérer les fonctionnalités du dernier ticket)
