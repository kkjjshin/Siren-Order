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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import edu.skku.cs.sirenorder.Contract.Mycontract;
import edu.skku.cs.sirenorder.Model.DatamodelLogin2;
import edu.skku.cs.sirenorder.Model.DatamodelLogin3;
import edu.skku.cs.sirenorder.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements Mycontract.ContractForMainView {

    EditText et_id;
    EditText et_pw;
    Button btn_login;
    RadioGroup rg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_id = findViewById(R.id.et_id);
        et_pw = findViewById(R.id.et_pw);
        btn_login = findViewById(R.id.btn_login);
        rg = findViewById(R.id.radioGroup);
        RadioButton btn_student = findViewById(R.id.rb_student);
        RadioButton btn_admin = findViewById(R.id.rb_admin);

        btn_student.setChecked(true);


        btn_login.setOnClickListener(view -> {
            int rb_id = rg.getCheckedRadioButtonId();

            if (rb_id == R.id.rb_student) { // 학생 라디오 버튼 클릭시 이벤트
                Intent intent = new Intent(this, MenuActivity.class);
                OkHttpClient client = new OkHttpClient();
                HttpUrl.Builder urlBuilder = HttpUrl.parse("https://dhn62rpfni.execute-api.ap-northeast-2.amazonaws.com/dev/getuserlogin").newBuilder();
                urlBuilder.addQueryParameter("user_id", String.valueOf(et_id.getText()));
                urlBuilder.addQueryParameter("user_pw", String.valueOf(et_pw.getText()));


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
                        final DatamodelLogin2 data_login = gson.fromJson(myResponse, DatamodelLogin2.class);


                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (data_login.getCanlogin().equals("no")) {
                                    Toast.makeText(MainActivity.this, "아이디와 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show();
                                } else if (data_login.getCanlogin().equals("yes")) {
                                    Toast.makeText(MainActivity.this, data_login.getUser_id() + "님 환영합니다", Toast.LENGTH_SHORT).show();
                                    intent.putExtra("name", data_login.getUser_id());
                                    startActivity(intent);
                                }

                            }
                        });
                    }
                });
            } else if (rb_id == R.id.rb_admin) {
                Intent intent2 = new Intent(this, OrderListActivity.class);
                OkHttpClient client = new OkHttpClient();
                HttpUrl.Builder urlBuilder = HttpUrl.parse("https://dhn62rpfni.execute-api.ap-northeast-2.amazonaws.com/dev/getadminlogin").newBuilder();
                urlBuilder.addQueryParameter("admin_id", String.valueOf(et_id.getText()));
                urlBuilder.addQueryParameter("admin_pw", String.valueOf(et_pw.getText()));


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
                        final DatamodelLogin3 data_login_a = gson.fromJson(myResponse, DatamodelLogin3.class);


                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (data_login_a.getCanlogin().equals("no")) {
                                    Toast.makeText(MainActivity.this, "아이디와 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show();
                                } else if (data_login_a.getCanlogin().equals("yes")) {
                                    Toast.makeText(MainActivity.this, data_login_a.getAdmin_id() + "님 환영합니다", Toast.LENGTH_SHORT).show();
                                    intent2.putExtra("name", data_login_a.getAdmin_id());
                                    startActivity(intent2);
                                }

                            }
                        });
                    }
                });

            }

        });


    }

    //뒤로가기 이벤트
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("종료할까요?");
        builder.setCancelable(false);
        builder.setPositiveButton("예", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                exit();
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

    public void exit() { // 종료
        super.onBackPressed();
    }
}