package edu.skku.cs.sirenorder.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import edu.skku.cs.sirenorder.Model.DataModelMenulist;
import edu.skku.cs.sirenorder.Model.MenuModel;
import edu.skku.cs.sirenorder.Presenter.MenuAdapter;
import edu.skku.cs.sirenorder.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MenuActivity extends AppCompatActivity {


    String allMenuArr[] = {"GrapeAde", "AppleAde", "PearAde", "LemonAde", "IceAmericano"
            , "Espresso", "CafeMocha", "ViennaCoffee", "VanillaCoffee", "Cappucchino"
            , "HoneyBread", "Waffle", "Pie", "AppleCookie"};
    String alMenuPriceArr[] = {"3500", "4000", "5000", "4500", "3500", "4000", "4000", "4500",
            "5000", "4000", "4000", "5000", "2000", "1500"};
    HashMap<String, Integer> basketMap = new HashMap<String, Integer>();
    HashMap<String, Integer> basketMap_price = new HashMap<String, Integer>();
    String user_id;


    //어댑터에서 장바구니에 담을 데이터 받아줌.
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String menu = intent.getStringExtra("menu");
            String menu_num = intent.getStringExtra("menu_num");

            int imenu_num = Integer.parseInt(menu_num);
            basketMap.put(menu, basketMap.get(menu) + imenu_num); // 각각의 개수 담김
            if (Integer.parseInt(String.valueOf(basketMap.get(menu))) != 0) {
                Toast.makeText(MenuActivity.this, "장바구니에 " + menu + "가 총 " + String.valueOf(basketMap.get(menu)) + "개 담겼습니다.", Toast.LENGTH_SHORT).show();
            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent secondIntent = getIntent();
        user_id = secondIntent.getStringExtra("name");

        //장바구니에 넘겨줄 map 초기화
        for (int i = 0; i < allMenuArr.length; i++) {
            basketMap.put(allMenuArr[i], 0);
        }


        Button btn_coffee = findViewById(R.id.btn_coffee);
        Button btn_ade = findViewById(R.id.btn_ade);
        Button btn_etc = findViewById(R.id.btn_etc);
        Button btn_gobasket_menu = findViewById(R.id.btn_gobasket_menu);
        Button btn_gologout_menu = findViewById(R.id.btn_gologout_menu);
        Button btn_gomypage_menu = findViewById(R.id.btn_gomypage_menu);

        ListView lv_menu = findViewById(R.id.lv_menu);

        //서버에 올라가 있는 모든 메뉴 메뉴, 가격별 배열 가져와줌
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://dhn62rpfni.execute-api.ap-northeast-2.amazonaws.com/dev/checkallmenu").newBuilder();

        String url = urlBuilder.build().toString();

        Request req = new Request.Builder().url(url).build();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("custom-message"));

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                //커피, 커피가격, 에이드, 에이드가격, 기타, 기타가격 배열로 가져와줌
                final DataModelMenulist data_menu = gson.fromJson(myResponse, DataModelMenulist.class);


                MenuActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //각각 메뉴명, 가격, 개수 어댑터로 꼽아주기

                        //기본 창은 커피창
                        ArrayList<MenuModel> items = new ArrayList<>();
                        ListView menuList = findViewById(R.id.lv_menu);
                        for (int i = 0; i < data_menu.getCoffee().length; i++) {
                            items.add(new MenuModel(data_menu.getCoffee()[i], "0", data_menu.getCoffee_price()[i]));
                        }
                        MenuAdapter mmAdapter = new MenuAdapter(getApplicationContext(), items);
                        menuList.setAdapter(mmAdapter);

                        //커피탭 클릭
                        btn_coffee.setOnClickListener(view -> {
                            ArrayList<MenuModel> items1 = new ArrayList<>();
                            for (int i = 0; i < data_menu.getCoffee().length; i++) {
                                items1.add(new MenuModel(data_menu.getCoffee()[i], "0", data_menu.getCoffee_price()[i]));
                            }
                            MenuAdapter mAdapter = new MenuAdapter(getApplicationContext(), items1);
                            menuList.setAdapter(mAdapter);
                        });

                        //에이드 탭 클릭
                        btn_ade.setOnClickListener(view -> {
                            ArrayList<MenuModel> items2 = new ArrayList<>();
                            for (int i = 0; i < data_menu.getAde().length; i++) {
                                items2.add(new MenuModel(data_menu.getAde()[i], "0", data_menu.getAde_price()[i]));
                            }
                            MenuAdapter mAdapter = new MenuAdapter(getApplicationContext(), items2);
                            menuList.setAdapter(mAdapter);
                        });

                        //기타 탭 클릭
                        btn_etc.setOnClickListener(view -> {
                            ArrayList<MenuModel> items3 = new ArrayList<>();
                            for (int i = 0; i < data_menu.getEtc().length; i++) {
                                items3.add(new MenuModel(data_menu.getEtc()[i], "0", data_menu.getEtc_price()[i]));
                            }
                            MenuAdapter mAdapter = new MenuAdapter(getApplicationContext(), items3);
                            menuList.setAdapter(mAdapter);
                        });

                        //장바구니 탭 클릭
                        btn_gobasket_menu.setOnClickListener(view -> {
                            Intent intent_basket = new Intent(MenuActivity.this, BasketActivity.class);
                            intent_basket.putExtra("basketMap", basketMap);
                            intent_basket.putExtra("user_id", user_id);

                            if (mMessageReceiver != null) {
                                LocalBroadcastManager.getInstance(MenuActivity.this).unregisterReceiver(mMessageReceiver);
                                mMessageReceiver = null;
                            }

                            finish();
                            startActivity(intent_basket);

                        });

                        //마이페이지 탭 클릭
                        btn_gomypage_menu.setOnClickListener(view -> {

                            Intent intent_mypage = new Intent(MenuActivity.this, MypageActivity.class);
                            intent_mypage.putExtra("user_id", user_id);


                            startActivity(intent_mypage);


                        });

                        //로그아웃 탭 클릭
                        btn_gologout_menu.setOnClickListener(view -> {
                            Intent intent_logout = new Intent(MenuActivity.this, MainActivity.class);

                            if (mMessageReceiver != null) {
                                LocalBroadcastManager.getInstance(MenuActivity.this).unregisterReceiver(mMessageReceiver);
                                mMessageReceiver = null;
                            }

                            finish();
                            startActivity(intent_logout);
                        });

                    }
                });
            }
        });


    }


}