package com.example.shu_ija;

public class Panier {
    private String idModel;
    private String quantity ;
    private String unit;
    private String prix;


    Panier(String idModel,String quantity,String unit,String prix){
        this.quantity=quantity;
        this.idModel=idModel;
        this.unit=unit;
        this.prix=prix;

    }
    public String getquantity() {
        return quantity;
    }
    public String getunit() {
        return unit;
    }
    public  String getprix() {
        return prix;
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
    public void setprix ( String prix) {
        this.prix=prix;
    }

}
