package com.example.convert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyClass {
    public static void main(String[] args) {
        String org="\"id\": 1,\n" +
                "            \"product_id\": 1,\n" +
                "            \"image\": \"http://datong.crmeb.net/public/uploads/attach/2019/01/15/5c3dba1366885.jpg\",\n" +
                "            \"images\": [\n" +
                "                \"http://datong.crmeb.net/public/uploads/attach/2019/01/15/5c3dba1366885.jpg\",\n" +
                "                \"http://datong.crmeb.net/public/uploads/attach/2019/01/15/5c3dba4187461.jpg\",\n" +
                "                \"http://datong.crmeb.net/public/uploads/attach/2019/01/15/5c3db9de2b73a.jpg\"\n" +
                "            ],\n" +
                "            \"title\": \"手慢无 无线吸尘器F8 玫瑰金礼盒版\",\n" +
                "            \"info\": \"【年货节活动价1699元，领取吸尘器优惠券再减50元，到手价仅1649元】\",\n" +
                "            \"price\": \"1.00\",\n" +
                "            \"cost\": \"100.00\",\n" +
                "            \"ot_price\": \"599.00\",\n" +
                "            \"give_integral\": \"1699.00\",\n" +
                "            \"sort\": 1,\n" +
                "            \"stock\": 987,\n" +
                "            \"sales\": 13,\n" +
                "            \"unit_name\": \"件\",\n" +
                "            \"postage\": \"0.00\",\n" +
                "            \"description\": \"\",\n" +
                "            \"start_time\": \"1546272000\",\n" +
                "            \"stop_time\": \"1551283200\",\n" +
                "            \"add_time\": \"1547554098\",\n" +
                "            \"status\": 1,\n" +
                "            \"is_postage\": 1,\n" +
                "            \"is_hot\": 1,\n" +
                "            \"is_del\": 0,\n" +
                "            \"num\": 1,\n" +
                "            \"is_show\": 1";
        //parm参数
        Pattern pattern=null;
        Matcher matcher=null;
        pattern=Pattern.compile("public String(.*?);");
        matcher=pattern.matcher(org);
        while (matcher.find()){
            try {
                String tmp1=matcher.group(1).replace("\"","").trim();
                System.out.println("statAll."+tmp1+"=statComp."+tmp1+";");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        pattern=Pattern.compile("TextView(.*?);");
        matcher=pattern.matcher(org);
        while (matcher.find()){
            try {
                String tmp1=matcher.group(1).replace("\"","").trim();
                System.out.println(""+tmp1+".setText(bean."+tmp1+");");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("-------------隔断--------------");
         pattern=Pattern.compile("(.*):(.*)");
         matcher=pattern.matcher(org);
        while (matcher.find()){
            try {
                String tmp1=matcher.group(1).replace("\"","").trim();
                String textview=tmp1.split("\\.")[0];
                System.out.println("result.put(\""+tmp1+"\","+textview+".getText().toString());");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("-------------隔断--------------");
        //

         pattern=Pattern.compile("(.*?):(.*)");
        matcher=pattern.matcher(org);
        while (matcher.find()){
            try {
                String tmp1=matcher.group(1).replace("\"","").trim();
                System.out.println("public String "+tmp1+";");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        pattern=Pattern.compile("(.*?):(.*)");
        matcher=pattern.matcher(org);
        while (matcher.find()){
            try {
                String tmp1=matcher.group(1).replace("\"","").trim();
                System.out.println("bean."+tmp1+"=jsonObject.optString(\""+tmp1+"\");");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        pattern=Pattern.compile("(.*?):(.*)");
//        matcher=pattern.matcher(org);
//        while (matcher.find()){
//            try {
//                String tmp1=matcher.group(1).replace("\"","").trim();
//                System.out.println("bean."+tmp1+"=jsonObject.optString(\""+tmp1+"\");");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        System.out.println("-------------隔断--------------");
        pattern=Pattern.compile("(.*?)\n");
        matcher=pattern.matcher(org);
        while (matcher.find()){
            try {
                String tmp1=matcher.group(1).replace("\"","").trim();
                System.out.println("map.put(\""+tmp1+"\", \"\");");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
