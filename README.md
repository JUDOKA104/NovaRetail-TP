# 🚀 NovaRetail - Pipeline ETL Big Data (Apache Spark)

Ce projet a été réalisé dans le cadre de l'évaluation finale du module **Introduction du Big Data** à l'**IPSSI**.

L'objectif est de moderniser le système d'information de l'entreprise de e-commerce NovaRetail en migrant et nettoyant une ancienne base de données relationnelle vers un Data Lake orienté documents, le tout en respectant les normes RGPD.

## 🛠️ Technologies Utilisées
* **Langage :** Java 21
* **Framework Big Data :** Apache Spark (Spark SQL) 3.5.1
* **Gestionnaire de dépendances :** Maven
* **Bases de données :** PostgreSQL (Optimisé) / MySQL (Legacy)

## 🔄 Architecture du Pipeline (ETL)
L'application a été conçue de manière **strictement modulaire** (séparation entre l'orchestrateur, la configuration Spark et le moteur métier) et respecte le cycle suivant :

1. **Extract :** Connexion JDBC à la base de données source et extraction des données brutes.
2. **Transform (Map/Filter/Reduce) :**
   * *Nettoyage :* Suppression automatique des transactions dont le pays est `NULL` (données corrompues).
   * *Anonymisation (RGPD) :* Suppression de la colonne `customer_email` pour garantir la confidentialité des clients.
   * *Tri :* Organisation des données par ordre alphabétique de pays, puis par montant d'achat décroissant.
3. **Load :** Export des données transformées au format **JSON** dans un dossier `datalake_novaretail`.
   * 📂 **Partitionnement :** Utilisation de `.partitionBy("country")` pour créer une arborescence physique optimisée par pays.

---

## 🔀 Flexibilité de la Base de Données (PostgreSQL vs MySQL)

Bien que le cahier des charges initial requérait l'utilisation de **MySQL**, ce projet utilise par défaut **PostgreSQL**, un SGBD reconnu pour sa supériorité et sa robustesse dans les environnements analytiques et Big Data.

Cependant, grâce à l'architecture modulaire du code et à l'abstraction de l'API JDBC de Spark, **le projet est totalement agnostique**.

**Pour repasser sur MySQL :**
Il suffit de décommenter les blocs dédiés (laissés en évidence pour référence) à deux endroits :
1. Dans le fichier `pom.xml` (Dépendance `mysql-connector-j`).
2. Dans le fichier `EtlPipeline.java` (Variables `DB_URL`, `DB_DRIVER`, etc.).
*Aucune modification de la logique métier ETL n'est requise.*

## 🚀 Lancement du projet (Local / Windows)
1. S'assurer d'avoir configuré Hadoop pour Windows (`winutils.exe` et `hadoop.dll` dans `C:\hadoop\bin`).
2. Créer la base de données source (PostgreSQL ou MySQL) et y injecter les données de test.
3. Sous Java 16+, ajouter les arguments de VM suivants pour autoriser Spark à accéder à la mémoire interne :
   `--add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.nio=ALL-UNNAMED [...]`
4. Lancer la classe `Main.java`.

---
*Projet développé par Enzo ORIOL - IPSSI*
*En collaboration avec Paul Mercier-Bouvard & Antony BALENDE*