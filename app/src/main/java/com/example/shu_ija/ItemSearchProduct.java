package com.example.shu_ija;

public class ItemSearchProduct{
    private String idProduct ;
    private String nameProduct ;
    private String nameCategory;


    ItemSearchProduct(String idProduct,String nameProduct,String nameCategory){
        this.nameProduct=nameProduct;
        this.idProduct=idProduct;
        this.nameCategory=nameCategory;

    }
    public String getnameProduct() {
        return nameProduct;
    }
    public String getnameCategory() {
        return nameCategory;
    }

    public  String getidProduct () {
        return idProduct;
    }
    public void setidProduct (String idProduct) {
        this.idProduct= idProduct;
    }
    public void setnameCategory (String nameCategory) {
        this.nameCategory =nameCategory;
    }
    public void setnameProduct (String nameProduct) {
        this.nameProduct =nameProduct;
    }
}