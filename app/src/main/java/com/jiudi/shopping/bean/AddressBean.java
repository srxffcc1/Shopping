package com.jiudi.shopping.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


public class AddressBean {

    private List<Province> mProvinceList = new ArrayList<>();

    public List<Province> getProvinceList() {
        return mProvinceList;
    }

    public void setProvinceList(List<Province> provinceList) {
        mProvinceList = provinceList;
    }

    public static class Province implements Parcelable {
        private String mId;
        private String mName;
        private List<City> mCityList = new ArrayList<>();

        public Province() {

        }

        protected Province(Parcel in) {
            mId = in.readString();
            mName = in.readString();
            mCityList = in.createTypedArrayList(City.CREATOR);
        }

        public static final Creator<Province> CREATOR = new Creator<Province>() {
            @Override
            public Province createFromParcel(Parcel in) {
                return new Province(in);
            }

            @Override
            public Province[] newArray(int size) {
                return new Province[size];
            }
        };

        public String getId() {
            return mId;
        }

        public String getName() {
            return mName;
        }

        public void setId(String id) {
            mId = id;
        }

        public void setName(String name) {
            mName = name;
        }

        public List<City> getCityList() {
            return mCityList;
        }

        public void setCityList(List<City> cityList) {
            mCityList = cityList;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mId);
            dest.writeString(mName);
            dest.writeTypedList(mCityList);
        }
    }

    public static class City implements Parcelable {
        private String mId;
        private String mName;
        private List<Area> mAreaList = new ArrayList<>();

        public City() {

        }

        protected City(Parcel in) {
            mId = in.readString();
            mName = in.readString();
            mAreaList = in.createTypedArrayList(Area.CREATOR);
        }

        public static final Creator<City> CREATOR = new Creator<City>() {
            @Override
            public City createFromParcel(Parcel in) {
                return new City(in);
            }

            @Override
            public City[] newArray(int size) {
                return new City[size];
            }
        };

        public String getId() {
            return mId;
        }

        public String getName() {
            return mName;
        }

        public void setId(String id) {
            mId = id;
        }

        public void setName(String name) {
            mName = name;
        }

        public List<Area> getAreaList() {
            return mAreaList;
        }

        public void setAreaList(List<Area> areaList) {
            mAreaList = areaList;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mId);
            dest.writeString(mName);
            dest.writeTypedList(mAreaList);
        }
    }

    public static class Area implements Parcelable {
        private String mId;
        private String mName;

        public Area() {

        }

        protected Area(Parcel in) {
            mId = in.readString();
            mName = in.readString();
        }

        public static final Creator<Area> CREATOR = new Creator<Area>() {
            @Override
            public Area createFromParcel(Parcel in) {
                return new Area(in);
            }

            @Override
            public Area[] newArray(int size) {
                return new Area[size];
            }
        };

        public String getId() {
            return mId;
        }

        public String getName() {
            return mName;
        }

        public void setId(String id) {
            mId = id;
        }

        public void setName(String name) {
            mName = name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mId);
            dest.writeString(mName);
        }
    }

}
