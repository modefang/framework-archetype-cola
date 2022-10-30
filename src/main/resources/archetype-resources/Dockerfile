FROM java:jdk-1.8

USER root
WORKDIR /data
COPY ./start/target/*.jar /data/app.jar

ENTRYPOINT ["/bin/sh"]
CMD ["-c", "java $JAVA_OPTS -jar /data/app.jar"]