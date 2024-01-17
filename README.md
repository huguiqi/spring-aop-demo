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

