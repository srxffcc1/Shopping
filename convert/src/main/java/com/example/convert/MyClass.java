package com.example.convert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyClass {
    public static void main(String[] args) {
        String org="addressId\n" +
                "收货地址ID\n" +
                "bargainId\n" +
                "砍价ID\n" +
                "couponId\n" +
                "优惠卷ID\n" +
                "payType\n" +
                "支付方式\n" +
                "seckill_id\n" +
                "秒杀ID\n" +
                "useIntegral\n" +
                "积分\n" +
                "mark\n" +
                "标注\n" +
                "combinationId\n" +
                "库存ID\n" +
                "pinkId";
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
