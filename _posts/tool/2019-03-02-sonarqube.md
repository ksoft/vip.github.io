---
layout: post
title: sonarqube-6.7.6 安装
category: 工具
tags: sonarQube
description: sonarqube-6.7.6 安装
---

### 一、下载软件
sonarQube从5.6开始,仅支持JDK1.8
- [[sonarqube-6.7.6]](https://binaries.sonarsource.com/Distribution/sonarqube/sonarqube-6.7.6.zip)  
- [[Mysql5.6]](http://8dx.pc6.com/wwb6/mysql56401.zip)  

### 二、安装MySql
- 安装步骤略过
- 安装完，新建数据库：sonar，用户名/密码：sonar/sonar
- MySQL的参数可能要修改：my.cnf中的max_allowed_packet参数需要调大，比如200M

### 三、安装sonarQube
- 解压sonarqube-6.7.6.zip
- 修改目录:conf下的sonar.properties：
```markdown
sonar.jdbc.username=sonar
sonar.jdbc.password=sonar
sonar.jdbc.url=jdbc:mysql://localhost:3306/sonar?useUnicode=true&characterEncoding=utf8&rewriteBatchedStatements=true&useConfigs=maxPerformance
#配置web端访问端口，默认9000
sonar.web.port=9000
#配置编码
sonar.sorceEncoding=UTF-8
```

### 四、启动
- 在bin目录下，执行：StartSonar.bat，启动sonarQube

### 五、汉化
- 启动后，访问：http://localhost:9000，登录默认帐号/密码：admin/admin，在配置>应用市场中搜索Chinese Pack，CheckStyle Pack、GitLab，安装插件

### 六、与maven集成
- 修改maven /conf/settings.xml，profiles下新增以下代码：
```markdown
    <profile>
        <id>sonar</id>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
        <properties>
            <!-- Optional URL to server. Default value is http://localhost:9000 -->
            <sonar.host.url>
              http://localhost:9000
            </sonar.host.url>
        </properties>
    </profile>
```
- maven打包时使用如下命令，就能分析项目，并将结果写入sonarQube数据库，并在sonarQube Web页面展示：
```markdown
mvn compile sonar:sonar
```

### 七、用管理员登录GitLab，在Runners页面中，可以看到GitLabURL和token
![GitLabRunner](/photos/gitlab-runner/gitlab-runner-1.png)

### 八、集成GiltLab Runner
- windows 安装：
    - 下载[[gitlab-runner-windows-386]](https://gitlab-runner-downloads.s3.amazonaws.com/latest/binaries/gitlab-runner-windows-386.exe)
    - 将下载的文件放于D:/gitRunner目录下，cmd执行以下命令，在当前目录会生成config.toml文件
    ```markdown
      cd D:/gitRunner
      #1.安装服务
      gitlab-runner-windows-386.exe install
      #2.执行start，或者在windwos服务中，启动服务：gitlab-runner
      gitlab-runner-windows-386.exe start
      #3.注册runner
      gitlab-runner-windows-386.exe register
    ```
    - 第3步，按提示输入第七步的url和token，tag需要跟之后需要配置的.gitlab-ci.yml配置的tags一致，才能执行
    - 安装完在GitLab Runners里，可以看到如下信息：
    ![GitLabRunner](/photos/gitlab-runner/gitlab-runner-2.png)
- linux 下安装

### 九、集成GitLab插件
- 在GitLab中，创建accessToken:
![GitLab](/photos/gitlab/gitlab-accessToken.png)
- 在sonarQube Web页面中，配置accessToken和GitLab URL

### 十、创建.gitlab-ci.yml
```markdown
image: maven:3-jdk-8

stages:
  - build
  - test
  - deploy
  
myjob_build:
  stage: build
  script:
    - mvn --batch-mode compile sonar:sonar
  tags:
    - test-runner

myjob_markcommit:
  stage: build
  script:
    - mvn --batch-mode compile sonar:sonar -Dsonar.analysis.mode=preview -Dsonar.issuesReport.html.enable=true -Dsonar.gitlab.project_id=%CI_PROJECT_ID% -Dsonar.gitlab.commit_sha=%CI_COMMIT_SHA% -Dsonar.gitlab.ref_name=%CI_COMMIT_REF_NAME%
  tags:
    - test-runner

myjob_test:
  stage: test
  script:
    - mvn test
  tags:
    - test-runner

myjob_deploy:
  stage: deploy
  script:
    - echo "deploy over..."
  tags:
    - test-runner
```

### 十一、提交代码，查看GitLab Pipeline和sonarQube Web页面


### 十二、sonarScanner使用
- 下载sonarScanner
- 配置压缩包：/conf/sonar-scanner.properties 文件
```markdown
#----- Default SonarQube server
sonar.host.url=http://localhost:9000
#----- Default source code encoding
sonar.sourceEncoding=UTF-8
#项目的唯一标识，不能重复
sonar.projectKey=test-key
#项目的名字
sonar.projectName=test
sonar.java.binaries=target/classes
sonar.sources=src/main/java
sonar.gitlab.project_id=git@192.168.99.100:root/ucarbase.git
```
-  进入/bin目录，执行sonar-scanner.bat,将会在sonarqube web端查看到分析结果

### 十三、与jenkens集成
- jenkins中安装 gitlab-jenkins-plugin，在jenkins中配置token，获取构建路径url

### 十四、gitLab web hooks
- webhooks 配置jenkins构建路径的 url和token

### 十五、其它
- sonaQube中，如果需要配置排除，在菜单栏：配置>>通用设置>>排除 中配置
