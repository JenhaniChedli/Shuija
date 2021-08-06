package com.example.shu_ija;


public class hlistItem {
    private String idhList ;
    private String nameList ;
    private String date;


    hlistItem(String nameList,String idhList,String date){
        this.nameList=nameList;
        this.idhList=idhList;
        this.date=date;

    }
    public String getidhList() {
        return idhList;
    }
    public String getdate() {
        return date;
    }

    public  String getnameList () {
        return nameList;
    }
    public void setidhList (String idList) {
        this.idhList = idhList;
    }
    public void setdate (String date) {
        this.date = date;
    }
    public void setnameList (String nameList) {
        this.nameList =nameList;
    }
}