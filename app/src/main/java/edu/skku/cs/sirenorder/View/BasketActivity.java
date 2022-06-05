package edu.skku.cs.sirenorder.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.skku.cs.sirenorder.Model.BasketModel;
import edu.skku.cs.sirenorder.Model.DatamodelLogin2;
import edu.skku.cs.sirenorder.Model.MenuModel;
import edu.skku.cs.sirenorder.Model.OrderModel;
import edu.skku.cs.sirenorder.Presenter.BasketAdapter;
import edu.skku.cs.sirenorder.Presenter.MenuAdapter;
import edu.skku.cs.sirenorder.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BasketActivity extends AppCompatActivity {
    //둘의 메뉴명, 순서 같음.
    String alMenuPriceArr[] = {"3500", "4000", "5000", "4500", "3500", "4000", "4000", "4500",
            "5000", "4000", "4000", "5000", "2000", "1500"};
    String allMenuArr[] = {"GrapeAde", "AppleAde", "PearAde", "LemonAde", "IceAmericano"
            , "Espresso", "CafeMocha", "ViennaCoffee", "VanillaCoffee", "Cappucchino"
            , "HoneyBread", "Waffle", "Pie", "AppleCookie"};
    HashMap<String, Integer> basketMap_price = new HashMap<String, Integer>();
    HashMap<String, Integer> total_price = new HashMap<String, Integer>();
    int totalprice = 0;
    String user_id;
    String menu_with_num = "";
    int clicked = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        Button btn_pay = findViewById(R.id.btn_pay);
        ListView lv_paylist = findViewById(R.id.lv_paylist);

        //키: 메뉴명 값: 가격
        for (int i = 0; i < alMenuPriceArr.length; i++) {
            basketMap_price.put(allMenuArr[i], Integer.parseInt(alMenuPriceArr[i]));
        }

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        HashMap<String, Integer> hashMap = (HashMap<String, Integer>) intent.getSerializableExtra("basketMap");


        ArrayList<BasketModel> items = new ArrayList<>();
        //이 과정을 함수화 시키면 됨. hashMap 업데이트 하는 과정.
        for (int i = 0; i < allMenuArr.length; i++) {
            if (hashMap.get(allMenuArr[i]) == 0) { //개수 0 인거는 제거
                hashMap.remove(allMenuArr[i]);
            } else {
                int inPrice = Integer.parseInt(alMenuPriceArr[i]);
                int inNum = Integer.parseInt(String.valueOf(hashMap.get(allMenuArr[i])));
                String inMenu = allMenuArr[i];
                items.add(new BasketModel(inMenu, String.valueOf(inNum), String.valueOf(inNum * inPrice)));
            }
        }
        BasketAdapter mAdapter = new BasketAdapter(getApplicationContext(), items);
        lv_paylist.setAdapter(mAdapter);

        lv_paylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clicked = i;
                lv_paylist.setSelector(new PaintDrawable(0xfff0000));
            }
        });


        //삭제 버튼 클릭
        Button btn_test = findViewById(R.id.btn_test);
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked != -1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(BasketActivity.this);
                    builder.setTitle(mAdapter.getItemname(clicked) + "를 장바구니에서 완전히 삭제할까요?"); // 다이얼로그 제목
                    builder.setCancelable(false);   // 다이얼로그 화면 밖 터치 방지
                    builder.setPositiveButton("예", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            items.remove(clicked);
                            mAdapter.notifyDataSetChanged();
                        }
                    });

                    builder.setNegativeButton("아니요", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                }
            }
        });

        btn_pay.setOnClickListener(view -> {
            //total price 구해주기
            int tprice = 0;
            for (int i = 0; i < mAdapter.getCount(); i++) {
                tprice += mAdapter.getItemprice(i);
            }

            //메뉴명과 개수 스트링 구해주기
            String orderString = "";
            for (int i = 0; i < mAdapter.getCount(); i++) {
                orderString += mAdapter.getItemname(i) + " " + mAdapter.getItemnum(i) + "ea, ";
            }

            if (tprice == 0) {
                Toast.makeText(this, "장바구니에 메뉴를 담아주세요.", Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(BasketActivity.this);
                builder.setTitle("총 금액은 " + tprice + "입니다. 결제 진행하시겠습니까?"); // 다이얼로그 제목
                builder.setCancelable(false);   // 다이얼로그 화면 밖 터치 방지
                builder.setPositiveButton("예", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //post 요청 보내기 주문 상태 테이블에 내 주문 요청 올려줌
                        OkHttpClient client = new OkHttpClient();

                        //total price 구해주기
                        int tprice = 0;
                        for (int i = 0; i < mAdapter.getCount(); i++) {
                            tprice += mAdapter.getItemprice(i);
                        }

                        //메뉴명과 개수 스트링 구해주기
                        String orderString = "";
                        for (int i = 0; i < mAdapter.getCount(); i++) {
                            orderString += mAdapter.getItemname(i) + " " + mAdapter.getItemnum(i) + "ea, ";
                        }

                        //이름명, 메뉴명과 개수 스트링, 진행상태, 총가격 꽂아줌줌
                        OrderModel OrderModel = new OrderModel();
                        OrderModel.setName(user_id); //유저아이디
                        OrderModel.setMenu_with_num(orderString); //메뉴명과 개수 스트링
                        OrderModel.setOrder_now("Order receipt"); //진행 상태
                        OrderModel.setAllprice(String.valueOf(tprice)); //총가격

                        Gson gson = new Gson();
                        String json = gson.toJson(OrderModel, OrderModel.class);

                        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://dhn62rpfni.execute-api.ap-northeast-2.amazonaws.com/dev/addorder").newBuilder();
                        String url = urlBuilder.build().toString();

                        Request req = new Request.Builder()
                                .url(url)
                                .post(RequestBody.create(MediaType.parse("application/json"), json))
                                .build();

                        client.newCall(req).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                final String myResponse = response.body().string();
                                Gson gson = new GsonBuilder().create();
                                OrderModel responseModel = gson.fromJson(myResponse, OrderModel.class);
                                BasketActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (responseModel.isSuccess() == true) {
                                            Toast.makeText(BasketActivity.this, "주문 접수되었습니다.", Toast.LENGTH_SHORT).show();
                                            Intent intent_menu = new Intent(BasketActivity.this, MenuActivity.class);
                                            intent_menu.putExtra("name", user_id);
                                            finish();
                                            startActivity(intent_menu);
                                        } else {
                                            Toast.makeText(BasketActivity.this, "이미 주문하셨습니다.", Toast.LENGTH_SHORT).show();
                                            Intent intent_menu = new Intent(BasketActivity.this, MenuActivity.class);
                                            intent_menu.putExtra("name", user_id);
                                            finish();
                                            startActivity(intent_menu);
                                        }
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
                builder.show();
            }
        });

    }


    //뒤로가기 이벤트
    @Override
    public void onBackPressed() {   // 뒤로가기 누르면 다이얼로그 생성
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("메뉴로 돌아갈까요? 장바구니에 담은 메뉴는 모두 사라집니다.");
        builder.setCancelable(false);
        builder.setPositiveButton("예", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent_menu = new Intent(BasketActivity.this, MenuActivity.class);
                intent_menu.putExtra("name", user_id);
                finish();
                startActivity(intent_menu);
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
    }
}
