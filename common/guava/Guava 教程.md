教程： [Google Guava官方教程（中文版）](https://wizardforcel.gitbooks.io/guava-tutorial/content/1.html) 

# 一、简介

Guava 项目托管在 [Github](https://github.com/google/guava) 上，主要有下面的几个包：

- `com.google.common.annotations`：普通注解类型

- `com.google.common.base`：基本工具类库和接口

- `com.google.common.cache`：缓存工具包，非常简单易用且功能强大的 JVM 内缓存

- `com.google.common.collect`：带泛型的集合接口扩展和实现，以及工具类

- `com.google.common.escape`：转义工具

- `com.google.common.eventbus`：发布订阅风格的事件总线

- `com.google.common.graph`：处理基于图的数据结构

- `com.google.common.hash`： 哈希工具包

- `com.google.common.html`：Html 字符串转义

- `com.google.common.io`：I/O 工具包

- `com.google.common.math`：原始算术类型和超大数的运算工具包

- `com.google.common.net`：网络工具包

- `com.google.common.primitives`：八种原始类型和无符号类型的静态工具包

- `com.google.common.reflect`：反射工具包

- `com.google.common.util.concurrent`：多线程工具

- `com.google.common.xml`：xml 字符串转义

# 二、功能详解

## 1. 基本工具 [Basic utilities]

让使用 Java 语言变得更舒适：

- [使用和避免null](http://ifeve.com/using-and-avoiding-null/)：很多 Guava工具类用快速失败拒绝 null 值，而不是盲目地接受

- [前置条件](http://ifeve.com/google-guava-preconditions/)： 让方法中的条件检查更简单

- [常见Object方法](http://ifeve.com/google-guava-commonobjectutilities/)： 简化 Object 方法实现，如 hashCode()和toString()

- [排序: Guava强大的”流畅风格比较器”](http://ifeve.com/google-guava-ordering/)

- [Throwables](http://ifeve.com/google-guava-throwables/)： 简化了异常和错误的传播与检查

## 2. 集合[Collections]

Guava 对 JDK 集合的扩展：

- [不可变集合](http://ifeve.com/google-guava-immutablecollections/)： 用不变的集合进行防御性编程和性能提升

- [新集合类型](http://ifeve.com/google-guava-newcollectiontypes/)： multisets, multimaps, tables, bidirectional maps等

- [强大的集合工具类](http://ifeve.com/google-guava-collectionutilities/)： 提供 java.util.Collections 中没有的集合工具

- [扩展工具类](http://ifeve.com/google-guava-collectionhelpersexplained/)：让实现和扩展集合类变得更容易，比如创建 `Collection` 的装饰器，或实现迭代器

## 3. 缓存[Caches]

[Guava Cache](http://ifeve.com/google-guava-cachesexplained)：本地缓存实现，支持多种缓存过期策略

## 4. 函数式风格[Functional idioms]

[Guava的函数式](http://ifeve.com/google-guava-functional/)： 支持可以显著简化代码，但请**谨慎使用**

## 5. 并发[Concurrency]

强大而简单的抽象，让编写正确的并发代码更简单

- [ListenableFuture](http://ifeve.com/google-guava-listenablefuture/)：完成后触发回调的 Future

- [Service框架](http://ifeve.com/google-guava-serviceexplained/)：抽象可开启和关闭的服务，帮助维护服务的状态逻辑

## 6. 字符串处理[Strings]

[字符串工具](http://ifeve.com/google-guava-strings/)：包括分割、连接、填充等操作

## 7. 原生类型[Primitives]

扩展 JDK 未提供的[原生类型](http://ifeve.com/google-guava-primitives/)(如int、char)操作， 包括某些类型的无符号形式

## 8. 区间[Ranges]

可比较类型的[区间API](http://ifeve.com/google-guava-ranges/)，包括连续和离散类型

## 9. I/O

简化[I/O](http://ifeve.com/google-guava-io/)尤其是I/O流和文件的操作，针对Java5和6版本

## 10. 散列[Hash]

提供比`Object.hashCode()`更复杂的[散列实现](http://ifeve.com/google-guava-hashing/)，并提供布鲁姆过滤器的实现

## 11. 事件总线[EventBus]

[事件总线](http://ifeve.com/google-guava-eventbus/)： 发布-订阅模式的组件通信，但组件不需要显式地注册到其他组件中

## 12. 数学运算[Math]

[数学运算](http://ifeve.com/google-guava-math/)： 优化的、充分测试的数学工具类

## 13. 反射[Reflection]

[反射](http://ifeve.com/guava-reflection/)： Guava 的 Java 反射机制工具类