---
layout: post
title: springboot maven profile 配置
category: 技术
tags: springboot maven profile
keywords: springboot maven profile
---

## maven添加profile
```markdown
<profiles>
        <profile>
            <id>dev</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <properties>
                <profileActive>dev</profileActive>
            </properties>
        </profile>
        <profile>
            <id>test</id>
            <properties>
                <profileActive>test</profileActive>
            </properties>
        </profile>
        <profile>
            <id>uat</id>
            <properties>
                <profileActive>uat</profileActive>
            </properties>
        </profile>
        <profile>
            <id>prod</id>
            <properties>
                <profileActive>prod</profileActive>
            </properties>
        </profile>
    </profiles>
```
## sprinboot 配置

### application.yml配置如下:
```markdown
spring:
  profiles:
    active: @profileActive@
```

### application.properties配置如下:
```markdown
spring.profiles.active=@profileActive@
```
