推荐阅读： 

- [jetty 官方中文翻译](https://www.orchome.com/58)
- [jetty 使用教程](https://www.cnblogs.com/yiwangzhibujian/category/876335.html) 

- [jetty 源码学习的整理](https://blog.csdn.net/fjs_cloud/category_1688475.html) 

# 一、配置文件详解

## 1、配置案例

(1) 嵌入式的 jetty 服务器**和配置在 Java 里**的例子：

```java
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
 
public class ExampleServer {
 
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.setConnectors(new Connector[] { connector });
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        context.addServlet(HelloServlet.class, "/hello");
        context.addServlet(AsyncEchoServlet.class, "/echo/*");
        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(new Handler[] { context, new DefaultHandler() });
        server.setHandler(handlers);
        server.start();
        server.join();
    }
}
```

(2) **在 XML 中实例化**和配置完全相同的服务器：

```xml
<?xml version="1.0"?>
<!DOCTYPE Configure PUBLIC "-//Jetty//Configure//EN" "https://www.eclipse.org/jetty/configure_9_3.dtd">
 
<Configure id="ExampleServer" class="org.eclipse.jetty.server.Server">
  <Set name="connectors">
    <Array type="org.eclipse.jetty.server.Connector">
      <Item>
        <New class="org.eclipse.jetty.server.ServerConnector">
          <Arg><Ref refid="ExampleServer"/></Arg>
          <Set name="port">8080</Set>
        </New>
      </Item>
    </Array>
  </Set>
 
  <New id="context" class="org.eclipse.jetty.servlet.ServletContextHandler">
    <Set name="contextPath">/hello</Set>
    <Call name="addServlet">
      <Arg>org.eclipse.jetty.embedded.HelloServlet</Arg>
      <Arg>/</Arg>
    </Call>
  </New>
 
  <Set name="handler">
    <New class="org.eclipse.jetty.server.handler.HandlerCollection">
      <Set name="handlers">
        <Array type="org.eclipse.jetty.server.Handler">
          <Item>
            <Ref refid="context" />
          </Item>
          <Item>
            <New class="org.eclipse.jetty.server.handler.DefaultHandler" />
          </Item>
        </Array>
      </Set>
    </New>
  </Set>
</Configure>
```

## 2、配置详解

参考： [jetty配置文件详解](https://blog.csdn.net/fjslovejhl/article/details/15501091)

### (1) Configure 标签

`Configure` 是整个配置文件的 root 元素，程序会根据它采用默认构造函数创建一个 server 对象

```xml
<Configure id="Server" class="org.mortbay.jetty.Server">
```

### (2) Set 与 New 标签

```xml
<Set name="ThreadPool">
		<New class="org.mortbay.thread.QueuedThreadPool">
    		<Set name="minThreads">10</Set>
    		<Set name="maxThreads">200</Set>
    		<Set name="lowThreads">20</Set>
    		<Set name="SpawnOrShrinkAt">2</Set>
  	</New>

  	<!-- Optional Java 5 bounded threadpool with job queue 
  	<New class="org.mortbay.thread.concurrent.ThreadPool">
    		<Set name="corePoolSize">50</Set>
    		<Set name="maximumPoolSize">50</Set>
  	</New>
  	-->
</Set>
```

**`Set` 标签**：**用于设置参数**，即调用 server 的 `set` 方法

- name 属性的值为 ThreadPool，就是调用 server 对象的 setThreadPool 方法

  对应 java 代码如下： 

  ```java
  //为当前的server设置运行时候的线程池
  public void setThreadPool(ThreadPool threadPool) {
      _container.update(this,_threadPool,threadPool, "threadpool",true);
      _threadPool = threadPool;
  }
  ```

---

**`New` 标签**： 用于创建对象

- 首先用于创建 `org.mortbay.thread.QueuedThreadPool` 对象
- 然后又调用 `Set` 方法，为 threadPool 对象设置参数

### (3) Call 标签

```xml
<Call name="addConnector">
   <Arg>
      <New class="org.mortbay.jetty.nio.SelectChannelConnector">
         <Set name="host"><SystemProperty name="jetty.host" /></Set>
         <Set name="port"><SystemProperty name="jetty.port" default="8080"/></Set>
         <Set name="maxIdleTime">30000</Set>
         <Set name="Acceptors">2</Set>
         <Set name="statsOn">false</Set>
         <Set name="confidentialPort">8443</Set>
         <Set name="lowResourcesConnections">5000</Set>
         <Set name="lowResourcesMaxIdleTime">5000</Set>
      </New>
   </Arg>
</Call>

<!-- Use this connector if NIO is not available. 
<Call name="addConnector">
  <Arg>
      <New class="org.mortbay.jetty.bio.SocketConnector">
        <Set name="port">8081</Set>
        <Set name="maxIdleTime">50000</Set>
        <Set name="lowResourceMaxIdleTime">1500</Set>
      </New>
  </Arg>
</Call>
-->
```

**`Call` 标签**：调用 addConnector 方法，为 Server 对象添加一个 Connector

### (4) Array 标签

```xml
<Set name="handler">
  	<New id="Handlers" class="org.mortbay.jetty.handler.HandlerCollection">
    		<Set name="handlers">
     				<Array type="org.mortbay.jetty.Handler">
       					<Item>
         						<New id="Contexts" class="org.mortbay.jetty.handler.ContextHandlerCollection"/>
       					</Item>
       					<Item>
         						<New id="DefaultHandler" class="org.mortbay.jetty.handler.DefaultHandler"/>
       					</Item>
       					<Item>
         						<New id="RequestLog" class="org.mortbay.jetty.handler.RequestLogHandler"/>
       					</Item>
     				</Array>
    		</Set>
  	</New>
</Set>
```

- 调用 Server 的 setHandler 方法为当前的 Server 设置 handler 属性

# 二、部署 web 应用的 XML 配置

默认安装的 jetty，会自动扫描它的 `$JETTY_HOME/webapps` 目录，若要部署web应用，则需将文件放在该目录中即可

部署描述符文件本身就是一个配置了 `WebAppContext类` 的 XML 文件，在默认安装的情况下，只需设置2个属性：

- `war`： web 应用程序的文件系统路径(或目录)
- `contextPath`： 上下文路径使用的 web 应用程序

---

比如：一个描述符文件，项目 `/opt/myapp/myapp.war` 指定上下文路径为 `/wiki`：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE Configure PUBLIC "-//Jetty//Configure//EN" "https://www.eclipse.org/jetty/configure_9_0.dtd">

<Configure class="org.eclipse.jetty.webapp.WebAppContext">
  <Set name="contextPath">/wiki</Set>
  <Set name="war">/opt/myapp/myapp.war</Set>
</Configure>
```

可以使用系统属性和属性元素，若设置系统属性 `myapp.home=/opt/myapp`，可以重写前面的例子：

> 如果需要修改 home 路径，可以简单的改变系统属性，如果经常为一个 app 在多版本间切换，这就非常有用

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE Configure PUBLIC "-//Jetty//Configure//EN" "https://www.eclipse.org/jetty/configure_9_0.dtd">

<Configure class="org.eclipse.jetty.webapp.WebAppContext">
  <Set name="contextPath">/wiki</Set>
  <Set name="war"><SystemProperty name="myapp.home"/>/myapp.war</Set>
</Configure>
```

---

**例子一**：告诉 Jetty，在部署时不展开 WAR 文件，让大家知道，不应该更改这个临时解压的 WAR，因为这种变化不持续，因此不适用于 web 应用程序部署

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE Configure PUBLIC "-//Jetty//Configure//EN" "https://www.eclipse.org/jetty/configure_9_0.dtd">

<Configure class="org.eclipse.jetty.webapp.WebAppContext">
  <Set name="contextPath">/wiki</Set>
  <Set name="war"><SystemProperty name="myapp.home"/>/myapp.war</Set>
  <!-- 该处配置 -->
  <Set name="extractWAR">false</Set> 
</Configure>
```

---

**例子二**： 检索 Java EE Servlet 的上下文，并设置其初始化参数

> 还可以使用 setAttribute 方法来设置一个 Servlet 上下文属性。但由于Web 应用程序的 web.xml 文件中的部署描述符之后处理，所以 **web.xml 值会覆盖同名的描述符属性**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE Configure PUBLIC "-//Jetty//Configure//EN" "https://www.eclipse.org/jetty/configure_9_0.dtd">

<Configure class="org.eclipse.jetty.webapp.WebAppContext">
  <Set name="contextPath">/wiki</Set>
  <Set name="war"><SystemProperty name="myapp.home"/>/myapp.war</Set>
  <!-- 该处配置 -->
  <Get name="ServletContext">
     <Call name="setInitParameter">
       <Arg>myapp.config</Arg>
       <Arg><SystemProperty name="myapp.home">/config/app-config.xml</Arg>
    </Call>
  </Get>
</Configure>
```

---

**例子三**： 设置一个特殊的 web.xml 覆盖描述符，该描述符是在 web 应用程序的 web.xml 之后处理的，因此它可以覆盖相同名称的属性

如果想添加的参数或附加的 Servlet 映射没有破开一个打包 WAR 文件，那此功能非常有用

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE Configure PUBLIC "-//Jetty//Configure//EN" "https://www.eclipse.org/jetty/configure_9_0.dtd">

<Configure class="org.eclipse.jetty.webapp.WebAppContext">
  <Set name="contextPath">/wiki</Set>
  <Set name="war"><SystemProperty name="myapp.home"/>/myapp.war</Set>
  <!-- 该处配置 -->
  <Set name="overrideDescriptor">/opt/myapp/overlay-web.xml</Set>
</Configure>
```

---

**例子四**： 该例子不仅是 web 应用的上下文配置，又是一个数据库连接池

> 参见数据源的例子，如果 web.xml 不包含这个数据源的引用，可以使用覆盖描述符的机制(前面的列子)，则应用程序就可以使用

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE Configure PUBLIC "-//Jetty//Configure//EN" "https://www.eclipse.org/jetty/configure_9_0.dtd">

<Configure class="org.eclipse.jetty.webapp.WebAppContext">
  <Set name="contextPath">/wiki</Set>
  <Set name="war"><SystemProperty name="myapp.home"/>/myapp.war</Set>
	<!-- 该处配置 -->
  <New id="DSTest" class="org.eclipse.jetty.plus.jndi.Resource">
    <Arg></Arg>
    <Arg>jdbc/DSTest</Arg>
    <Arg>
      <New class="org.apache.commons.dbcp.BasicDataSource">
        <Set name="driverClassName">org.some.Driver</Set>
        <Set name="url">jdbc.url</Set>
        <Set name="username">jdbc.user</Set>
        <Set name="password">jdbc.pass</Set>
      </New>
    </Arg>
  </New>
</Configure>
```

# 三、WebAppContexts 的部署处理

## 1、类配置

**默认配置类**： 

|                            配置类                            | 说明                                                         |
| :----------------------------------------------------------: | :----------------------------------------------------------- |
| [org.eclipse.jetty.webapp.WebInfConfiguration](https://download.eclipse.org/jetty/stable-9/apidocs/org/eclipse/jetty/webapp/WebInfConfiguration.html) | 提取 war, jars 和 定义的类路径                               |
| [org.eclipse.jetty.webapp.WebXmlConfiguration](https://download.eclipse.org/jetty/stable-9/apidocs/org/eclipse/jetty/webapp/WebXmlConfiguration.html) | 处理 WEB-INF/web.xml 文件                                    |
| [org.eclipse.jetty.webapp.MetaInfConfiguration](https://download.eclipse.org/jetty/stable-9/apidocs/org/eclipse/jetty/webapp/MetaInfConfiguration.html) | 寻找容器和 web 应用中的 jar，为 META-INF/resources 和 META-INF/web-fragment.xml |
| [org.eclipse.jetty.webapp.FragmentConfiguration](https://download.eclipse.org/jetty/stable-9/apidocs/org/eclipse/jetty/webapp/FragmentConfiguration.html) | 处理所有找到的 META-INF/web-fragment.xml 文件                |
| [org.eclipse.jetty.webapp.JettyWebXmlConfiguration](https://download.eclipse.org/jetty/stable-9/apidocs/org/eclipse/jetty/webapp/JettyWebXmlConfiguration.html) | 处理 WEB-INF/jetty-web.xml 文件                              |

## 2、解剖配置类

WebAppContext 的5种不同的阶段的生命周期：

- `preConfigure`：WebAppContext 启动前需要执行的阶段，任何在其后的阶段，都将用到该资源

- `configure`：该阶段是类的工作，通常使用的资源在 preConfigure 阶段发现

- `postConfigure`： 该阶段允许配置明确创建的任何资源(可能是在前面的两个阶段不需要)

- `deconfigure`： 该阶段每当发生 WebAppContext 时停止，并允许撤销任何创建的资源/元数据

  > WebAppContext 应该能够不占用资源的启动/停止多次

- `destroy`： 该阶段说明 WebAppContext 从服务中移除

> `preConfigure()` 将调用 `WebInfConfiguration、WebXmlConfiguration、MetaInfConfiguration、FragmentConfiguration、JettyWebXmlConfiguration`，然后循环执行 `configure()、postConfigure()、deconfigure()、destroy()`

## 3、通过创建额外配置扩展容器支持

> jetty 利用了配置的灵活性，使 JNDI 和注释支持可插拔

### (1) 直接在 WebAppContext 设置列表

**添加 JNDI 和注释的配置的示例**：

```xml
<?xml version="1.0"?>
<!DOCTYPE Configure PUBLIC "-//Jetty//Configure//EN" "https://www.eclipse.org/jetty/configure_9_0.dtd">
 
<Configure class="org.eclipse.jetty.webapp.WebAppContext">
  <Set name="war"><SystemProperty name="jetty.home" default="."/>/webapps/my-cool-webapp</Set>
  <Set name="configurationClasses">
    <Array type="java.lang.String">
      <Item>org.eclipse.jetty.webapp.WebInfConfiguration</Item>
      <Item>org.eclipse.jetty.webapp.WebXmlConfiguration</Item>
      <Item>org.eclipse.jetty.webapp.MetaInfConfiguration</Item>
      <Item>org.eclipse.jetty.webapp.FragmentConfiguration</Item>
      <Item>org.eclipse.jetty.plus.webapp.EnvConfiguration</Item>
      <Item>org.eclipse.jetty.plus.webapp.PlusConfiguration</Item>
      <Item>org.eclipse.jetty.annotations.AnnotationConfiguration</Item>
      <Item>org.eclipse.jetty.webapp.JettyWebXmlConfiguration</Item>
    </Array>
  </Set>
</Configure>
```

### (2) 设置列表部署所有的webapps

设置配置类的列表上的 WebAppProvider，这样将应用于每个部署人员部署的 WebAppContext：

```xml
<?xml version="1.0"?>
<!DOCTYPE Configure PUBLIC "-//Jetty//Configure//EN" "https://www.eclipse.org/jetty/configure_9_0.dtd">
 
<Configure id="Server" class="org.eclipse.jetty.server.Server">
  <Call name="addBean">
    <Arg>
      <New id="DeploymentManager" class="org.eclipse.jetty.deploy.DeploymentManager">
        <Set name="contexts">
          <Ref refid="Contexts" />
        </Set>
        <Call id="webappprovider" name="addAppProvider">
          <Arg>
            <New class="org.eclipse.jetty.deploy.providers.WebAppProvider">
              <Set name="monitoredDirName"><Property name="jetty.home" default="." />/webapps</Set>
              <Set name="configurationClasses">
                <Array type="java.lang.String">
                  <Item>org.eclipse.jetty.webapp.WebInfConfiguration</Item>
                  <Item>org.eclipse.jetty.webapp.WebXmlConfiguration</Item>
                  <Item>org.eclipse.jetty.webapp.MetaInfConfiguration</Item>
                  <Item>org.eclipse.jetty.webapp.FragmentConfiguration</Item>
                  <Item>org.eclipse.jetty.plus.webapp.EnvConfiguration</Item>
                  <Item>org.eclipse.jetty.plus.webapp.PlusConfiguration</Item>
                  <Item>org.eclipse.jetty.annotations.AnnotationConfiguration</Item>
                  <Item>org.eclipse.jetty.webapp.JettyWebXmlConfiguration</Item>
                </Array>
              </Set>
            </New>
          </Arg>
        </Call>
      </New>
    </Arg>
  </Call>
</Configure>
```

---

不用列举完整的列表，可以简单地指定想要添加的类：

```xml
<?xml version="1.0"?>
<!DOCTYPE Configure PUBLIC "-//Jetty//Configure//EN" "https://www.eclipse.org/jetty/configure_9_0.dtd">
 
<Configure id="Server" class="org.eclipse.jetty.server.Server">
  <!-- =========================================================== -->
  <!-- Add plus Configuring classes to all webapps for this Server -->
  <!-- =========================================================== -->
  <Call class="org.eclipse.jetty.webapp.Configuration$ClassList" name="setServerDefault">
    <Arg><Ref refid="Server" /></Arg>
    <Call name="addAfter">
      <Arg name="afterClass">org.eclipse.jetty.webapp.FragmentConfiguration</Arg>
      <Arg>
        <Array type="String">
          <Item>org.eclipse.jetty.plus.webapp.EnvConfiguration</Item>
          <Item>org.eclipse.jetty.plus.webapp.PlusConfiguration</Item>
        </Array>
      </Arg>
    </Call>
  </Call>
</Configure>
```

`org.eclipse.jetty.webapp.Configuration.ClassList` 类提供这些方法插入：

- `addAfter`： 在给定的**配置类名称后**插入提供的配置类名称的列表
- `addBefore`：在给定**配置类名称之前**插入配置类名称提供的列表

### (3) 其他配置

`org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern`： 这是一个上下文属性，可以设置在 WebAppContext 去控制容器的类路径的哪些部分应该处理注释、META-INF/resources, META-INF/web-fragment.xml和META-INF里的 tld

```xml
<?xml version="1.0"?>
<!DOCTYPE Configure PUBLIC "-//Jetty//Configure//EN" "https://www.eclipse.org/jetty/configure_9_0.dtd">
 
<Configure class="org.eclipse.jetty.webapp.WebAppContext">
    <Call name="setAttribute">
      <Arg>org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern</Arg>
      <Arg>.*/foo-[^/]*\.jar$|.*/bar-[^/]*\.jar$|.*/classes/.*</Arg>
    </Call>
</Configure>
```

---

`org.eclipse.jetty.server.webapp.WebInfIncludeJarPattern`： 该属性控制处理 jar，像注释、META-INF/resources、META-INF/web-fragment.xml 和 META-INF 数据的 tld

```xml
<?xml version="1.0"?>
<!DOCTYPE Configure PUBLIC "-//Jetty//Configure//EN" "https://www.eclipse.org/jetty/configure_9_0.dtd">
 
<Configure class="org.eclipse.jetty.webapp.WebAppContext">
    <Call name="setAttribute">
      <Arg>org.eclipse.jetty.server.webapp.WebInfIncludeJarPattern</Arg>
      <Arg>.*/spring-[^/]*\.jar$</Arg>
    </Call>
</Configure>
```

# 四、静态部署与热部署

## 1、静态部署

如果需要一个纯粹的静态内容，可以用下面的方法：只需在 `${jetty.home}/webapps` 目录中创建一个 `scratch.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE Configure PUBLIC "-//Jetty//Configure//EN" "https://www.eclipse.org/jetty/configure_9_0.dtd">
<Configure class="org.eclipse.jetty.server.handler.ContextHandler">
  <Set name="contextPath">/scratch</Set>
  <Set name="handler">
    <New class="org.eclipse.jetty.server.handler.ResourceHandler">
        <Set name="resourceBase">/home/jesse/scratch</Set>
      <Set name="directoriesListed">true</Set>
    </New>
  </Set>
</Configure>
```

## 2、热部署

- **热部署**： jetty 通过监测目录变化来部署上下文或 web 应用程序

  若向目录中添加一个 web 应用程序或上下文描述符，jetty的 `DeploymentManager(DM)` 将部署新的上下文：

  - 若 touch 或更新上下文描述符，DM 停止、 重新配置和重新部署其上下文
  - 若删除一个上下文，DM 停止它，并从服务器上删除

- **实现**： 通过配置 `WebAppProvider` 属性来控制这种行为
  - `monitoredDirName`： 该目录扫描可能部署的 web 应用(或部署描述符的 XML 文件)
  - `scanInterval`： 提供 monitoredDirName 扫描之间的秒数，值为 0 将禁用连续热部署扫描，只在启动时部署 Web 应用程序

该配置的默认位置在 `${jetty.home}/etc/jetty-deploy.xml`： 

```xml
<?xml version="1.0"?>
<!DOCTYPE Configure PUBLIC "-//Jetty//Configure//EN" "https://www.eclipse.org/jetty/configure_9_0.dtd">
<Configure id="Server" class="org.eclipse.jetty.server.Server">

  <Call name="addBean">
    <Arg>
      <New id="DeploymentManager" class="org.eclipse.jetty.deploy.DeploymentManager">
        <Set name="contexts">
          <Ref refid="Contexts" />
        </Set>
        <Call name="setContextAttribute">
          <Arg>org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern</Arg>
          <Arg>.*/servlet-api-[^/]*\.jar$</Arg>
        </Call>

        <Call id="webappprovider" name="addAppProvider">
          <Arg>
            <New class="org.eclipse.jetty.deploy.providers.WebAppProvider">
              <Set name="monitoredDirName"><Property name="jetty.home" default="." />/webapps</Set>
              <Set name="defaultsDescriptor">
                	<Property name="jetty.home" default="." />/etc/webdefault.xml</Set>
              <Set name="scanInterval">1</Set>
              <Set name="extractWars">true</Set>
            </New>
          </Arg>
        </Call>
      </New>
    </Arg>
  </Call>
</Configure>
```

# 五、附录

## 1、简单配置虚拟目录

简单在 jetty 里配置虚拟目录，可以放图片，静态页面等文件：

```xml
<Set name="handler">    
   <New id="Handlers" class="org.mortbay.jetty.handler.HandlerCollection">    
    <Set name="handlers">    
      <Array type="org.mortbay.jetty.Handler">    
        <Item>    
          <New id="RequestLog" class="org.mortbay.jetty.handler.RequestLogHandler"/>    
        </Item>    
        <Item>    
      <New class="org.mortbay.jetty.webapp.WebAppContext">    
        <Set name="contextPath">/test</Set>    
        <Set name="resourceBase">D:/system/system/WebRoot</Set>    
        <Call name="addServlet">    
          <Arg>org.mortbay.jetty.servlet.DefaultServlet</Arg>    
          <Arg>/test</Arg>    
        </Call>    
      </New>    
    </Item>    
      </Array>    
    </Set>    
  </New>    
</Set>
```

## 2、jetty 与 tomcat 对比

1. **架构比较**
   - Jetty 的架构比 Tomcat 更简单
   - Jetty 的架构基于 Handler 实现，主要的扩展功能都可以用 Handler 来实现，扩展简单
   - Tomcat 的架构基于容器设计，进行扩展是需要了解 Tomcat 的整体设计结构，不易扩展

2. **性能比较**： Jetty 和 Tomcat 性能方面差异不大
   - 擅长场景： 
     - Jetty 可以同时处理大量连接而且可以长时间保持连接，适合于 web 聊天应用等
     - Tomcat 适合处理少数非常繁忙的链接，因此对于连接生命周期短的应用，Tomcat 的总体性能更高
   - IO 处理性能：
     - Tomcat 默认采用 BIO 处理 I/O 请求，在处理静态资源时，性能较差
     - Jetty 默认采用 NIO，因此在处理 I/O 请求上更占优势，在处理静态资源时，性能较高
   - Jetty 作为服务器，可以按需加载组件，减少不需要的组件，减少了服务器内存开销，从而提高服务器性能

3. **其它比较**
   - Jetty 的应用更加快速，修改简单，对新的 Servlet 规范的支持较好
   - Tomcat 目前应用比较广泛，对 JavaEE 和 Servlet 的支持更加全面，很多特性会直接集成进来

## 3、Jetty 常用命令

(1) **启动命令**： 

```shell
java -jar start.jar

## 指定项目名称启动:
java -jar start.jar -Dname=zoush

##保持jetty后台启动命令, 在命令后+ 
nohup java -jar start.jar＆     

## 指定项目名称启动:   
java -jar start.jar -Dname=zoush &

##设置JVM内存运行
java -Xms1024m -Xmx2048m -XX:PermSize=128M -XX:MaxPermSize=1024m -jar start.jar

##如想启用远程调试（端口8000）
java -Xms1024m -Xmx2048m -XX:PermSize=128M -XX:MaxPermSize=1024m -Xdebug 
		 -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n -jar start.jar
```

(2) **设置密码**： 

```shell
##启动命令：
java -DSTOP.PORT=8081 -DSTOP.KEY=123 -jar start.jar

##输入关闭命令：
java -DSTOP.PORT=8081 -DSTOP.KEY=123 -jar start.jar --stop 
```

(3) **`${JETTY_HOME}/bin` 目录下的命令**：

```shell
./jetty.sh start 启动项目
./jetty.sh stop 关闭
./jetty.sh restart 进行重启
```