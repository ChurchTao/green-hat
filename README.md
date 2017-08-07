

[![](http://115.159.181.84:9000/upload/2017/07/oe28fngd7sj7ep6pn6bua4f26m.png)](https://github.com/ChurchTao/green-hat)

[Quick Start]()&nbsp; | &nbsp;[Demo Project](https://github.com/ChurchTao/greenhat-demo)&nbsp; | &nbsp;[English]()


## What is green-hat ?

green-hat 是一款轻量级MVC框架。沿用Spring的注解风格(暂时也只能是注解...)，在学习spring原理的途中

想着自己造一个简单的轮子。于是就有了这个 `green-hat[Forgive Hat]`

如果你喜欢它请为它 [Star](https://github.com/ChurchTao/green-hat/stargazers) 谢谢

## 特性

* [x] MVC框架，不依赖更多的库
* [x] Spring注解风格
* [x] 支持html | jsp | json 输出
* [x] 名字奇葩
* [x] 正在完善，Bug多多
* [x] 已经支持 AOP
* [ ] 暂不支持 ORM 数据库这部分还在琢磨

## 概述

* 简洁的：框架设计简单,容易理解,不依赖于更多第三方库。你完全可以看懂框架里面的简单代码。
* 优雅的：`green-hat` 沿用Spring的注解风格，方便记忆。

## 快速入门 [demo](https://github.com/ChurchTao/greenhat-demo)

开始之前,首先 下载[源码](https://github.com/ChurchTao/green-hat),到根目录执行 `mvn install` 目的是把源码打包到本地maven仓库

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
app.base_package=com.test       项目包地址[必须]
app.jsp_path=/WEB-INF/view/     Jsp目录
app.asset_path=/asset/          资源目录
app.home_page=/index.html       主页
app.www_path=/www/              html页面地址
app.upload_limit=10             上传文件大小[默认10mb]
```

## API示例

```java
    @Autowired
    private UserService userService;  //自动注入

    @Mapping(value = "/index",method = RequestMethod.get)
    public View index(){
        //man= BeanHelper.getBean(Man.class);
        man.setName("church");
        DataContext.Session.put("loginUser",man);
        return new View("index.jsp");
    }
```

## REST URL参数获取

```java
    @Mapping(value = "/index2",method = RequestMethod.get)
    public Data index(Param param){
        userService.out();
        Man man2 = DataContext.Session.get("loginUser");
        man2.say();
        return new Data(param);
    }
```

## 上传文件 and 表单参数

```java
    @Mapping(value = "/upload",method = RequestMethod.post)
    public Data uploadFile(Param param, HttpServletRequest request){
        //获取表单数据
        Map<String,Object> formMap = param.getFieldMap();
        //获取上传文件
        FileParam fileParam = param.getFile("photo");
        //保存文件  -- uploadFile() 如果上传文件想存在项目目录内，请使用 2 或 3
        //1
        try {
            UploadHelper.uploadFile("/tmp/upload/",fileParam);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //2
        //UploadHelper.uploadFile(DataContext.getRequest().getSession().getServletContext().getRealPath("/tmp/upload/"),fileParam);
        //3
        //UploadHelper.uploadFile(request.getSession().getServletContext().getRealPath("/tmp/upload/"),fileParam);
        return new Data(fileParam.getFileName()+" "+fileParam.getFileSize());
    }
```

## 联系我

- Mail: swkzymlyy#gmail.com
