#========================================
# Étape 1 : construire l'application
#========================================
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Copier Maven Wrapper et le pom.xml
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Donner le droit d'exécution au Maven Wrapper
RUN chmod +x mvnw

# Télécharger les dépendances Maven
RUN ./mvnw dependency:go-offline

# Copier le code source
COPY src ./src

# Construire le fichier JAR
RUN ./mvnw clean package -DskipTests


#========================================
# ÉTAPE 2 : exécuter l'application
#========================================
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copier uniquement le JAR généré à l'étape précédente
COPY --from=build /app/target/*.jar app.jar

# Port utilisé par le Web Service
EXPOSE 10000

# Démarrer Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]