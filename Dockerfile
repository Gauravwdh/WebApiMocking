FROM bankmonitor/spring-boot
MAINTAINER gaurav.wadhwa@tapzo.com

COPY build/libs/app-1.0-SNAPSHOT.jar /root/app-1.0-SNAPSHOT.jar
CMD ["java", "-jar app-1.0-SNAPSHOT.jar"]