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

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;

import edu.skku.cs.sirenorder.Model.MenuModel;
import edu.skku.cs.sirenorder.R;

public class MenuAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<MenuModel> items;


    public MenuAdapter(Context mContext, ArrayList<MenuModel> items) {
        this.mContext = mContext;
        this.items = items;


    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.menu_listview, viewGroup, false); //질문
        }

        TextView tv_menu = view.findViewById(R.id.tv_menu);
        TextView tv_price = view.findViewById(R.id.tv_price);
        Button btn_gobasket = view.findViewById(R.id.btn_gobasket);
        Button bnt_minus = view.findViewById(R.id.btn_minus);
        Button btn_plus = view.findViewById(R.id.btn_plus);
        TextView tv_gaesoo = view.findViewById(R.id.tv_gaesoo);


        //플러스 버튼 누를시
        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cnt = Integer.parseInt(String.valueOf(tv_gaesoo.getText()));
                cnt++;
                tv_gaesoo.setText(String.valueOf(cnt));
            }
        });


        //마이너스 버튼 누를시
        bnt_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(String.valueOf(tv_gaesoo.getText())) > 0) {
                    int cnt = Integer.parseInt(String.valueOf(tv_gaesoo.getText()));
                    cnt--;
                    tv_gaesoo.setText(String.valueOf(cnt));
                }
            }
        });


        //담기 클릭할때마다 정보 저장하고 장바구니 탭 클릭할때 정보 한번에 넘겨
        btn_gobasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("custom-message");
                intent.putExtra("menu", String.valueOf(items.get(i).menu));
                intent.putExtra("menu_num", String.valueOf(tv_gaesoo.getText()));
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                tv_gaesoo.setText("0");
            }
        });
        tv_gaesoo.setText(items.get(i).menu_number);
        tv_menu.setText(items.get(i).menu);
        tv_price.setText(items.get(i).price);


        return view;
    }
}