# GreenHat
一个 Java MVC框架轮子

使用方法：

1. 下载源码,到根目录执行 `mvn install` 目的是把源码打包到本地maven仓库

2. 新建maven工程，在pom.xml中加入，并点击import~
```xml
<dependencies>
    <dependency>
        <groupId>com.github.ChurchTao</groupId>
        <artifactId>GreenHat</artifactId>
        <version>1.0</version>
    </dependency>
</dependencies>
```

3.接下去在`main`下新建`webapp`，在`webapp`下新建`WEB-INF`，在`WEB-INF`下新建`web.xml`

4.出现一个小弹窗点击 ok ，相当于创建成功一个webapp项目。

[![](http://115.159.181.84:9000/upload/2017/07/oe28fngd7sj7ep6pn6bua4f26m.png)](https://github.com/ChurchTao/GreenHat)

[Quick Start]()&nbsp; | &nbsp;[Demo Project](https://github.com/ChurchTao/greenhat-demo)&nbsp; | &nbsp;[English]()


## What is GreenHat ?

GreenHat 是一款轻量级MVC框架。沿用Spring的注解风格(暂时也只能是注解...)，在学习spring原理的途中

想着自己造一个简单的轮子。于是就有了这个 `GreenHat[Forgive Hat]`

如果你喜欢它请为它 [Star](https://github.com/ChurchTao/GreenHat/stargazers) 谢谢

## 特性

* [x] MVC框架，不依赖更多的库
* [x] Spring注解风格
* [x] 支持html | jsp | json 输出
* [x] 名字奇葩
* [x] 正在完善，Bug多多
* [ ] 暂不支持 AOP
* [ ] 暂不支持 ORM 数据库这部分还在琢磨

## 概述

* 简洁的：框架设计简单,容易理解,不依赖于更多第三方库。你完全可以看懂框架里面的简单代码。
* 优雅的：`GreenHat` 沿用Spring的注解风格，方便记忆。

## 快速入门 [demo](https://github.com/ChurchTao/greenhat-demo)

开始之前,首先 下载[源码](https://github.com/ChurchTao/GreenHat),到根目录执行 `mvn install` 目的是把源码打包到本地maven仓库

因为它还没有资格进入Maven中央仓库。

新建maven工程，在pom.xml中加入`Maven` 配置：

```xml
<dependency>
    <groupId>com.github.ChurchTao</groupId>
    <artifactId>GreenHat</artifactId>
    <version>1.0</version>
</dependency>
```

接下来在resources目录下创建 `config.properties` 用来存放配置信息

```properties
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://127.0.0.1:3306/book
jdbc.username=root
jdbc.password=root
app.base_package=com.test       项目包地址
app.jsp_path=/WEB-INF/view/     Jsp目录
app.asset_path=/asset/          资源目录
app.home_page=/index.html       主页
app.www_path=/www/              html页面地址
```

## API示例

```java
    @Autowired
    private UserService userService;  //自动注入

    @Mapping(value = "get:/index")
    public View index(){
        DataContext.Session.put("loginUser",man);//操作session
        userService.out();
        return new View("index.jsp"); //返回视图 可addModel
    }
```

## REST URL参数获取

```java
 @Mapping(value = "get:/index2")
    public Data index(Param param){
        Man man2 = DataContext.Session.get("loginUser");
        return new Data(param); //把接受到的参数直接转成json输出 [当然你也可以自定义类型，都可以自动转成json]
    }
```

## 表单参数

暂无

## 上传文件

暂无

## 联系我

- Mail: swkzymlyy#gmail.com