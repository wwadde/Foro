FROM openjdk:21

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo JAR del proyecto al contenedor
COPY target/ForoApp-0.0.1-SNAPSHOT.jar .
EXPOSE 8080

# Comando para ejecutar la aplicación Spring Boot  cuando se inicie el contenedor
CMD ["java","-jar", "ForoApp-0.0.1-SNAPSHOT.jar"]