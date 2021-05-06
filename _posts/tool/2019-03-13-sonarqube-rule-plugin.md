---
layout: post
title: sonarqube 自定义规则
category: 工具
tags: sonarQube
description: sonarqube 自定义规则
---

### 创建pom依赖

### 创建插件类 XxxJavaRulesPlugin

### 创建 XxxJavaRulesDefinition

### 创建 XxxJavaFileCheckRegistrar

### 创建 RulesList

### 创建规则类 VariableMinLengthCheck

### 创建（目录必须为：resources下：org.sonar.l10n.java.rules.squid）
- ControllerClassNameCheck_java.html
- ControllerClassNameCheck_java.json

### 具体代码参照：[[测试代码]](http://www.datuzi.cn/softs/sonarrule.rar)

### 编写完后，执行maven命令：clean install -DskipTests=true  sonar-packaging:sonar-plugin

### 将生成的jar包复制到sonarqube安装目录/extensions/plugins/目录下，重启sonarqube生效

### json文件，状态说明：
```markdown
type 类型:
  BUG BUG
  VULNERABILITY 漏洞
  CODE_SMELL 坏味道

defaultSeverity 默认问题验证性：
    INFO,提示
    MINOR,次要
    MAJOR,主要
    CRITICAL,严重
    BLOCKER; 阻断
```
