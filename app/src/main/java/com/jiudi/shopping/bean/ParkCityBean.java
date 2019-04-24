package com.jiudi.shopping.bean;

import java.util.List;

/**
 * 作者：Constantine on 2018/9/6.
 * 邮箱：2534159288@qq.com
 */
public class ParkCityBean {

    private int code;
    private String info;
    private List<Data> data;


    public void setCode(int code) {

        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public List<Data> getData() {
        return data;
    }



    public static class Data {

        private String id;
        private String name;
        private List<Citys> citys;

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setCitys(List<Citys> citys) {
            this.citys = citys;
        }

        public List<Citys> getCitys() {
            return citys;
        }

        public static class Citys {

            private String id;
            private String name;

            public void setId(String id) {
                this.id = id;
            }

            public String getId() {
                return id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }

        }
    }
}