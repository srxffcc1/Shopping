package com.jiudi.shopping.bean;

public class DeviceBean {
    public String sys_time;
    public String user_name;
    public String jingdu;
    public String weidu;
    public String ljingdu;
    public String lweidu;
    public String datetime;
    public String heart_time;
    public String su;
    public String status;
    public String hangxiang;
    public String sim_id;
    public String user_id;
    public String sale_type;
    public String iconType;
    public String server_time;
    public String product_type;
    public String expire_date;
    public String group_id;
    public String statenumber;
    public String electric;
    public String describe;
    public String sim;
    public String precision;


    public String status2String(int index){
        String result=status.substring(index-1,index);
        if("0".equals(result)){
            return "闭";
        }else{
            return "开";
        }
    }
    public String statusNumber2String(int index){
        String result=statenumber.split(",")[index-1];
        return result;
    }
}
