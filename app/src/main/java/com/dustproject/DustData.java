package com.dustproject;

public class DustData {
    private String humi;
    private String temp;
    private String dust;
    private String dustmin;
    private String tvoc;
    private String co2;
    private String co;
    private String ch2o;
    private String pm1;
    private int dustitem;

    public String getHumi() {
        return humi;
    }
    public void setHumi(String humi) {
        this.humi = humi;
    }
    public String getTemp() {
        return temp;
    }
    public void setTemp(String temp) {
        this.temp = temp;
    }
    public String getDust() {
        return dust;
    }
    public void setDust(String dust) {
        this.dust = dust;
    }
    public String getDustmin() {
        return dustmin;
    }
    public void setDustmin(String dustmin) {
        this.dustmin = dustmin;
    }
    public String getTvoc() {
        return tvoc;
    }
    public void setTvoc(String tvoc) {
        this.tvoc = tvoc;
    }
    public String getCo2() {
        return co2;
    }
    public void setCo2(String co2) {
        this.co2 = co2;
    }
    public String getCo() {
        return co;
    }
    public void setCo(String co) {
        this.co = co;
    }
    public String getPm1() {
        return pm1;
    }
    public String getCh2o() {
        return ch2o;
    }
    public void setCh2o(String ch2o) {
        this.ch2o = ch2o;
    }
    public void setPm1(String pm1) {
        this.pm1 = pm1;
    }
    public int getDustitem() { return dustitem; }
    public void setDustitem(int dustitem ) {this.dustitem = dustitem; }
}