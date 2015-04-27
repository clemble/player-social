FROM java:8-jre
MAINTAINER antono@clemble.com

EXPOSE 10014

ADD target/player-social-*-SNAPSHOT.jar /data/player-social.jar

CMD java -jar -Dspring.profiles.active=cloud -Dhttp.connection.timeout=300000 -Dhttp.socket.timeout=300000 -Dlogging.config=classpath:logback.cloud.xml -Dserver.port=10014 /data/player-social.jar
