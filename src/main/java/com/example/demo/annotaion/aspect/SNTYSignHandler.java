package com.example.demo.annotaion.aspect;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.annotaion.RequestCheckHandler;
import com.example.demo.common.utils.Md5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 *
 * @version v1
 * @Author: sam.hu (huguiqi@zaxh.cn)
 * @Copyright (c) 2023, zaxh Group All Rights Reserved.
 * @since: 2023/12/26/14:07
 * @summary:
 */

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
