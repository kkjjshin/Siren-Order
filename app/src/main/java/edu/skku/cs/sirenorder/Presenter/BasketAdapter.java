package edu.skku.cs.sirenorder.Presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IntDef;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.HashMap;

import edu.skku.cs.sirenorder.Model.BasketModel;
import edu.skku.cs.sirenorder.Model.MenuModel;
import edu.skku.cs.sirenorder.R;
import edu.skku.cs.sirenorder.View.BasketActivity;

public class BasketAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<BasketModel> items;
    int alMenuPriceArr[] = {3500, 4000, 5000, 4500, 3500, 4000, 4000, 4500,
            5000, 4000, 4000, 5000, 2000, 1500};
    String allMenuArr[] = {"GrapeAde", "AppleAde", "PearAde", "LemonAde", "IceAmericano"
            , "Espresso", "CafeMocha", "ViennaCoffee", "VanillaCoffee", "Cappucchino"
            , "HoneyBread", "Waffle", "Pie", "AppleCookie"};
    HashMap<String, Integer> basketMap_price = new HashMap<String, Integer>();


    public BasketAdapter(Context mContext, ArrayList<BasketModel> items) {
        this.mContext = mContext;
        this.items = items;
        //메뉴명-가격 해시맵 생성
        for (int i = 0; i < allMenuArr.length; i++) {
            basketMap_price.put(allMenuArr[i], alMenuPriceArr[i]);
        }
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }


    public String getItemnum(int i) {
        return items.get(i).menu_number;
    }

    public String getItemname(int i) {
        return items.get(i).menu;
    }

    public int getItemprice(int i) {
        return Integer.parseInt(items.get(i).price);
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.basket_listview, viewGroup, false);
        }

        TextView tv_menu_bsk = view.findViewById(R.id.tv_menu_bsk);
        TextView tv_num_bsk = view.findViewById(R.id.tv_num_bsk);
        TextView tv_price_bsk = view.findViewById(R.id.tv_price_bsk);
        Button btn_plus2 = view.findViewById(R.id.btn_plus2);
        Button btn_minus2 = view.findViewById(R.id.btn_minus2);


        //플러스
        btn_plus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = Integer.parseInt(items.get(i).menu_number);
                num++;
                String newnum = String.valueOf(num);
                items.get(i).menu_number = newnum;
                tv_num_bsk.setText(items.get(i).menu_number);

                //가격도 변경해야함
                int totalprice = num * basketMap_price.get(items.get(i).menu);
                items.get(i).price = String.valueOf(totalprice);
                tv_price_bsk.setText(items.get(i).price);
            }
        });

        //마이너스
        btn_minus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.valueOf(items.get(i).menu_number) > 1) {
                    int num = Integer.parseInt(items.get(i).menu_number);
                    num--;
                    String newnum = String.valueOf(num);
                    items.get(i).menu_number = newnum;
                    tv_num_bsk.setText(items.get(i).menu_number);

                    //가격도 변경해야함
                    int totalprice = num * basketMap_price.get(items.get(i).menu);
                    items.get(i).price = String.valueOf(totalprice);
                    tv_price_bsk.setText(items.get(i).price);
                } else {
                    Toast.makeText(mContext, "전체 삭제를 원하시면 항목을 클릭한 후, 삭제 버튼을 눌러주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tv_menu_bsk.setText(items.get(i).menu);
        tv_num_bsk.setText(items.get(i).menu_number);
        tv_price_bsk.setText(items.get(i).price);


        return view;
    }
}
