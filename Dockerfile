# Etapa 1: Construção
FROM maven:3.9.6-eclipse-temurin-21-jammy AS build

# Define a pasta de trabalho logo de cara
WORKDIR /app

# Força Bruta: Copia o pom.xml e a pasta src de forma explícita e separada
COPY pom.xml .
COPY src ./src

# Roda a compilação
RUN mvn clean package -DskipTests

# Etapa 2: Execução
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Pega o arquivo .jar compilado e renomeia para ficar limpo
COPY --from=build /app/target/*.jar ./app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]