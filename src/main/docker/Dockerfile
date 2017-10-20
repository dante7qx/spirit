FROM java:8
MAINTAINER Michael Dante <ch.sun@hnair.com>

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ENV APPS_HOME=/AppServer
RUN mkdir -p $APPS_HOME/

WORKDIR $APPS_HOME/

ADD ffx-1.0.jar $APPS_HOME/app.jar

RUN sh -c 'touch $APPS_HOME/app.jar'
RUN chmod 644 $APPS_HOME/app.jar

ENV JAVA_OPTS="-Xms2048m -Xmx2048m"

VOLUME ["$APPS_HOME/logs"]

EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar $APPS_HOME/app.jar" ]