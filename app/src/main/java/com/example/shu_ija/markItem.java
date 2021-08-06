package com.example.shu_ija;

public class markItem {

    private String nameMark;
    private String idMark;


    markItem(String idMark,String nameMark){
        this.nameMark=nameMark;
        this.idMark=idMark;
    }

    public String getnameMark() {
        return nameMark;
    }
    public String getidMark() {
        return idMark;
    }
    public void setnameMark (String nameMark) {
        this.nameMark=nameMark;
    }
    public void setidMark (String idMark) {
        this.idMark=idMark;
    }


}
