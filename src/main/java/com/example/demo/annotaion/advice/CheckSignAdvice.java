//package com.example.demo.annotaion.advice;
//
//import com.example.demo.annotaion.CheckSign;
//import org.apache.commons.logging.Log;
//import org.slf4j.LoggerFactory;
//import org.springframework.aop.MethodBeforeAdvice;
//
//import javax.servlet.http.HttpServletRequest;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//
///**
// * Created with IntelliJ IDEA.
// *
// * @version v1
// * @Author: sam.hu (huguiqi@zaxh.cn)
// * @Copyright (c) 2023, zaxh Group All Rights Reserved.
// * @since: 2023/12/26/14:34
// * @summary:
// */
//
//public class CheckSignAdvice implements MethodBeforeAdvice {
//
//    private static final Log LOGGER = LoggerFactory.getLogger(CheckSignAdvice.class));
//
//
//
//
//
//
//    private Object parseReqParam(HttpServletRequest request) {
//        //todo 后续有需要实现
//        return null;
//    }
//
//
//    @Override
//    public void before(Method method, Object[] objects, Object target) throws Throwable {
//
////        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
////        Method method = signature.getMethod();
//
//        CheckSign checkSign = method.getAnnotation(CheckSign.class);
//        LOGGER.info("=====================checkSign--log-start================================");
//        LOGGER.info("className:{}" + checkSign.type().getName());
//
//        // 请求的方法名
//        String className = target.getClass().getName();
//        String methodName = method.getName();
//        LOGGER.info("request method:" + className + "." + methodName + "()");
//        // 请求的参数
//        Object[] args = objects;
//        String sign = request.getHeader("sign");
//        Object paramObj = null;
//
//        if (args.length == 1) {
//            String params = JSON.toJSONString(args[0]);
//            paramObj = args[0];
//            LOGGER.info("params:" + params);
//        } else {
//            LOGGER.info("args.length:" + args.length);
//            if (args.length == 0) {
//                paramObj = this.parseReqParam(request);
//            }
//        }
//
//        Class checkClass = checkSign.type();
//        RequestCheckHandler checkHandler = null;
//        try {
//            checkHandler = (RequestCheckHandler) checkClass.getDeclaredConstructor().newInstance();
//        } catch (InstantiationException e) {
//            LOGGER.error(checkClass + ":InstantiationException error", e);
//        } catch (IllegalAccessException e) {
//            LOGGER.error(checkClass + ":IllegalAccessException error", e);
//        } catch (InvocationTargetException e) {
//            LOGGER.error(checkClass + ":InvocationTargetException error", e);
//        } catch (NoSuchMethodException e) {
//            LOGGER.error(checkClass + ":NoSuchMethodException error", e);
//        }
//        if (checkHandler != null) {
//            //call
//            checkHandler.check(sign, paramObj, checkSign.sortKeys());
//        }
//        LOGGER.info("=====================checkSign-log-end================================");
//    }
//}
