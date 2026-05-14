# Etapa 1: Prepara o terreno com Maven e Java 21 para compilar o código
FROM maven:3.9.6-eclipse-temurin-21-jammy AS build
COPY src /app
WORKDIR /app
# Roda o comando para gerar o .jar ignorando os testes
RUN mvn clean package -DskipTests

# Etapa 2: Pega o executável pronto e roda num Java 21 limpo e leve
FROM eclipse-temurin:21-jre-jammy
COPY --from=build /app/target/*.jar /app/app.jar
EXPOSE 8080
# O comando que "aperta o play" no Spring Boot
CMD ["java", "-jar", "/app/app.jar"]