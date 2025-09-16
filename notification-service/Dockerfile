# Etapa 1: Builder (gera o .jar)
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder

# Instalar tzdata e configurar timezone
RUN apk --no-cache add tzdata \
    && cp /usr/share/zoneinfo/America/Sao_Paulo /etc/localtime \
    && echo "America/Sao_Paulo" > /etc/timezone

WORKDIR /app

# Copiar arquivos de configuração e código fonte
COPY pom.xml .
COPY src ./src

# Rodar build (gera o jar em target/)
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:21-alpine AS runtime

# Copiar timezone já configurado do builder
COPY --from=builder /usr/share/zoneinfo/America/Sao_Paulo /usr/share/zoneinfo/America/Sao_Paulo
COPY --from=builder /etc/localtime /etc/localtime
COPY --from=builder /etc/timezone /etc/timezone

# Configurar locale e timezone
ENV LANG=pt_BR.UTF-8
ENV LANGUAGE=pt_BR:pt
ENV LC_ALL=pt_BR.UTF-8
ENV TZ=America/Sao_Paulo

WORKDIR /app

# Criar usuário e grupo de execução
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copiar o JAR compilado do builder
COPY --from=builder /app/target/*.jar /app/api.jar

EXPOSE 8080

# Definir parâmetros regionais da JVM e iniciar o app
CMD ["java", "-Duser.language=pt", "-Duser.country=BR", "-jar", "api.jar"]
