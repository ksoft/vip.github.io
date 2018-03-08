---
layout: post
title: springboot linux脚本
category: 技术
tags: springboot linux脚本
keywords: springboot linux脚本
---
## start.sh
```markdown
#!/bin/sh 
rm -f tpid 
nohup java -jar myapp.jar > /dev/null 2>&1 & 
echo $! > tpid 
echo Start Success!
```
## stop.sh
```markdown
#!/bin/sh 
APP_NAME=myapp 
tpid=`ps -ef|grep $APP_NAME|grep -v grep|grep -v kill|awk '{print $2}'` 
if [ ${tpid} ]; then
    echo 'Stop Process...'
    kill -15 $tpid 
fi 
sleep 5
tpid=`ps -ef|grep $APP_NAME|grep -v grep|grep -v kill|awk '{print $2}'` 
if [ ${tpid} ]; then
    echo 'Kill Process!'
    kill -9 $tpid 
else
    echo 'Stop Success!' 
fi
```
## check.sh
```markdown
#!/bin/sh 
APP_NAME=myapp 
tpid=`ps -ef|grep $APP_NAME|grep -v grep|grep -v kill|awk '{print $2}'` 
if [ ${tpid} ]; then
        echo 'App is running.' 
else
        echo 'App is NOT running.' 
fi
```
## kill.sh
```markdown
#!/bin/sh 
APP_NAME=myapp 
tpid=`ps -ef|grep $APP_NAME|grep -v grep|grep -v kill|awk '{print $2}'` 
if [ ${tpid} ]; 
then
    echo 'Kill Process!'
    kill -9 $tpid 
fi
```