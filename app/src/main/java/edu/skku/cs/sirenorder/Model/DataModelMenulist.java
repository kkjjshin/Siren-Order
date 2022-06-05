package edu.skku.cs.sirenorder.Model;

public class DataModelMenulist {

    private String[] ade;
    private String[] ade_price;
    private String[] coffee;
    private String[] coffee_price;
    private String[] etc;

    public String[] getAde_price() {
        return ade_price;
    }

    public void setAde_price(String[] ade_price) {
        this.ade_price = ade_price;
    }

    public String[] getCoffee() {
        return coffee;
    }

    public void setCoffee(String[] coffee) {
        this.coffee = coffee;
    }

    public String[] getCoffee_price() {
        return coffee_price;
    }

    public void setCoffee_price(String[] coffee_price) {
        this.coffee_price = coffee_price;
    }

    public String[] getEtc() {
        return etc;
    }

    public void setEtc(String[] etc) {
        this.etc = etc;
    }

    public String[] getEtc_price() {
        return etc_price;
    }

    public void setEtc_price(String[] etc_price) {
        this.etc_price = etc_price;
    }

    private String[] etc_price;


    public String[] getAde() {
        return ade;
    }

    public void setAde(String[] ade) {
        this.ade = ade;
    }
}
