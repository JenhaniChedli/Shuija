package com.example.shu_ija;

public class ItemPos {
        private String idPointOfSale ;
        private String designation ;
        private String address;
        private Boolean check;


    ItemPos(String idPointOfSale,String designation,String address,Boolean check){
            this.designation=designation;
            this.idPointOfSale=idPointOfSale;
            this.address=address;
            this.check=check;
        }
        public String getidPointOfSale() {
            return idPointOfSale;
        }
        public Boolean getcheck() {
        return check;
    }
        public String getaddress() {
            return address;
        }

        public  String getdesignation () {
            return designation;
        }
        public void setidPointOfSale (String idPointOfSale) {
            this.idPointOfSale = idPointOfSale;
        }
    public void setcheck (Boolean check) {
        this.check=check;
    }
        public void setaddress (String address) {
            this.address = address;
        }
        public void setdesignation (String designation) {
            this.designation =designation;
        }

}
