package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @version v1
 * @Author: sam.hu (huguiqi@zaxh.cn)
 * @Copyright (c) 2023, zaxh Group All Rights Reserved.
 * @since: 2023/12/20/15:09
 * @summary:
 */
public class EncodeTest {


    @Test
    public void testEncodeUrl() throws UnsupportedEncodingException, JsonProcessingException {
        String baseUrl = "http://192.168.0.144:8099/seeyon/rest/detailPage/h5";
        String parmaLink = "tempCode=QZ-JD-01&dataId=-6330374241148056512";

//        Map<String,Object> parmaMap = new HashMap<>();
//        parmaMap.put("tempCode",URLEncoder.encode("QZ-JD-01","UTF-8"));
//        parmaMap.put("dataId",URLEncoder.encode("-6330374241148056512","UTF-8"));

//        ObjectMapper mapper = new ObjectMapper();
//        String json = mapper.writeValueAsString(parmaMap);
//        System.out.println(json);
//        System.out.println(parmaMap);
        String encodeUrl = URLEncoder.encode(parmaLink, "UTF-8");

        System.out.println(baseUrl+"?"+encodeUrl);
    }


    @Test
    public void testInstanseLong(){
        Object checkSurvey = "-123443.4544 ";
        System.out.println(checkSurvey);
    }


    @Test
    public void testSign(){
        JSONObject jsonObject = new JSONObject(Boolean.TRUE);
        jsonObject.put("recordId", 2328);
        jsonObject.put("taskDetailId", 97);
//        jsonObject.put("result", 0);
        jsonObject.put("checkSurvey", "666");
        String jsonString = JSON.toJSONString(jsonObject);
        String salt = "zaxh6666";
        String tmpSign = this.MD5(jsonString + salt);
        System.out.println(tmpSign);
    }





    @Test
    public void testSign2(){

        Map<String, Object> data = new HashMap<>();
        data.put("taskId", 2);//任务ID
        data.put("farmId", "1111");//主体代码
        data.put("templateId", 318);//天眼模板ID
        data.put("overDay", 2);//计划完成期限（天）
        data.put("remark", "");//备注
        data.put("keyInfo", "");//重点信息
        data.put("fromLevel", "CITY");
        //todo 补贴类型暂时写死
        data.put("susidyTypeDesc", "还田");

        String toUrl = "http://192.168.0.144:8099/seeyon/rest/detailPage/h5?tempCode=QZ-JD-01&dataId=-6330374241148056512";
        data.put("toUrl", toUrl);

        ArrayList<Map<String, String>> governmentSubjectIdList = new ArrayList<>();
        Map<String, String> governmentSubject = new HashMap<>();
        governmentSubject.put("governmentSubjectId", "117");
        governmentSubjectIdList.add(governmentSubject);
        data.put("members", governmentSubjectIdList);//计划检查机构

        Map<String, Object> param = new HashMap<>();


        List<Map<String, Object>> list = new ArrayList<>();
        list.add(data);

        String signData = "data=" + JSON.toJSONString(list) + "&secureKey=e88bb3876440515a0a32fa8384o7f8bc" + "&token=37145423-5ea8-41e8-82e7-94e70311047d";
        String sign = this.MD5(signData);

        param.put("data", JSON.toJSONString(list));
        param.put("sign", sign);
        param.put("token", "37145423-5ea8-41e8-82e7-94e70311047d");

        String bodyString = JSON.toJSONString(param);

        System.out.println(bodyString);


    }



    public static String MD5(String inStr) {
        MessageDigest md = null;
        String outStr = null;

        try {
            md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(inStr.getBytes());
            outStr = bytetoString(digest);
        } catch (NoSuchAlgorithmException var4) {
            var4.printStackTrace();
        }

        return outStr;
    }


    public static String bytetoString(byte[] digest) {
        String str = "";
        String tempStr = "";

        for(int i = 0; i < digest.length; ++i) {
            tempStr = Integer.toHexString(digest[i] & 255);
            if (tempStr.length() == 1) {
                str = str + "0" + tempStr;
            } else {
                str = str + tempStr;
            }
        }

        return str.toLowerCase();
    }


    @Test
    public void testSign3(){
        JSONObject jsonObject = new JSONObject(Boolean.TRUE);
        jsonObject.put("userId", 97);
        jsonObject.put("userName", "xiaohei");
//        jsonObject.put("result", 0);
        jsonObject.put("password", "xiaohei111");
        String jsonString = JSON.toJSONString(jsonObject);
        String salt = "zaxh6666";
        String tmpSign = this.MD5(jsonString + salt);
        System.out.println(tmpSign);
    }




}
