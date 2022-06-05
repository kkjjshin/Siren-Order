package edu.skku.cs.sirenorder.Model;

public class BasketModel {
    public String menu;
    public String price;
    public String menu_number;

    public BasketModel(String menu, String menu_number, String price){
        this.menu = menu;
        this.menu_number = menu_number;
        this.price = price;
    }
}
