package com.example.demo.annotaion.aspect;

import com.alibaba.fastjson.JSON;
import com.example.demo.annotaion.CheckSign;
import com.example.demo.annotaion.RequestCheckHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 *
 * @version v1
 * @Author: sam.hu (huguiqi@zaxh.cn)
 * @Copyright (c) 2023, zaxh Group All Rights Reserved.
 * @since: 2023/12/26/14:34
 * @summary:
 */

@Component
//@Aspect
public class CheckSignAspect {

    private static final Log LOGGER = LogFactory.getLog(CheckSignAspect.class);

//    @Pointcut("@annotation(com.example.demo.annotaion.CheckSign)")
    public void pt() {

    }


    /**
     * before通知
     */
//    @Before(value = "pt()")
    public void log(JoinPoint joinPoint) throws Throwable {
        long beginTime = System.currentTimeMillis();
        // 执行方法
        // check
        check(joinPoint, beginTime);
    }

    /***
     * Created with IntelliJ IDEA.
     * @Author: sam.hu (huguiqi@huazhu.com)
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
