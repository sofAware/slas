#!/usr/bin/env bash

REPOSITORY=/opt/slasApp
cd $REPOSITORY

APP_NAME=action_codedeploy
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep '.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

CURRENT_PID=$(pgrep -f $APP_NAME)

echo "> $CURRENT_PID" >> /home/ubuntu/deploy.log

if [ -z "$CURRENT_PID" ]
then
  echo "> 종료할 것 없음." >> /home/ubuntu/deploy.log
else
  echo "> kill -9 $CURRENT_PID" >> /home/ubuntu/deploy.log
  kill -15 "$CURRENT_PID" >> /home/ubuntu/deploy.log
  sleep 5
fi

echo "> $JAR_PATH 배포" >> /home/ubuntu/deploy.log
nohup java -jar $JAR_PATH > /dev/null 2> /dev/null < /dev/null &
