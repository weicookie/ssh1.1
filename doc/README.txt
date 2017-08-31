说明：spring+springmvc+hibernate+mysql的整合

本项目框架采用的是的工具版本：
jdk：jdk-8u121-windows-x64
tomcat：apache-tomcat-8.5.15
spring：spring-framework-4.3.10.RELEASE
hibernate：hibernate-release-4.2.21.Final
数据源：c3p0-0.9.2.1
数据库：MySQL5.5
开发工具：ideaIU-15.0.3
如若修改，请根据实际情况进行修改
---------------------------------------------------------------------------------------------------------------
1. 所有的domain数据库对应的字段大写
2. 所有domain需要书写对应字段的静态常量，方便调用
3. 定时调度使用spring的@Scheduled，这里也可以通过表达式获取properties中的配置参数
4. 注意：虽然SysUserDao继承了BaseHibernateDao，拥有save、update等方法，
   但是如果想在dao中将这些方法作为连接点执行切面代码，则需要在相对应的dao中再重新定义，必须存在对应的方法，否则无法被aop拦截
   参考SysUserDaoAop和SysUserDao
5. dao中使用getCurrentSession()获取session，并且由spring管理事务，因此需要在spring配置文件中加上事务管理器
6. 在<form>标签的action属性中以.ac为后缀名可以访问对应的controller，如果controller中返回的是"forward:/template/hello.jsp"，则会跳转到对应的hello.jsp,地址栏地址不会变化，
    如果是"redirect:/template/hello.jsp",地址栏会变化。
    但是如果是使用js中访问，没有效果，返回的类型依然是application/json
    <form action="${webRoot}/user/findUserByUserId.ac">
        <input type="submit" value="提交"/>
    </form>
7. jackson版本
jackson-all-1.9.0.jar
jackson-annotations-2.8.8.jar
jackson-core-2.8.8.jar
jackson-databind-2.8.8.jar
8. 注意：rapid-framework-3.9.3.20100923.jar包已经是重新打包了，与网上下载的jar包不同
