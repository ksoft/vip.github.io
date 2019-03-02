---
layout: post
title: sonarQube安装
category: 工具
tags: sonarQube
description: sonarQube 5.5 安装
---

### 一、下载软件
sonarQube从5.6开始,仅支持JDK1.8
- [[sonarQube5.5]](https://binaries.sonarsource.com/Distribution/sonarqube/sonarqube-5.5.zip)  
- [[Mysql5.6]](http://8dx.pc6.com/wwb6/mysql56401.zip)  

### 二、安装MySql
- 安装步骤略过  
- 安装完，新建数据库：sonar，用户名/密码：sonar/sonar

### 三、安装sonarQube5.5
- 解压sonarqube-5.5.zip
- 修改目录:conf下的sonar.properties：
```markdown
sonar.jdbc.username=sonar
sonar.jdbc.password=sonar
sonar.jdbc.url=jdbc:mysql://localhost:3306/sonar?useUnicode=true&characterEncoding=utf8&rewriteBatchedStatements=true&useConfigs=maxPerformance
sonar.web.port=9000
```

### 四、汉化
- 将汉化包[[l10n-zh]](http://www.datuzi.vip/softs/sonar-l10n-zh-plugin-1.10-RC2-SNAPSHOT.jar)，放到sonarQube安装目录下的：\extensions\plugins

### 五、启动
- 在bin目录下，执行：StartSonar.bat，启动sonarQube

### 六、与maven集成
- maven打包时使用如下命令：
```markdown
install org.codehaus.sonar:sonar-maven-plugin:4.5.7:sonar -f pom.xml
```
### 七、其它
- sonaQube中，如果需要配置排除，在菜单栏：配置>>通用设置>>排除 中配置
