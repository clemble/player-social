FROM java:8-jre
MAINTAINER antono@clemble.com

EXPOSE 10014

ADD target/player-social-0.17.0-SNAPSHOT.jar /data/player-social.jar

CMD java -jar -Dspring.profiles.active=cloud -Dserver.port=10014 /data/player-social.jar
