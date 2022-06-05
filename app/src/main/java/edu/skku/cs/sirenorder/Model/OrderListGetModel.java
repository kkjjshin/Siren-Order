package edu.skku.cs.sirenorder.Model;

public class OrderListGetModel {
    public String[] nameList;
    public String[] menu_with_numList;
    public String[] order_now_list;
    public String[] allpriceList;

    public String[] getNameList() {
        return nameList;
    }

    public void setNameList(String[] nameList) {
        this.nameList = nameList;
    }

    public String[] getMenu_with_numList() {
        return menu_with_numList;
    }

    public void setMenu_with_numList(String[] menu_with_numList) {
        this.menu_with_numList = menu_with_numList;
    }

    public String[] getOrder_now_list() {
        return order_now_list;
    }

    public void setOrder_now_list(String[] order_now_list) {
        this.order_now_list = order_now_list;
    }

    public String[] getAllpriceList() {
        return allpriceList;
    }

    public void setAllpriceList(String[] allpriceList) {
        this.allpriceList = allpriceList;
    }
}
