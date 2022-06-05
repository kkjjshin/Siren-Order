package edu.skku.cs.sirenorder.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import edu.skku.cs.sirenorder.Model.MypageModel;
import edu.skku.cs.sirenorder.Model.OrderListGetModel;
import edu.skku.cs.sirenorder.Model.OrderModel;
import edu.skku.cs.sirenorder.Model.UpdateModel;
import edu.skku.cs.sirenorder.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OrderAcceptActivity extends AppCompatActivity {

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_accept);

        TextView tv_whatmenu = findViewById(R.id.tv_whatmenu);
        Button btn_accept = findViewById(R.id.btn_accept);
        Button btn_reject = findViewById(R.id.btn_reject);
        EditText et_whattime = findViewById(R.id.et_whattime);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");

        //get으로 주문 들어온 메뉴명 불러오기
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://dhn62rpfni.execute-api.ap-northeast-2.amazonaws.com/dev/getnameorder").newBuilder();
        urlBuilder.addQueryParameter("name", name);
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
                final OrderListGetModel data_whatmenu = gson.fromJson(myResponse, OrderListGetModel.class);

                OrderAcceptActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_whatmenu.setText(data_whatmenu.getMenu_with_numList()[0]);
                    }
                });
            }

        });


        //수락 버튼 누를시의 이벤트 post로 값변경
        btn_accept.setOnClickListener(view -> {
            String temp_et = et_whattime.getText().toString();
            if (temp_et.matches("")) {
                Toast.makeText(this, "주문 시간을 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else {

                //post 요청 보내기 주문 상태 테이블에 내 주문 요청 올려줌
                OkHttpClient client1 = new OkHttpClient();

                //이름명, 메뉴명과 개수 스트링, 진행상태, 총가격 꽂아줌줌
                UpdateModel uModel = new UpdateModel();
                uModel.setName(name);
                uModel.setOrder_now(String.valueOf(et_whattime.getText()));


                Gson gson = new Gson();
                String json = gson.toJson(uModel, UpdateModel.class);


                HttpUrl.Builder urlBuilder1 = HttpUrl.parse("https://dhn62rpfni.execute-api.ap-northeast-2.amazonaws.com/dev/updateorder").newBuilder();
                String url1 = urlBuilder1.build().toString();

                Request req1 = new Request.Builder()
                        .url(url1)
                        .post(RequestBody.create(MediaType.parse("application/json"), json))
                        .build();

                client1.newCall(req1).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        final String myResponse = response.body().string(); //tostring아님 주의

                        Gson gson = new GsonBuilder().create();

                        OrderAcceptActivity.this.runOnUiThread(new Runnable() { //별도의 핸들러 만들지 않아도 됨.
                            @Override
                            public void run() {
                                Toast.makeText(OrderAcceptActivity.this, "주문을 수락하였습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent_order = new Intent(OrderAcceptActivity.this, OrderListActivity.class);
                                finish();
                                startActivity(intent_order);
                            }
                        });
                    }
                });
            }
        });


        //거부 버튼 누를시의 이벤트(주문 목록에서 삭제 시켜버리기
        btn_reject.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(name + "님의 주문을 거부할까요?");
            builder.setCancelable(false);
            builder.setPositiveButton("예", new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    //post 요청 보내기 주문 상태 테이블에 내 주문 요청 올려줌
                    OkHttpClient client1 = new OkHttpClient();

                    //이름명, 메뉴명과 개수 스트링, 진행상태, 총가격 꽂아줌줌
                    UpdateModel uModel = new UpdateModel();
                    uModel.setName(name);


                    Gson gson = new Gson();
                    String json = gson.toJson(uModel, UpdateModel.class);


                    HttpUrl.Builder urlBuilder1 = HttpUrl.parse("https://dhn62rpfni.execute-api.ap-northeast-2.amazonaws.com/dev/dorder").newBuilder();
                    String url1 = urlBuilder1.build().toString();

                    Request req1 = new Request.Builder()
                            .url(url1)
                            .post(RequestBody.create(MediaType.parse("application/json"), json))
                            .build();

                    client1.newCall(req1).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            final String myResponse = response.body().string();

                            Gson gson = new GsonBuilder().create();

                            OrderAcceptActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent_order = new Intent(OrderAcceptActivity.this, OrderListActivity.class);
                                    finish();
                                    startActivity(intent_order);
                                }
                            });
                        }
                    });

                }
            });

            builder.setNegativeButton("아니요", new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.setNeutralButton("취소", new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        });
    }
}