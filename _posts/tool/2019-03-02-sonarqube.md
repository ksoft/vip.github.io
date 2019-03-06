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
![GitLabRunner](http://www.datuzi.vip/photos/gitlab-runner/gitlab-runner-1.png)

### 八、集成GiltLab Runner
- windows 安装：
    - 下载[[gitlab-runner-windows-386]](https://gitlab-runner-downloads.s3.amazonaws.com/latest/binaries/gitlab-runner-windows-386.exe)
    - 将下载的文件放于D:/gitRunner目录下，cmd执行以下命令，在当前目录会生成config.toml文件
    ```markdown
      cd D:/gitRunner
      gitlab-runner-windows-386.exe register
    ```
    - 按提示输入第七步的url和token，tag需要跟之后需要配置的.gitlab-ci.yml配置的tags一致，才能执行
    - 安装完在GitLab Runners里，可以看到如下信息：
    ![GitLabRunner](http://www.datuzi.vip/photos/gitlab-runner/gitlab-runner-2.png)
- linux 下安装

### 九、集成GitLab插件
- 在GitLab中，创建accessToken:
![GitLab](http://www.datuzi.vip/photos/gitlab/gitlab-accessToken.png)
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

### 十二、其它
- sonaQube中，如果需要配置排除，在菜单栏：配置>>通用设置>>排除 中配置
