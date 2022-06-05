package edu.skku.cs.sirenorder.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import edu.skku.cs.sirenorder.Model.MypageModel;
import edu.skku.cs.sirenorder.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MypageActivity extends AppCompatActivity {

    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        TextView tv_id = findViewById(R.id.textView3);
        EditText et_pagename = findViewById(R.id.et_pagename);
        EditText et_allprice = findViewById(R.id.et_allprice);
        EditText et_ordernow = findViewById(R.id.et_ordernow);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        tv_id.setText(user_id + "님의 주문현황");

        //get 요청

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://dhn62rpfni.execute-api.ap-northeast-2.amazonaws.com/dev/getnameorder").newBuilder();
        urlBuilder.addQueryParameter("name", user_id);
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
                final MypageModel data_mypage = gson.fromJson(myResponse, MypageModel.class);

                MypageActivity.this.runOnUiThread(new Runnable() { //별도의 핸들러 만들지 않아도 됨.
                    @Override
                    public void run() {
                        if (data_mypage.getNameList().length != 0) { //주문 했을 때만 실행
                            et_pagename.setText(data_mypage.getNameList()[0]);
                            et_allprice.setText(data_mypage.getAllpriceList()[0]);
                            et_ordernow.setText(data_mypage.getOrder_now_list()[0]);
                        }
                    }
                });
            }
        });


    }
}