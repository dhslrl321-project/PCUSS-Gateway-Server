FROM openjdk:17-ea-11-jdk-slim

VOLUME /tmp

COPY server/build/libs/gateway-server-0.0.1.jar GatewayService.jar

ENTRYPOINT ["java", "-jar", "GatewayService.jar"]
