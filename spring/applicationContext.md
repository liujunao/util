配置模版：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    	 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
       xmlns:context="http://www.springframework.org/schema/context"
    	 xmlns:aop="http://www.springframework.org/schema/aop"
   		 xsi:schemaLocation="
    http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-4.0.xsd
    http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.0.xsd">
    
    <!-- 引入db.properties -->
    <context:property-placeholder location="classpath:db.properties" />

    <!-- 配置C3P0数据源 -->
    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="jdbcUrl" value="${jdbc.url}"></property>
        <property name="driverClass" value="${jdbc.driverName}"></property>
        <property name="user" value="${jdbc.username}"></property>
        <property name="password" value="${jdbc.pwd}"></property>
    </bean>

    <!-- 配置 Spring 的 org.springframework.jdbc.core.JdbcTemplate -->
    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="dataSource"></property>
    </bean>

    <!-- 测试 SpEL: 可以为属性进行动态的赋值(了解) -->
    <bean id="girl" class="com.helloworld.User">
        <property name="userName" value="周迅"></property>
    </bean>
  
  	<!--context:component-scan 指定扫描的包 -->
    <!-- resource-pattern 的含义： 只扫描 base-package 对应包下的目录为 myrepository 的所有java Bean -->
    <context:component-scan base-package="imooc_spring.test.anotation"
        										resource-pattern="myrepository/*.class"></context:component-scan>

    <context:exclude-filter type="assignable" expression="imooc_spring.test.anotation.TestObj"/>

    <context:component-scan base-package="com.aop"></context:component-scan>

    <!-- aop测试,需要引入aop命名空间 -->
    <aop:aspectj-autoproxy></aop:aspectj-autoproxy>
  
    <!-- aop xmlType，用xml的形式配置AOP前置通知 -->
    <aop:config>
        <!--aop:pointcut 其实放在这儿也可以 -->
        <aop:pointcut expression="execution (* com.aop.xmltype.CalculatorImplxml.*(..))" id="pointcut1" />

        <!-- 配置切面和通知 ，aop:aspect标签需要通过ref指定配置好的bean，id随便配置或者不配置，id的值可以随意起 -->
        <aop:aspect id="myaspxml" ref="myaspxml" order="2">
            <!-- 配置切点，切点不需要与对应的 bean 关联，只要所在的类配置了bean就可以 -->
            <aop:pointcut expression="execution (* com.aop.xmltype.CalculatorImplxml.*(..))" 
                          id="pointcut1" />
            <!-- 切面里的具体的用于记录的方法就是一个通知，需要用通过pointcut-ref来指定具体的切点 -->
            <aop:before method="logBefore" pointcut-ref="pointcut1" />
            <aop:after method="logAfter" pointcut-ref="pointcut1" />
        </aop:aspect>
    </aop:config>
  
    <!-- autowired测试，自动装配测试 -->
    <bean id="people" class="test.spring.autowired.Person" scope="prototype"
        autowire="byName">
        <property name="name" value="小明"></property>
        <!-- <property name="cat" ref="cat222"></property> -->
        <!-- <property name="cat" ref="cat1"></property> -->
    </bean>

</beans>
```

