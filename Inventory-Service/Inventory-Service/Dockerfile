FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# ca.pem को कंटेनर में कॉपी करें
COPY src/main/resources/certs /app/certs

COPY target/*.jar app.jar
# Order-Service के लिए 8082, Inventory-Service के लिए 8083 रखें
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]