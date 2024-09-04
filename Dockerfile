# Etapa 1: Construcción del proyecto
FROM maven:3.9.5-eclipse-temurin-21 AS build
WORKDIR /timeless

# Copia el archivo pom.xml y descarga las dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia todo el código fuente y construye el proyecto
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Crear la imagen de la aplicación
FROM eclipse-temurin:21-jre-alpine
WORKDIR /timeless

# Copia el JAR generado desde la etapa anterior
COPY --from=build /timeless/target/*.jar timeless.jar

# Expone el puerto 8080 para la aplicación Spring Boot
EXPOSE 8080

# Define el comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/timeless/timeless.jar"]
