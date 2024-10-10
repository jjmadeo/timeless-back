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
# Establecer la variable de entorno TZ para configurar la zona horaria
ENV TZ=America/Argentina/Buenos_Aires
# Copia el JAR generado desde la etapa anterior
COPY --from=build /timeless/target/*.jar timeless.jar

# Exponer puertos para la aplicación y el debugger
EXPOSE 8080 5005

# Define el comando para ejecutar la aplicación con soporte para debugging remoto
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "/timeless/timeless.jar"]
