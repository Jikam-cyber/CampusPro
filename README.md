# 🎓 CampusPro — Système de Gestion Universitaire

## Démarrage rapide

### Prérequis
- Java 17+
- Maven 3.8+
- NetBeans 17+ (ou IntelliJ IDEA)

### Lancer en local
```bash
mvn spring-boot:run
```
Puis ouvrez : http://localhost:8080

---

## Comptes de démonstration

| Rôle         | Identifiant | Mot de passe |
|--------------|-------------|--------------|
| Administrateur | admin     | admin123     |
| Professeur   | P001        | prof123      |
| Étudiant     | ETU001      | etu123       |

---

## Structure du projet

```
CampusPro/
├── src/main/java/com/campuspro/
│   ├── CampusProApplication.java     ← Point d'entrée
│   ├── SecurityConfig.java           ← Config sécurité
│   ├── model/                        ← Entités
│   │   ├── Student.java
│   │   ├── Professor.java
│   │   ├── Matiere.java
│   │   └── Classe.java
│   ├── data/
│   │   └── DataStore.java            ← Données en mémoire
│   ├── service/
│   │   ├── DataService.java          ← Logique métier
│   │   └── PdfService.java           ← Export PDF
│   └── controller/
│       ├── AuthController.java       ← Login / Logout
│       ├── AdminController.java      ← Admin CRUD
│       ├── ProfessorController.java  ← Gestion notes
│       └── StudentController.java   ← Espace étudiant
├── src/main/resources/
│   ├── templates/                    ← Thymeleaf HTML
│   ├── static/css/style.css          ← Thème sombre
│   └── application.properties
├── pom.xml
└── render.yaml                       ← Déploiement Render
```

---

## Déploiement sur Render

1. Pushez le projet sur GitHub
2. Connectez votre repo sur [render.com](https://render.com)
3. Render détecte automatiquement `render.yaml`
4. Build : `mvn clean package -DskipTests`
5. Start : `java -jar target/campuspro.jar`

---

## Données simulées incluses

- **20 étudiants** avec notes générées automatiquement
- **10 professeurs** assignés à des matières et classes
- **5 classes** (L1 Info A/B, L2, L3, M1)
- **5 matières** (Maths, Info, Physique, Anglais, Algorithmes)
- **1 administrateur**
