# 一、依赖

```xml
<dependency>  
    <groupId>junit</groupId>  
    <artifactId>junit</artifactId>  
    <version>{version}</version>  
    <scope>test</scope>  
</dependency>   
<dependency>  
    <groupId>org.springframework</groupId>  
    <artifactId>spring-test</artifactId>  
    <version>{version}</version>  
    <scope>provided</scope>  
</dependency>   
```



# 二、引入

```java
@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration(locations = "classpath:spring/applicationContext.xml") //加载配置文件
```



# 三、demo

```java
@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration(locations = {"classpath:applicationContext.xml"}) //加载配置文件
public class UpmServiceTest {

    @Autowired
    private UpmServiceImpl upmService;

    @Test
    public void testUpmService() {
        System.out.println(upmService.getAppKey());
        System.out.println(upmService.getJetAppId());
        System.out.println(upmService.getUpmUrl());
        System.out.println(upmService.validAccess("123", "123"));
    }
}
```
