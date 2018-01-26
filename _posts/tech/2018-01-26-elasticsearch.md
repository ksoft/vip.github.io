---
layout: post
title: ElasticSearch 学习 - 查询
category: 技术
tags: ElasticSearch
keywords: ElasticSearch
---

## 1.term 过滤
term主要用于精确匹配哪些值，比如数字，日期，布尔值或 not_analyzed 的字符串(未经分析的文本数据类型)： 
```
{ "term": { "name":    "java"      }} 
{ "term": { "date":   "2014-09-01" }} 
```
完整的例子， name 字段完全匹配成 java 的数据：
```
{ 
  "query": { 
    "term": { 
      "name": "java" 
    } 
  } 
}
```
## 2.terms 过滤
terms 跟 term 有点类似，但 terms 允许指定多个匹配条件。 如果某个字段指定了多个值，那么文档需要一起去做匹配：
```
{ 
    "terms": { 
        "tag": [ "search", "full_text", "nosql" ] 
        } 
}
```
完整的例子，所有http的状态是 302 、304 的， 由于ES中状态是数字类型的字段，所有这里我们可以直接这么写。：
```
{ 
  "query": { 
    "terms": { 
      "status": [ 
        304, 
        302 
      ] 
    } 
  } 
}
```
