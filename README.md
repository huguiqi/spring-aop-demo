## springMvc-自定义切面

两种切面配置方式：
1. 注解方式
2. xml配置方式



自定义注解CheckSign：

```
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CheckSign {

    Class<? extends RequestCheckHandler> type();
    String[] orderKeys();

}

```

RequestCheckHandler.java 基类：

```
public interface RequestCheckHandler {

    void check(String sign, Object params, String [] sortKeys);
}

```


处理签名业务实现类SNTYSignHandler.java:

```
@Slf4j
public class SNTYSignHandler implements RequestCheckHandler {



    @Override
    public void check(String sign, Object params, String[] keys) {
//        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (StringUtils.isEmpty(sign)) {
            throw new RuntimeException("签名错误");
        }
        String jsonString = "";
        if (CollectionUtils.isEmpty(Arrays.asList(keys))){
            jsonString = JSONObject.toJSONString(params);
        }else {
            Map<String,Object> map = (Map<String, Object>) params;
            Map<String,Object> paramMap = new LinkedHashMap<>();
            Arrays.stream(keys).forEach(key->{
                Object value = map.get(key);
                if (Objects.nonNull(value)){
                    paramMap.put(key, value);
                }
            });
            jsonString = JSONObject.toJSONString(paramMap);
        }
        String tmpSign = Md5Util.MD5(jsonString + "zaxh6666");
        if (!Objects.equals(sign, tmpSign)) {
            throw new RuntimeException("签名错误");
        }
        log.debug("request sign is" + sign);


    }
}
```



## 注解方式



### xml配置

```
  <context:annotation-config/>
  
  <!--配置切面对象-->
    <bean id="checkSignAspect" class="com.example.demo.annotaion.aspect.CheckSignAspect"/>
  <!--开启自动代理,会自动读取aspect注解-->  
   <aop:aspectj-autoproxy />
   
```

### 相关类

CheckSignAspect.java:

```
@Component
@Aspect
public class CheckSignAspect {

    private static final Log LOGGER = LogFactory.getLog(CheckSignAspect.class);

    @Pointcut("@annotation(com.example.demo.annotaion.CheckSign)")
    public void pt() {

    }


    /**
     * before通知
     */
    @Before(value = "pt()")
    public void log(JoinPoint joinPoint) throws Throwable {
        long beginTime = System.currentTimeMillis();
        // 执行方法
        // check
        check(joinPoint, beginTime);
    }

    /***
     * Created with IntelliJ IDEA.
     * @Author: sam.hu (huguiqi@zaxh.com)
     * @version v1
     * @param joinPoint
     * @param time
     * @return void
     * @since: 2024/1/5 18:34
     * @summary: 规定controller的method的参数只能是1个或者两个，如果是只有一个参数，那必须是HttpServletRequest,如果是两个参数，则一个是sign,一个是bodyMap
     */
    private void check(JoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        CheckSign checkSign = method.getAnnotation(CheckSign.class);
        LOGGER.info("=====================checkSign--log-start:" + time + "================================");
        LOGGER.info("className:{}" + checkSign.type().getName());

        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        LOGGER.info("request method:" + className + "." + methodName + "()");

        // 请求的参数
        Object[] args = joinPoint.getArgs();
        String sign = "";
        Object paramObj = null;
        if (args.length == 2){
            Object arg1 = args[0];
            if (arg1 instanceof HttpServletRequest){
                HttpServletRequest request = (HttpServletRequest) arg1;
                sign = request.getHeader("sign");
            }else if (arg1 instanceof String){
                sign = (String)arg1;
            }

            String params = JSON.toJSONString(args[1]);
            paramObj = args[1];
            LOGGER.info("params:" + params);
        }else if (args.length == 1){
            paramObj = args[0];
            HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
            sign = request.getHeader("sign");
        }else {
            //todo 没有参数，参数数量大于2时，需要规定处理
        }

        Class checkClass = checkSign.type();
        RequestCheckHandler checkHandler = null;
        try {
            checkHandler = (RequestCheckHandler) checkClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            LOGGER.error(checkClass + ":InstantiationException error", e);
        } catch (IllegalAccessException e) {
            LOGGER.error(checkClass + ":IllegalAccessException error", e);
        } catch (InvocationTargetException e) {
            LOGGER.error(checkClass + ":InvocationTargetException error", e);
        } catch (NoSuchMethodException e) {
            LOGGER.error(checkClass + ":NoSuchMethodException error", e);
        }
        if (checkHandler != null) {
            //call
            checkHandler.check(sign,paramObj,checkSign.orderKeys());
        }
        LOGGER.info("=====================checkSign-log-end================================");
    }

    private Object parseReqParam(HttpServletRequest request) {
        //todo 后续有需要实现
        return null;
    }


}

```


## xml方式

xml配置：

```
<context:annotation-config/>

<!--配置切面对象-->
    <bean id="checkSignAspect" class="com.example.demo.annotaion.aspect.CheckSignAspect"/>
    <aop:config proxy-target-class="true">
        <!--声明切面-->
        <aop:aspect ref="checkSignAspect">

            <!--抽取切点表达式-->
            <aop:pointcut id="myPointcut"  expression="@annotation(com.example.demo.annotaion.CheckSign)"/>

            <!--调用切点表达式-->
            <aop:before method="log"  pointcut-ref="myPointcut"/>
        </aop:aspect>
    </aop:config>


```


CheckSignAspect.java:

```

@Component
public class CheckSignAspect {

    private static final Log LOGGER = LogFactory.getLog(CheckSignAspect.class);

public void pt() {

    }


/**
 * before通知
 */
public void log(JoinPoint joinPoint) throws Throwable {
long beginTime = System.currentTimeMillis();
// 执行方法
// check
check(joinPoint, beginTime);
}

    private void check(JoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        CheckSign checkSign = method.getAnnotation(CheckSign.class);
        LOGGER.info("=====================checkSign--log-start:" + time + "================================");
        LOGGER.info("className:{}" + checkSign.type().getName());

        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        LOGGER.info("request method:" + className + "." + methodName + "()");

        // 请求的参数
        Object[] args = joinPoint.getArgs();
        String sign = "";
        Object paramObj = null;
        if (args.length == 2){
            Object arg1 = args[0];
            if (arg1 instanceof HttpServletRequest){
                HttpServletRequest request = (HttpServletRequest) arg1;
                sign = request.getHeader("sign");
            }else if (arg1 instanceof String){
                sign = (String)arg1;
            }

            String params = JSON.toJSONString(args[1]);
            paramObj = args[1];
            LOGGER.info("params:" + params);
        }else if (args.length == 1){
            paramObj = args[0];
            HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
            sign = request.getHeader("sign");
        }else {
            //todo 没有参数，参数数量大于2时，需要规定处理
        }

        Class checkClass = checkSign.type();
        RequestCheckHandler checkHandler = null;
        try {
            checkHandler = (RequestCheckHandler) checkClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            LOGGER.error(checkClass + ":InstantiationException error", e);
        } catch (IllegalAccessException e) {
            LOGGER.error(checkClass + ":IllegalAccessException error", e);
        } catch (InvocationTargetException e) {
            LOGGER.error(checkClass + ":InvocationTargetException error", e);
        } catch (NoSuchMethodException e) {
            LOGGER.error(checkClass + ":NoSuchMethodException error", e);
        }
        if (checkHandler != null) {
            //call
            checkHandler.check(sign,paramObj,checkSign.orderKeys());
        }
        LOGGER.info("=====================checkSign-log-end================================");
    }

    private Object parseReqParam(HttpServletRequest request) {
        //todo 后续有需要实现
        return null;
    }


}

```


## 参考文档

https://docs.spring.io/spring-framework/docs/4.3.29.RELEASE/spring-framework-reference/html/


https://juejin.cn/post/7320457799946453055


https://juejin.cn/post/7088930138385023007


https://www.jianshu.com/p/0aca0d876319




## rabbitMQ添加连接池(非spring注入管理)






## 复用连接池的连接

如果想让线程池中的线程能够被重复利用,而不是每个任务各自独立运行,可以这么做:

使用无限队列,此时corePoolSize和maximumPoolSize相同:
java
``` 
int corePoolSize = 5;
int maximumPoolSize = 5;

ThreadPoolExecutor executor = new ThreadPoolExecutor(
corePoolSize,
maximumPoolSize,
0L,
TimeUnit.MILLISECONDS,
new LinkedBlockingQueue<>());

```

## 保持消费循环不退出,用于后台监听线程，常驻系统:
java
``` 
executor.submit(() -> {
while(true) {
// 消费消息
}
});

```

设置任务间隔时间:

java
```
try {
// 消费消息

Thread.sleep(1000); // 1秒
} catch (InterruptedException e) {}

```

拒绝新任务:
java

```
executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
这样每个线程就能形成一个无限循环,耗时任务完成后通过睡眠保持活跃,下一个任务直接分配到已经活跃的线程上运行,实现了线程复用。

最大限度利用了线程池中的有限线程,防止频繁创建销毁线程带来的性能开销。

```


## 输出效果

```
pool-1-thread-17 Consumer msg:hello,fuck you!!,第0次
pool-1-thread-3 Consumer msg:hello,fuck you!!,第1次
pool-1-thread-5 Consumer msg:hello,fuck you!!,第2次
pool-1-thread-7 Consumer msg:hello,fuck you!!,第3次
pool-1-thread-9 Consumer msg:hello,fuck you!!,第4次
pool-1-thread-11 Consumer msg:hello,fuck you!!,第5次
pool-1-thread-13 Consumer msg:hello,fuck you!!,第6次
pool-1-thread-15 Consumer msg:hello,fuck you!!,第7次
pool-1-thread-17 Consumer msg:hello,fuck you!!,第8次
pool-1-thread-3 Consumer msg:hello,fuck you!!,第9次
```


## RabbitMQ的Channel实例是否可以做成连接池？

RabbitMQ的Channel实例是不能作为连接池的。
每个Channel实例都是与RabbitMQ服务器的单独网络连接，它们是轻量级的通信通道，用于发送和接收消息。
相反，连接池通常用于管理连接到数据库或其他资源的连接。
连接池可以帮助提高应用程序的性能和可伸缩性，通过重用连接来减少连接的创建和销毁开销。

虽然Channel不能作成连接池，但是它可以绑定到线程中，作为私有属性进行复用，这样如果用线程作为消费者去消费时，就不需要不断创建Channel，从而消耗资源

