# Estágio de construção (Builder)
FROM eclipse-temurin:21-alpine AS builder

# Instalar tzdata e configurar timezone no builder
RUN apk --no-cache add tzdata \
    && cp /usr/share/zoneinfo/America/Sao_Paulo /etc/localtime \
    && echo "America/Sao_Paulo" > /etc/timezone

WORKDIR /app

# Copiar o JAR da aplicação
COPY target/*.jar /app/api.jar

# Estágio de execução (Runtime)
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

# Copiar o JAR da aplicação
COPY --from=builder /app/api.jar /app/api.jar

EXPOSE 8080

# Definir parâmetros regionais da JVM e iniciar o app
CMD ["java", "-Duser.language=pt", "-Duser.country=BR", "-jar", "api.jar"]