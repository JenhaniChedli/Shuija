package com.example.shu_ija;

public class listItem {
    private String idList ;
    private String nameList ;
    private String date;
    private String NProduit;

    listItem(String nameList,String idList,String date,String NProduit){
        this.nameList=nameList;
        this.idList=idList;
        this.date=date;
        this.NProduit=NProduit;
    }
    public String getidList() {
        return idList;
    }
    public String getdate() {
        return date;
    }
    public String getNProduit() {
        return NProduit;
    }
    public  String getnameList () {
        return nameList;
    }
    public void setidList (String idList) {
        this.idList = idList;
    }
    public void setdate (String date) {
        this.date = date;
    }
    public void setNProduit (String NProduit) {
        this.NProduit = NProduit;
    }
    public void setnameList (String nameList) {
        this.nameList =nameList;
    }
}
