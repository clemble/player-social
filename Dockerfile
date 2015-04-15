FROM java:8-jre
MAINTAINER antono@clemble.com

EXPOSE 10014

ADD target/player-social-*-SNAPSHOT.jar /data/player-social.jar

CMD java -jar -Dspring.profiles.active=cloud -Dlogging.config=classpath:logback.cloud.xml -Dserver.port=10014 /data/player-social.jar
