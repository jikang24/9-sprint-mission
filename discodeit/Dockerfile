FROM amazoncorretto:17

LABEL maintainer="jiwkang" \
      description="Discodeit chatting system" \
      version="1.0.0"

WORKDIR /app

EXPOSE 80

COPY . .
RUN ./gradlew build --no-daemon -x test

ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8
ENV JVM_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar"]