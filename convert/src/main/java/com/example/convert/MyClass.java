package com.example.convert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyClass {
    public static void main(String[] args) {
        String org="        \"uid\": 15,\n" +
                "        \"account\": \"wx151555392310\",\n" +
                "        \"pwd\": \"e10adc3949ba59abbe56e057f20f883e\",\n" +
                "        \"nickname\": \"\\u68a6\\u56de\\u6c5f\\u5357\\u591c\\u4e36\",\n" +
                "        \"avatar\": \"http:\\/\\/thirdwx.qlogo.cn\\/mmopen\\/3ywYCFwUvZmQLw3w9qrwf29WLpibe8kNXFm9TpibK4VOAKtp7Xsd8fM5bZkrt0Rorplgb8udNLdwES3ZAIbuazL7sVweFQodBK\\/132\",\n" +
                "        \"phone\": null,\n" +
                "        \"add_time\": \"19\\/04\\/16\",\n" +
                "        \"add_ip\": \"140.207.54.80\",\n" +
                "        \"last_time\": 1555638578,\n" +
                "        \"last_ip\": \"117.80.132.11\",\n" +
                "        \"now_money\": \"0.00\",\n" +
                "        \"integral\": \"1.00\",\n" +
                "        \"status\": 1,\n" +
                "        \"level\": 0,\n" +
                "        \"spread_uid\": 6,\n" +
                "        \"agent_id\": 1,\n" +
                "        \"user_type\": \"wechat\",\n" +
                "        \"is_promoter\": 1,\n" +
                "        \"pay_count\": 0,\n" +
                "        \"direct_num\": 0,\n" +
                "        \"team_num\": 0,\n" +
                "        \"is_reward\": 0,\n" +
                "        \"allowance_number\": 0,\n" +
                "        \"money\": 0";
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
