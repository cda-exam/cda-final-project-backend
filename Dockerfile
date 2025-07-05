# Utilise une image OpenJDK légère pour exécuter l'application
FROM eclipse-temurin:17-jre-alpine

# Répertoire de travail dans le conteneur
WORKDIR /app

# Copie le jar généré dans le conteneur
COPY target/*.jar app.jar

# Expose le port utilisé par Spring Boot (par défaut 8080)
EXPOSE 8080

# Commande pour lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
