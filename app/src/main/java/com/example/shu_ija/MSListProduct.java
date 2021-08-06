package com.example.shu_ija;

public class MSListProduct{
    private String idModel;
    private String quantity ;
    private String unit;


    MSListProduct(String idModel,String quantity,String unit){
        this.quantity=quantity;
        this.idModel=idModel;
        this.unit=unit;

    }
    public String getquantity() {
        return quantity;
    }
    public String getunit() {
        return unit;
    }

    public  String getidModel() {
        return idModel;
    }
    public void setidModel(String idModel) {
        this.idModel= idModel;
    }
    public void setunit (String unit) {
        this.unit =unit;
    }
    public void setquantity (String quantity) {
        this.quantity =quantity;
    }
}