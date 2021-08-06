package com.example.shu_ija;

import java.util.ArrayList;

public class modalItem {

    private String nameModel;
    private ArrayList unit;
    private String idModel;


    modalItem (String idModel,String nameModel){
        this.nameModel=nameModel;
       // this.unit=unit;
        this.idModel=idModel;
    }

    public String getnameModel() {
        return nameModel;
    }
    public ArrayList getunit() {
        return unit;
    }
    public String getidModel() {
        return idModel;
    }
    public void setnameModel (String nameModel) {
        this.nameModel=nameModel;
    }
    public void setunit (ArrayList unit) {
        this.unit=unit;
    }
    public void setidModel (String idModel) {
        this.idModel=idModel;
    }

}

