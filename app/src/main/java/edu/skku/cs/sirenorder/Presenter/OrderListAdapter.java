package edu.skku.cs.sirenorder.Presenter;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

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

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.w3c.dom.Text;

import java.util.ArrayList;

import edu.skku.cs.sirenorder.Model.MenuModel;
import edu.skku.cs.sirenorder.Model.OrderListModel;
import edu.skku.cs.sirenorder.R;
import edu.skku.cs.sirenorder.View.OrderAcceptActivity;

public class OrderListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<OrderListModel> items;

    public OrderListAdapter(Context mContext, ArrayList<OrderListModel> items) {
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
            view = inflater.inflate(R.layout.orderlist_listview, viewGroup, false); //질문
        }

        TextView tv_name_orderlist = view.findViewById(R.id.tv_name_orderlist);
        TextView tv_allprice_orderlist = view.findViewById(R.id.tv_allprice_orderlist);
        Button btn_see_orderlist = view.findViewById(R.id.btn_see_orderlist);
        TextView tv_dd = view.findViewById(R.id.tv_ordernowlist);

        tv_name_orderlist.setText(items.get(i).name);
        tv_allprice_orderlist.setText(items.get(i).allprice);
        tv_dd.setText((items.get(i).ordernow));

        //보기 버튼 클릭시 그 사용자 정보를 인텐트로 보내줌
        btn_see_orderlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), OrderAcceptActivity.class);
                intent.putExtra("name", items.get(i).name);
                mContext.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
            }
        });


        return view;
    }
}
