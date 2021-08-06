package com.example.shu_ija;

    public class ItemModeCourse {
        private String nameProduct ;
        private String nameCategory;
        private String nameMark;
        private String nameModel;
        private String unit;
        private String quantity;
        private String idModel;
        private String idProduct;
        private String idMark;
        private String price;

        ItemModeCourse(String nameProduct,String nameCategory,String nameMark,String nameModel,String unit,String quantity,String idModel,String idProduct,String idMark,String price){
            this.nameProduct=nameProduct;
            this.nameCategory=nameCategory;
            this.nameMark=nameMark;
            this.unit=unit;
            this.nameModel=nameModel;
            this.quantity=quantity;
            this.idModel=idModel;
            this.idProduct=idProduct;
            this.idMark=idMark;
            this.price=price;
        }


        public String getnameProduct() {
            return nameProduct;
        }
        public String getnameCategory() {
            return nameCategory;
        }
        public String getnameMark() {
            return nameMark;
        }
        public String getnameModel() {
            return nameModel;
        }
        public String getunit() {
            return unit;
        }
        public String getquantity() {
            return quantity;
        }
        public String getidModel() {
            return idModel;
        }
        public String getidProduct() {
            return idProduct;
        }
        public String getidMark() {
            return idMark;
        }
        public String getprice() {
            return price;
        }

        public void setnameProduct (String nameProduct) {
            this.nameProduct=nameProduct;
        }
        public void setnameCategory (String nameCategory) {
            this.nameCategory=nameCategory;
        }
        public void setnameMark (String nameMark) {
            this.nameMark=nameMark;
        }
        public void setnameModel (String nameModel) {
            this.nameModel=nameModel;
        }
        public void setunit (String unit) {
            this.unit=unit;
        }
        public void  setquantity(String quantity) {
            this.quantity=quantity;
        }
        public void setidModel (String idModel) {
            this.idModel=idModel;
        }
        public void setidProduct (String idProduct) {
            this.idProduct=idProduct;
        }
        public void setidMark (String idMark) {
            this.idMark=idMark;
        }
        public void setprice (String price) {
            this.price=price;
        }
    }