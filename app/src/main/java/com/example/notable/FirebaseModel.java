package com.example.notable;

public class FirebaseModel {

    private String baslik;
    private String icerik;

    public FirebaseModel()
    {

    }

    public FirebaseModel(String baslik, String icerik)
    {
        this.baslik = baslik;
        this.icerik = icerik;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getIcerik() {
        return icerik;
    }

    public void setIcerik(String icerik) {
        this.icerik = icerik;
    }
}


