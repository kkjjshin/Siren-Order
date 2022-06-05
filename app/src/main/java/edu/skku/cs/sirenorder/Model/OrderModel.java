package edu.skku.cs.sirenorder.Model;

public class OrderModel {
    public String name;
    public String menu_with_num;
    public String order_now;
    public String allprice;



    public boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMenu_with_num() {
        return menu_with_num;
    }

    public void setMenu_with_num(String menu_with_num) {
        this.menu_with_num = menu_with_num;
    }

    public String getOrder_now() {
        return order_now;
    }

    public void setOrder_now(String order_now) {
        this.order_now = order_now;
    }

    public String getAllprice() {
        return allprice;
    }

    public void setAllprice(String allprice) {
        this.allprice = allprice;
    }
}
