
---

# Gestion des Rendez-vous d’un Cabinet Médical

---

## Réalisé par

**Abla MARGHOUB**

## Encadré par

**Pr. Mohamed LACHGAR**

## Module

**Techniques de Programmation Avancée**

## Établissement

**École Normale Supérieure - Université Cadi Ayyad**

---

## 1. Description du projet

### 1.1 Contexte fonctionnel

Dans un cabinet médical, les secrétaires doivent gérer la planification, les confirmations, les annulations et la disponibilité des médecins.
Une solution numérique permet de simplifier ce processus.

### 1.2 Objectif de l’application

L’application a pour objectif de faciliter la prise, la consultation et la modification des rendez-vous médicaux entre les patients et les médecins.

### 1.3 Public cible / Cas d’usage

* **Assistante** : planifie, confirme ou annule les rendez-vous.
* **Médecin** : gère ses rendez-vous et consulte les informations des patients.

### 1.4 Fonction principale

L’application permet de gérer les rendez-vous médicaux en ligne (ajout, modification, annulation, filtrage) via une interface web intuitive.

---

## 2. Architecture technique

### 2.1 Stack technologique

| **Catégorie**       | **Technologies utilisées**                                                       |
| ------------------- | -------------------------------------------------------------------------------- |
| **Backend**         | Spring Boot 3.5.6, Spring Web MVC, Spring Data JPA / Hibernate, Spring Dev Tools |
| **Frontend**        | Thymeleaf, HTML5, CSS, Bootstrap 5                                               |
| **Base de données** | MySQL                                                                            |
| **Build Tool**      | Maven                                                                            |
| **Serveur / JDK**   | Java 21 (JDK 21)                                                                 |

### 2.2 Structure du projet
<img width="777" height="678" alt="image" src="https://github.com/user-attachments/assets/f37fa391-e2e8-409f-a797-3af99791ab41" />

### 2.3 Architecture en couches (MVC)

L’application suit une architecture en couches (**MVC : Model - View - Controller**).

#### 1. Navigateur (Frontend)

* L’utilisateur (assistante ou médecin) interagit via un navigateur web.
* Il envoie des requêtes HTTP (GET, POST, etc.) vers l’application, par exemple pour afficher la liste des rendez-vous ou en ajouter un.

#### 2. Contrôleur Spring

* C’est la porte d’entrée du backend.
* Il reçoit la requête HTTP, récupère ou envoie les données nécessaires, et choisit quelle vue (page HTML) afficher.

#### 3. Repository

* Communique directement avec la base de données via **Spring Data JPA**.
* Exécute les requêtes SQL générées automatiquement (`findAll`, `findById`, `save`, `delete`, etc.).

#### 4. Base de données (MySQL)

* Contient les tables : **patient**, **medecin**, **rendez_vous**.
* Stocke toutes les données persistantes de l’application.

#### 5. Vue Thymeleaf

* Le contrôleur renvoie un **modèle (Model)** contenant les données.
* **Thymeleaf** affiche dynamiquement ces données dans les pages HTML grâce à des expressions.

---

### 2.4 Flux global de l’application

<img width="506" height="490" alt="image" src="https://github.com/user-attachments/assets/9ec019a8-de7d-4623-b5ab-61b8c52ce36e" />


---

## 3. Fonctionnalités principales

* CRUD complet sur les rendez-vous, médecins et patients
* Filtrage dynamique par date, spécialité, statut, médecin
* Gestion des statuts : Confirmé, Annulé
* Modification et suppression d’un rendez-vous
* Interface responsive en Bootstrap

---

## 4. Modèle de données

### 4.1 Entités principales

* Patient
* Médecin
* RendezVous
<img width="599" height="176" alt="image" src="https://github.com/user-attachments/assets/1c728763-d4d0-4141-8555-262f6b625c1f" />

### 4.2 Relations

* Patient (OneToMany) → RendezVous
* Médecin (OneToMany) → RendezVous
* RendezVous (ManyToOne) → Patient et Médecin
  

### 4.3 Configuration base de données

MySQL configuré via le fichier **application.properties**.

<img width="619" height="236" alt="image" src="https://github.com/user-attachments/assets/7648ebf0-2b3c-486d-8ce5-53f59bacfbef" />


---

## 5. Lancer le projet

### 5.1 Prérequis

* JDK 21
* Maven
* MySQL en service

### 5.2 Installation

1. Cloner le projet
   `git clone https://github.com/abmarghoub/mini-projet.git`
2. Se placer dans le dossier du projet
   `cd mini-projet`
3. Lancer l’application
   `mvn spring-boot:run`

### 5.3 Accès

* Page d’accueil : [http://localhost:8080](http://localhost:8080)
* Page statistiques : [http://localhost:8080/statistiques](http://localhost:8080/statistiques)

---

## 6. Démonstration

Lien de la vidéo de démonstration :
https://drive.google.com/drive/folders/13r9_Pc8GxxqnyTAy-mFx7vIZ3xrl7zCe?usp=sharing
