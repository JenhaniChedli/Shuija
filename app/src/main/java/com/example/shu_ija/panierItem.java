package com.example.shu_ija;

public class panierItem {
        private String idPanier ;
        private String idPos ;
        private String Pos ;
        private String date;
        private String NProduit;
        private String prixtotal;


        panierItem(String idPos,String Pos,String idPanier,String date,String NProduit,String prixtotal){
            this.idPos=idPos;
            this.Pos=Pos;
            this.idPanier=idPanier;
            this.date=date;
            this.NProduit=NProduit;
            this.prixtotal=prixtotal;
        }
        public String getIdPanier() {
            return idPanier;
        }
        public String getdate() {
            return date;
        }
        public String getNProduit() {
            return NProduit;
        }
        public String getprixtotal() {
        return prixtotal;
         }
        public  String getIdPos () {
            return idPos;
        }
        public  String getPos () {
        return Pos;
    }
        public void setIdPanier (String idPanier) {
            this.idPanier = idPanier;
        }
        public void setdate (String date) {
            this.date = date;
        }
        public void setprixtotal (String prixtotal) {
            this.prixtotal = prixtotal;
        }
        public void setNProduit (String NProduit) {
        this.NProduit = NProduit;
         }
        public void setIdPos (String idPos) {
            this.idPos =idPos;
        }
        public void setPos (String Pos) {
        this.Pos =Pos;
    }
    
}
