package edu.skku.cs.sirenorder.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import edu.skku.cs.sirenorder.Model.MenuModel;
import edu.skku.cs.sirenorder.Model.MypageModel;
import edu.skku.cs.sirenorder.Model.OrderListGetModel;
import edu.skku.cs.sirenorder.Model.OrderListModel;
import edu.skku.cs.sirenorder.Presenter.MenuAdapter;
import edu.skku.cs.sirenorder.Presenter.OrderListAdapter;
import edu.skku.cs.sirenorder.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrderListActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        //로그아웃 버튼클릭시
        Button btn_logout = findViewById(R.id.button4);

        btn_logout.setOnClickListener(view -> {
            Intent intent_logout = new Intent(OrderListActivity.this, MainActivity.class);
            finish();
            startActivity(intent_logout);
        });

        //주문리스트 받아오기

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://dhn62rpfni.execute-api.ap-northeast-2.amazonaws.com/dev/getorder").newBuilder();
        String url = urlBuilder.build().toString();

        Request req = new Request.Builder().url(url).build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final OrderListGetModel data_orderlist = gson.fromJson(myResponse, OrderListGetModel.class);

                OrderListActivity.this.runOnUiThread(new Runnable() { //별도의 핸들러 만들지 않아도 됨.
                    @Override
                    public void run() {
                        //어댑터로 총 주문 목록 가져와줌
                        ArrayList<OrderListModel> items = new ArrayList<>();
                        ListView lv_adminlist = findViewById(R.id.lv_adminlist);
                        int num = data_orderlist.getNameList().length;
                        for (int i = 0; i < num; i++) {
                            items.add(new OrderListModel(data_orderlist.getNameList()[i], data_orderlist.getAllpriceList()[i], data_orderlist.getOrder_now_list()[i]));
                        }

                        OrderListAdapter mmAdapter = new OrderListAdapter(getApplicationContext(), items);
                        lv_adminlist.setAdapter(mmAdapter);
                    }
                });
            }
        });


    }
}